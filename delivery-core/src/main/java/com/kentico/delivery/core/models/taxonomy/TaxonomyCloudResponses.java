/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.core.models.taxonomy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kentico.delivery.core.deserializers.CloudDateDeserializer;
import com.kentico.delivery.core.models.common.CommonCloudResponses;

import java.util.Date;
import java.util.List;

public class TaxonomyCloudResponses {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxonomyTermsRaw {

        TaxonomyTermsRaw(){}

        @JsonProperty("name")
        public String name;

        @JsonProperty("codename")
        public String codename;

        @JsonProperty("terms")
        public List<TaxonomyTermsRaw> terms;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxonomySystemAttributesRaw {

        TaxonomySystemAttributesRaw(){}

        @JsonProperty("id")
        public String id;

        @JsonProperty("name")
        public String name;

        @JsonProperty("codename")
        public String codename;

        @JsonProperty("last_modified")
        @JsonDeserialize(using = CloudDateDeserializer.class)
        public Date lastModified;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxonomyMultipleResponseRaw {

        TaxonomyMultipleResponseRaw(){
            // Mandatory constructor
        }

        @JsonProperty("taxonomies")
        public List<TaxonomyRaw> taxonomies;

        @JsonProperty("pagination")
        public CommonCloudResponses.PaginationRaw pagination;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxonomySingleResponseRaw {

        TaxonomySingleResponseRaw(){
            // Mandatory constructor
        }

        @JsonProperty("system")
        public TaxonomySystemAttributesRaw system;

        @JsonProperty("terms")
        public List<TaxonomyTermsRaw> terms;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxonomyRaw {

        TaxonomyRaw(){
            // Mandatory constructor
        }

        @JsonProperty("system")
        public TaxonomySystemAttributesRaw system;

        @JsonProperty("terms")
        public List<TaxonomyTermsRaw> terms;

    }
}
