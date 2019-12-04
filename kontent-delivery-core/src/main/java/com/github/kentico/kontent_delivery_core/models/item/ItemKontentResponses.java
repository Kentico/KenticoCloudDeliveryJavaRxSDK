/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.models.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.kentico.kontent_delivery_core.deserializers.KontentDateDeserializer;
import com.github.kentico.kontent_delivery_core.models.common.CommonKontentResponses;

import java.util.Date;
import java.util.List;

public class ItemKontentResponses {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryItemListingResponseRaw {

        DeliveryItemListingResponseRaw() {
            // Mandatory constructor
        }

        @JsonProperty("items")
        public List<ContentItemRaw> items;

        @JsonProperty("modular_content")
        public JsonNode linkedItems;

        @JsonProperty("pagination")
        public CommonKontentResponses.PaginationRaw pagination;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryItemResponseRaw {

        DeliveryItemResponseRaw() {
            // Mandatory constructor
        }

        @JsonProperty("item")
        public ContentItemRaw item;

        @JsonProperty("modular_content")
        public JsonNode linkedItems;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentItemRaw {

        ContentItemRaw() {
            // Mandatory constructor
        }

        @JsonProperty("system")
        public ContentItemSystemAttributesRaw system;

        @JsonProperty("elements")
        public JsonNode elements;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ElementRaw {

        ElementRaw() {
            // Mandatory constructor
        }

        @JsonProperty("name")
        public String name;

        @JsonProperty("type")
        public String type;

        @JsonProperty("value")
        public JsonNode value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentItemSystemAttributesRaw {

        ContentItemSystemAttributesRaw() {
            // Mandatory constructor
        }

        @JsonProperty("id")
        public String id;

        @JsonProperty("name")
        public String name;

        @JsonProperty("codename")
        public String codename;

        @JsonProperty("language")
        public String language;

        @JsonProperty("type")
        public String type;

        @JsonProperty("sitemap_locations")
        public String[] sitemapLocations;

        @JsonProperty("last_modified")
        @JsonDeserialize(using = KontentDateDeserializer.class)
        public Date lastModified;
    }
}

