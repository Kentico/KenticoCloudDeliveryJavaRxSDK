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
import com.github.kentico.kontent_delivery_core.enums.FieldType;
import com.github.kentico.kontent_delivery_core.interfaces.item.IContentItem;
import com.github.kentico.kontent_delivery_core.elements.AssetsElement;
import com.github.kentico.kontent_delivery_core.elements.ContentElement;
import com.github.kentico.kontent_delivery_core.elements.CustomElement;
import com.github.kentico.kontent_delivery_core.elements.DateTimeElement;
import com.github.kentico.kontent_delivery_core.elements.LinkedItemsElement;
import com.github.kentico.kontent_delivery_core.elements.MultipleChoiceElement;
import com.github.kentico.kontent_delivery_core.elements.NumberElement;
import com.github.kentico.kontent_delivery_core.elements.RichTextElement;
import com.github.kentico.kontent_delivery_core.elements.TaxonomyElement;
import com.github.kentico.kontent_delivery_core.elements.TextElement;
import com.github.kentico.kontent_delivery_core.elements.UrlSlugElement;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;
import com.github.kentico.kontent_delivery_core.models.item.ContentItem;
import com.github.kentico.kontent_delivery_core.models.item.ContentItemSystemAttributes;
import com.github.kentico.kontent_delivery_core.models.item.ElementMapping;
import com.github.kentico.kontent_delivery_core.models.item.ItemKontentResponses;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemMapService {

    private IDeliveryConfig config;
    private ObjectMapper objectMapper;
    private List<IContentItem> processedLinkedItems = new ArrayList<>();

    public ItemMapService(IDeliveryConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public <TItem extends IContentItem> List<TItem> mapItems(List<ItemKontentResponses.ContentItemRaw> rawItems, JsonNode linkedItems) throws JsonProcessingException, IllegalAccessException {
        List<TItem> mappedItems = new ArrayList<>();

        for (ItemKontentResponses.ContentItemRaw rawItem : rawItems) {
            mappedItems.add(this.<TItem>mapItem(rawItem, linkedItems));
        }

        return mappedItems;
    }

    public <TItem extends IContentItem> TItem mapItem(ItemKontentResponses.ContentItemRaw rawItem, JsonNode linkedItems) throws JsonProcessingException, IllegalAccessException {
        // try getting the mapped item using the resolver if available
        TItem item;
        boolean stronglyTyped = false;

        try {
            item = tryGetInstanceFromTypeResolver(this.config.getTypeResolvers(), rawItem.system.type);

        } catch (Exception ex) {
            throw new KontentException("Instantiating strongly typed instance for '" + rawItem.system.type + "' failed", ex);
        }

        if (item == null) {
            stronglyTyped = false;
            // throw Exception if the type is not registered and user wishes to do so
            if (this.config.getThrowExceptionForUnknownTypes()) {
                throw new KontentException("Could not create an instance of '" + rawItem.system.type + "' type", null);
            }
            item = this.getContentItemInstance();
        } else {
            stronglyTyped = true;
        }

        // add item to list to prevent infinite recursion when resolving linked items
        boolean itemFound = false;
        for (IContentItem processedItem : this.processedLinkedItems) {
            if (processedItem.getSystem().getCodename().equalsIgnoreCase(rawItem.system.codename)) {
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            this.processedLinkedItems.add(item);
        }

        // system attributes
        item.setContentItemSystemAttributes(this.mapSystemAttributes(rawItem.system));

        if (stronglyTyped) {
            return this.mapStronglyTypedItem(item, rawItem, linkedItems);
        }

        return this.mapContentItem(item, rawItem, linkedItems);
    }

    private <TItem extends IContentItem> TItem mapStronglyTypedItem(TItem item, ItemKontentResponses.ContentItemRaw rawItem, JsonNode linkedItems) throws JsonProcessingException, IllegalAccessException {
        List<ContentElement<?>> elements = new ArrayList<>();

        // get properties
        Field[] fields = item.getClass().getDeclaredFields();

        // map elements into all linked properties
        for (Field field : fields) {
            ElementMapping elementMapping = field.getAnnotation(ElementMapping.class);

            // property does not have element mapping, skip it
            if (elementMapping == null) {
                continue;
            }

            // see if the element exists in Kontent Json response
            String elementCodename = elementMapping.value();
            JsonNode elementNode = rawItem.elements.get(elementCodename);

            // property that we tried to map does not exist in the response. This may be because
            // the property was not included in projection or it simply does not exist in Kontent
            // we should skip such properties
            if (elementNode == null) {
                continue;
            }

            // prepare element
            ItemKontentResponses.ElementRaw elementRaw;

            // get element
            elementRaw = this.objectMapper.treeToValue(elementNode, ItemKontentResponses.ElementRaw.class);

            // proceed as the property was annotated with {@link ElementMapping)
            if (elementRaw.value != null) {

                ContentElement<?> element = mapElement(elementRaw.name, elementCodename, elementRaw.type, elementRaw.value, linkedItems);
                field.set(item, element);
                elements.add(element);
            }
        }

        // elements
        item.setElements(elements);

        return item;
    }

    private <TItem extends IContentItem> TItem mapContentItem(TItem item, ItemKontentResponses.ContentItemRaw rawItem, JsonNode linkedItems) throws JsonProcessingException, IllegalAccessException {
        item.setElements(this.getContentElements(rawItem, linkedItems));

        return item;
    }

    private List<ContentElement<?>> getContentElements(ItemKontentResponses.ContentItemRaw rawItem, JsonNode linkedItems) throws JsonProcessingException, IllegalAccessException {
        List<ContentElement<?>> elements = new ArrayList<>();

        for (JsonNode element : rawItem.elements) {
            ItemKontentResponses.ElementRaw elementRaw;

            // get element
            elementRaw = this.objectMapper.treeToValue(element, ItemKontentResponses.ElementRaw.class);
            elements.add(mapElement(elementRaw.name, element.asText(), elementRaw.type, elementRaw.value, linkedItems));
        }

        return elements;
    }

    private ContentElement mapElement(String name, String codename, String type, JsonNode value, JsonNode linkedItems) throws JsonProcessingException,IllegalAccessException {
        FieldType fieldType = getFieldTypeEnum(type);

        switch (fieldType) {
            case text:
                return new TextElement(this.objectMapper, name, codename, type, value);
            case rich_text:
                return new RichTextElement(this.objectMapper, name, codename, type, value);
            case asset:
                return new AssetsElement(this.objectMapper, name, codename, type, value);
            case number:
                return new NumberElement(this.objectMapper, name, codename, type, value);
            case date_time:
                return new DateTimeElement(this.objectMapper, name, codename, type, value);
            case url_slug:
                return new UrlSlugElement(this.objectMapper, name, codename, type, value);
            case taxonomy:
                return new TaxonomyElement(this.objectMapper, name, codename, type, value);
            case modular_content:
                return mapLinkedItemsElement(name, codename, type, value, linkedItems);
            case multiple_choice:
                return new MultipleChoiceElement(this.objectMapper, name, codename, type, value);
            case custom:
                return new CustomElement(this.objectMapper, name, codename, type, value);
            default:
                return ThrowOnUnsupportedType(type, null);
        }
    }

    private FieldType getFieldTypeEnum(String type) {
        try {
            return FieldType.valueOf(type);
        } catch (IllegalArgumentException exception) {
            return ThrowOnUnsupportedType(type, exception);
        }
    }

    private <T> T ThrowOnUnsupportedType(String type, Throwable cause) {
        throw new KontentException("Field type '" + type + "' is not supported", cause);
    }

    private LinkedItemsElement mapLinkedItemsElement(String name, String fieldCodename, String type, JsonNode fieldValue, JsonNode linkedItems) throws JsonProcessingException, IllegalAccessException {
        ArrayList<IContentItem> fieldLinkItems = new ArrayList<>();
        Iterator<JsonNode> iterator = fieldValue.elements();

        while (iterator.hasNext()) {
            String linkedItemCodename = iterator.next().textValue();

            IContentItem linkedItem = null;

            // take item from process items as to avoid infinite recursion
            for (IContentItem processedItem : this.processedLinkedItems) {
                if (processedItem.getSystem().getCodename().equalsIgnoreCase(linkedItemCodename)) {
                    // item found in processed item, use it
                    linkedItem = processedItem;
                    break;
                }
            }

            if (linkedItem == null) {
                // try getting the item from linked items response
                JsonNode linkedItemFromResponse = linkedItems.get(linkedItemCodename);

                if (linkedItemFromResponse == null) {
                    throw new KontentException("Could not map linked items element field '" + fieldCodename + "' because the linked item '" + linkedItemCodename + "' is not present in response. Make sure you set 'Depth' parameter if you need this item.", null);
                }

                ItemKontentResponses.ContentItemRaw contentItemRaw;

                try {
                    contentItemRaw = this.objectMapper.readValue(linkedItemFromResponse.toString(), ItemKontentResponses.ContentItemRaw.class);
                } catch (IOException e) {
                    throw new KontentException("Could not parse item response for linked item element '" + fieldCodename + "'", e);
                }

                linkedItem = this.mapItem(contentItemRaw, linkedItems);
            }

            if (linkedItem == null) {
                throw new KontentException("Linked item '" + linkedItemCodename + "' could not be resolved for field '" + fieldCodename + "'", null);
            }

            fieldLinkItems.add(linkedItem);
        }

        return new LinkedItemsElement<>(this.objectMapper, name, fieldCodename, type, fieldValue, fieldLinkItems);
    }

    @SuppressWarnings("unchecked")
    private <TItem extends IContentItem> TItem tryGetInstanceFromTypeResolver(List<TypeResolver<?>> typeResolvers, String type) throws Exception {
        // get type resolver of given type
        for (TypeResolver typeResolver : typeResolvers) {
            // type resolver matched requested type
            if (typeResolver.getType().equalsIgnoreCase(type)) {
                // create new instance of proper type
                return (TItem) typeResolver.createInstance();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <TItem extends IContentItem> TItem getContentItemInstance() {
        return (TItem) new ContentItem();
    }

    private ContentItemSystemAttributes mapSystemAttributes(ItemKontentResponses.ContentItemSystemAttributesRaw systemRaw) {
        return new ContentItemSystemAttributes(
                systemRaw.id,
                systemRaw.name,
                systemRaw.codename,
                systemRaw.type,
                systemRaw.lastModified,
                systemRaw.language,
                systemRaw.sitemapLocations
        );
    }
}
