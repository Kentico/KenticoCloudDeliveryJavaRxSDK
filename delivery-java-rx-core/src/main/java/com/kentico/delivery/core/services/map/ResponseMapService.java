package com.kentico.delivery.core.services.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kentico.delivery.core.config.DeliveryClientConfig;
import com.kentico.delivery.core.interfaces.item.item.IContentItem;
import com.kentico.delivery.core.models.item.DeliveryItemListingResponse;
import com.kentico.delivery.core.models.item.DeliveryItemResponse;
import com.kentico.delivery.core.models.item.ItemCloudResponses;
import com.kentico.delivery.core.models.type.DeliverySingleTypeResponse;
import com.kentico.delivery.core.models.type.DeliveryTypeListingResponse;
import com.kentico.delivery.core.models.type.TypeCloudResponses;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public final class ResponseMapService {

    private PaginationMapService paginationMapService;
    private ItemMapService itemMapService;
    private TypeMapService typeMapService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ResponseMapService( DeliveryClientConfig config){
        this.itemMapService = new ItemMapService(config, this.objectMapper);
        this.typeMapService = new TypeMapService(config, this.objectMapper);
        this.paginationMapService = new PaginationMapService(config, this.objectMapper);
    }

    public<TItem extends IContentItem> DeliveryItemResponse<TItem> mapItemResponse(JSONObject cloudResponse) throws JSONException, IOException, IllegalAccessException {
        ItemCloudResponses.DeliveryItemResponseRaw rawResponse = this.objectMapper.readValue(cloudResponse.toString(), ItemCloudResponses.DeliveryItemResponseRaw.class);

        TItem item = this.itemMapService.mapItem(rawResponse.item, rawResponse.modularContent);

        return new DeliveryItemResponse<>(item);
    }

    public<TItem extends IContentItem> DeliveryItemListingResponse<TItem> mapItemListingResponse(JSONObject cloudResponse) throws JSONException, IOException, IllegalAccessException {
        ItemCloudResponses.DeliveryItemListingResponseRaw rawResponse = this.objectMapper.readValue(cloudResponse.toString(), ItemCloudResponses.DeliveryItemListingResponseRaw.class);

        List<TItem> items = this.itemMapService.mapItems(rawResponse.items, rawResponse.modularContent);

        return new DeliveryItemListingResponse<TItem>(items, this.paginationMapService.mapPagination(rawResponse.pagination));
    }

    public DeliverySingleTypeResponse mapDeliverySingleTypeResponse(JSONObject cloudResponse) throws IOException {
        TypeCloudResponses.DeliverySingleTypeResponseRaw rawResponse = this.objectMapper.readValue(cloudResponse.toString(), TypeCloudResponses.DeliverySingleTypeResponseRaw.class);

        return new DeliverySingleTypeResponse(this.typeMapService.mapType(rawResponse));
    }

    public DeliveryTypeListingResponse mapDeliveryMultipleTypesResponse(JSONObject cloudResponse) throws IOException {
        TypeCloudResponses.DeliveryMultipleTypeResponseRaw rawResponse = this.objectMapper.readValue(cloudResponse.toString(), TypeCloudResponses.DeliveryMultipleTypeResponseRaw.class);

        return new DeliveryTypeListingResponse(this.typeMapService.mapTypes(rawResponse.types), this.paginationMapService.mapPagination(rawResponse.pagination));
    }
}

