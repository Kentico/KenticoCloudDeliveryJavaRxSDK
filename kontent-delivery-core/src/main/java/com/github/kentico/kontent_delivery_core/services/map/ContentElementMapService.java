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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.models.element.ContentTypeElement;
import com.github.kentico.kontent_delivery_core.models.element.ContentTypeElementOption;
import com.github.kentico.kontent_delivery_core.models.element.ElementKontentResponses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContentElementMapService {

    private IDeliveryConfig config;
    private ObjectMapper objectMapper;

    public ContentElementMapService(IDeliveryConfig config, ObjectMapper objectMapper){
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public List<ContentTypeElement> mapContentTypeElements(JsonNode elementsNode) throws JsonProcessingException {
        List<ContentTypeElement> elements = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iterator = elementsNode.fields();

        for (; iterator.hasNext(); ) {

            Map.Entry<String, JsonNode> item = iterator.next();
            if (item == null){
                continue;
            }
            String elementCodename = item.getKey();
            JsonNode elementNode = item.getValue();

            elements.add(this.mapContentTypeElement(elementCodename, elementNode));
        }

        return elements;
    }

    public ContentTypeElement mapContentTypeElement(ElementKontentResponses.ContentTypeElementResponseRaw response) throws JsonProcessingException {

        return new ContentTypeElement(
                response.name,
                response.codename,
                response.type,
                response.taxonomyGroup,
                response.options == null ? null : this.mapOptions(response.options));
    }

    public ContentTypeElement mapContentTypeElement(String elementCodename, JsonNode elementNode) throws JsonProcessingException {
        String name = elementNode.get("name") == null ? null : elementNode.get("name").asText();
        String type = elementNode.get("type") == null ? null : elementNode.get("type").asText();
        String taxonomyGroup = elementNode.get("taxonomy_group") == null ? null : elementNode.get("taxonomy_group").asText();
        ElementKontentResponses.ContentTypeElementOptionRaw[] options = elementNode.get("options") == null ? null : this.objectMapper.treeToValue(elementNode.get("options"), ElementKontentResponses.ContentTypeElementOptionRaw[].class);

        return new ContentTypeElement(
                name,
                elementCodename,
                type,
                taxonomyGroup,
                options == null ? null : this.mapOptions(options));
    }

    private List<ContentTypeElementOption> mapOptions(ElementKontentResponses.ContentTypeElementOptionRaw[] optionsRaw){
        List<ContentTypeElementOption> options = new ArrayList<>();

        for(ElementKontentResponses.ContentTypeElementOptionRaw optionRaw : optionsRaw){
            options.add(new ContentTypeElementOption(optionRaw.name, optionRaw.codename));
        }

        return options;
    }
}


