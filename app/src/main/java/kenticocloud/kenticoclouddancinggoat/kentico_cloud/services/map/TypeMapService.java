package kenticocloud.kenticoclouddancinggoat.kentico_cloud.services.map;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import kenticocloud.kenticoclouddancinggoat.kentico_cloud.config.DeliveryClientConfig;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.exceptions.KenticoCloudException;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.type.ContentType;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.type.ContentTypeSystemAttributes;
import kenticocloud.kenticoclouddancinggoat.kentico_cloud.models.type.TypeCloudResponses;

public class TypeMapService {

    private DeliveryClientConfig config;
    private ObjectMapper objectMapper;
    private ContentElementMapService contentElementMapService;

    public TypeMapService(@NonNull DeliveryClientConfig config, @NonNull ObjectMapper objectMapper){
        this.config = config;
        this.objectMapper = objectMapper;
        this.contentElementMapService = new ContentElementMapService(config, objectMapper);
    }

    public List<ContentType> mapTypes(List<TypeCloudResponses.ContentTypeResponseRaw> typesRaw) throws JsonProcessingException {
        List<ContentType> types = new ArrayList<>();

        for(TypeCloudResponses.ContentTypeResponseRaw typeRaw : typesRaw){
            types.add(this.mapType(typeRaw));
        }

        return types;
    }

    public ContentType mapType(TypeCloudResponses.DeliverySingleTypeResponseRaw singleTypeResponseRaw) throws JsonProcessingException {
        if (singleTypeResponseRaw == null){
            throw new KenticoCloudException("Cannot create an instance of content type", null);
        }

        return new ContentType(
                this.mapSystemAttributes(singleTypeResponseRaw.system),
                this.contentElementMapService.mapContentTypeElements(singleTypeResponseRaw.elements)
        );
    }

    public ContentType mapType(TypeCloudResponses.ContentTypeResponseRaw typeRaw) throws JsonProcessingException {
            if (typeRaw == null){
                throw new KenticoCloudException("Cannot create an instance of content type", null);
            }

           return new ContentType(
                   this.mapSystemAttributes(typeRaw.system),
                   this.contentElementMapService.mapContentTypeElements(typeRaw.elements)
           );
    }

    private ContentTypeSystemAttributes mapSystemAttributes(TypeCloudResponses.ContentTypeSystemAttributesRaw systemAttributesRaw) {
        return new ContentTypeSystemAttributes(
                systemAttributesRaw.id,
                systemAttributesRaw.name,
                systemAttributesRaw.codename,
                systemAttributesRaw.lastModified
        );
    }
}
