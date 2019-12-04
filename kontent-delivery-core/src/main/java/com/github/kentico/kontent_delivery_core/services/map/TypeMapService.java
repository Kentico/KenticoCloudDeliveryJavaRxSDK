/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.services.map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;
import com.github.kentico.kontent_delivery_core.models.type.ContentType;
import com.github.kentico.kontent_delivery_core.models.type.ContentTypeSystemAttributes;
import com.github.kentico.kontent_delivery_core.models.type.TypeKontentResponses;

import java.util.ArrayList;
import java.util.List;

public class TypeMapService {

    private IDeliveryConfig config;
    private ObjectMapper objectMapper;
    private ContentElementMapService contentElementMapService;

    public TypeMapService(IDeliveryConfig config, ObjectMapper objectMapper){
        this.config = config;
        this.objectMapper = objectMapper;
        this.contentElementMapService = new ContentElementMapService(config, objectMapper);
    }

    public List<ContentType> mapTypes(List<TypeKontentResponses.ContentTypeResponseRaw> typesRaw) throws JsonProcessingException {
        List<ContentType> types = new ArrayList<>();

        for(TypeKontentResponses.ContentTypeResponseRaw typeRaw : typesRaw){
            types.add(this.mapType(typeRaw));
        }

        return types;
    }

    public ContentType mapType(TypeKontentResponses.DeliverySingleTypeResponseRaw singleTypeResponseRaw) throws JsonProcessingException {
        if (singleTypeResponseRaw == null){
            throw new KontentException("Cannot create an instance of content type", null);
        }

        return new ContentType(
                this.mapSystemAttributes(singleTypeResponseRaw.system),
                this.contentElementMapService.mapContentTypeElements(singleTypeResponseRaw.elements)
        );
    }

    public ContentType mapType(TypeKontentResponses.ContentTypeResponseRaw typeRaw) throws JsonProcessingException {
            if (typeRaw == null){
                throw new KontentException("Cannot create an instance of content type", null);
            }

           return new ContentType(
                   this.mapSystemAttributes(typeRaw.system),
                   this.contentElementMapService.mapContentTypeElements(typeRaw.elements)
           );
    }

    private ContentTypeSystemAttributes mapSystemAttributes(TypeKontentResponses.ContentTypeSystemAttributesRaw systemAttributesRaw) {
        return new ContentTypeSystemAttributes(
                systemAttributesRaw.id,
                systemAttributesRaw.name,
                systemAttributesRaw.codename,
                systemAttributesRaw.lastModified
        );
    }
}
