/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.interfaces.item.IContentItem;
import com.github.kentico.kontent_delivery_core.services.map.ContentElementMapService;
import com.github.kentico.kontent_delivery_core.services.map.ItemMapService;
import com.github.kentico.kontent_delivery_core.services.map.PaginationMapService;
import com.github.kentico.kontent_delivery_core.services.map.TaxonomyMapService;
import com.github.kentico.kontent_delivery_core.services.map.TypeMapService;
import com.github.kentico.kontent_delivery_core.models.element.DeliveryContentTypeElementResponse;
import com.github.kentico.kontent_delivery_core.models.element.ElementKontentResponses;
import com.github.kentico.kontent_delivery_core.models.item.DeliveryItemListingResponse;
import com.github.kentico.kontent_delivery_core.models.item.DeliveryItemResponse;
import com.github.kentico.kontent_delivery_core.models.item.ItemKontentResponses;
import com.github.kentico.kontent_delivery_core.models.taxonomy.DeliveryTaxonomyListingResponse;
import com.github.kentico.kontent_delivery_core.models.taxonomy.DeliveryTaxonomyResponse;
import com.github.kentico.kontent_delivery_core.models.taxonomy.TaxonomyKontentResponses;
import com.github.kentico.kontent_delivery_core.models.type.DeliveryTypeListingResponse;
import com.github.kentico.kontent_delivery_core.models.type.DeliveryTypeResponse;
import com.github.kentico.kontent_delivery_core.models.type.TypeKontentResponses;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public final class ResponseMapService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private PaginationMapService paginationMapService;
    private ItemMapService itemMapService;
    private TypeMapService typeMapService;
    private TaxonomyMapService taxonomyMapService;
    private ContentElementMapService contentElementMapService;

    public ResponseMapService(IDeliveryConfig config) {
        this.itemMapService = new ItemMapService(config, this.objectMapper);
        this.typeMapService = new TypeMapService(config, this.objectMapper);
        this.taxonomyMapService = new TaxonomyMapService(config, this.objectMapper);
        this.paginationMapService = new PaginationMapService(config, this.objectMapper);
        this.contentElementMapService = new ContentElementMapService(config, this.objectMapper);
    }

    public <TItem extends IContentItem> DeliveryItemResponse<TItem> mapItemResponse(JSONObject kontentResponse) throws JSONException, IOException, IllegalAccessException {
        ItemKontentResponses.DeliveryItemResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), ItemKontentResponses.DeliveryItemResponseRaw.class);

        TItem item = this.itemMapService.mapItem(rawResponse.item, rawResponse.linkedItems);

        return new DeliveryItemResponse<>(item);
    }

    public <TItem extends IContentItem> DeliveryItemListingResponse<TItem> mapItemListingResponse(JSONObject kontentResponse) throws JSONException, IOException, IllegalAccessException {
        ItemKontentResponses.DeliveryItemListingResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), ItemKontentResponses.DeliveryItemListingResponseRaw.class);

        List<TItem> items = this.itemMapService.mapItems(rawResponse.items, rawResponse.linkedItems);

        return new DeliveryItemListingResponse<>(items, this.paginationMapService.mapPagination(rawResponse.pagination));
    }

    public DeliveryTypeResponse mapDeliverySingleTypeResponse(JSONObject kontentResponse) throws IOException {
        TypeKontentResponses.DeliverySingleTypeResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), TypeKontentResponses.DeliverySingleTypeResponseRaw.class);

        return new DeliveryTypeResponse(this.typeMapService.mapType(rawResponse));
    }

    public DeliveryTypeListingResponse mapDeliveryMultipleTypesResponse(JSONObject kontentResponse) throws IOException {
        TypeKontentResponses.DeliveryMultipleTypeResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), TypeKontentResponses.DeliveryMultipleTypeResponseRaw.class);

        return new DeliveryTypeListingResponse(this.typeMapService.mapTypes(rawResponse.types), this.paginationMapService.mapPagination(rawResponse.pagination));
    }

    public DeliveryTaxonomyListingResponse mapDeliveryTaxonomyListingResponse(JSONObject kontentResponse) throws IOException {
        TaxonomyKontentResponses.TaxonomyMultipleResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), TaxonomyKontentResponses.TaxonomyMultipleResponseRaw.class);

        return new DeliveryTaxonomyListingResponse(this.taxonomyMapService.mapTaxonomies(rawResponse.taxonomies), this.paginationMapService.mapPagination(rawResponse.pagination));
    }

    public DeliveryTaxonomyResponse mapDeliveryTaxonomyResponse(JSONObject kontentResponse) throws IOException {
        TaxonomyKontentResponses.TaxonomySingleResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), TaxonomyKontentResponses.TaxonomySingleResponseRaw.class);

        return new DeliveryTaxonomyResponse(this.taxonomyMapService.mapTaxonomy(rawResponse.system, rawResponse.terms));
    }


    public DeliveryContentTypeElementResponse mapDeliveryContentTypeResponse(JSONObject kontentResponse) throws IOException {
        ElementKontentResponses.ContentTypeElementResponseRaw rawResponse = this.objectMapper.readValue(kontentResponse.toString(), ElementKontentResponses.ContentTypeElementResponseRaw.class);

        return new DeliveryContentTypeElementResponse(this.contentElementMapService.mapContentTypeElement(rawResponse));
    }
}
