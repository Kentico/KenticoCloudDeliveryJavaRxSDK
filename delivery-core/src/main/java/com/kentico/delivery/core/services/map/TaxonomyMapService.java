/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.core.services.map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kentico.delivery.core.config.DeliveryConfig;
import com.kentico.delivery.core.models.exceptions.KenticoCloudException;
import com.kentico.delivery.core.models.taxonomy.Taxonomy;
import com.kentico.delivery.core.models.taxonomy.TaxonomyCloudResponses;
import com.kentico.delivery.core.models.taxonomy.TaxonomySystemAttributes;
import com.kentico.delivery.core.models.taxonomy.TaxonomyTerms;

import java.util.ArrayList;
import java.util.List;

public class TaxonomyMapService {

    private DeliveryConfig config;
    private ObjectMapper objectMapper;

    public TaxonomyMapService(DeliveryConfig config, ObjectMapper objectMapper){
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public List<Taxonomy> mapTaxonomies(List<TaxonomyCloudResponses.TaxonomyRaw> taxonomiesRaw) throws JsonProcessingException {
        List<Taxonomy> taxonomies = new ArrayList<>();

        for(TaxonomyCloudResponses.TaxonomyRaw taxonomyRaw : taxonomiesRaw){
            taxonomies.add(this.mapTaxonomy(taxonomyRaw));
        }

        return taxonomies;
    }

    public Taxonomy mapTaxonomy(TaxonomyCloudResponses.TaxonomySystemAttributesRaw system, List<TaxonomyCloudResponses.TaxonomyTermsRaw> terms) throws JsonProcessingException {
        if (system == null || terms == null){
            throw new KenticoCloudException("Cannot create an instance of taxonomy", null);
        }

        return new Taxonomy(
                this.mapSystemAttributes(system),
                this.mapListOfTaxonomyTerms(terms)
        );
    }

    public Taxonomy mapTaxonomy(TaxonomyCloudResponses.TaxonomyRaw taxonomyRaw) throws JsonProcessingException {
        if (taxonomyRaw == null){
            throw new KenticoCloudException("Cannot create an instance of taxonomy", null);
        }

        return new Taxonomy(
                this.mapSystemAttributes(taxonomyRaw.system),
                this.mapListOfTaxonomyTerms(taxonomyRaw.terms)
        );
    }

    private List<TaxonomyTerms> mapListOfTaxonomyTerms(List<TaxonomyCloudResponses.TaxonomyTermsRaw> termsListRaw){
        List<TaxonomyTerms> terms = new ArrayList<>();

        if (termsListRaw == null){
            return terms;
        }

        for(TaxonomyCloudResponses.TaxonomyTermsRaw termsRaw : termsListRaw){
            terms.add(this.mapTaxonomyTerm(termsRaw));
        }

        return terms;
    }

    private TaxonomyTerms mapTaxonomyTerm(TaxonomyCloudResponses.TaxonomyTermsRaw termsRaw){
        if (termsRaw == null){
            return null;
        }

        return new TaxonomyTerms(
                termsRaw.name,
                termsRaw.codename,
                this.mapListOfTaxonomyTerms(termsRaw.terms)
        );
    }

    private TaxonomySystemAttributes mapSystemAttributes(TaxonomyCloudResponses.TaxonomySystemAttributesRaw systemAttributesRaw) {
        return new TaxonomySystemAttributes(
                systemAttributesRaw.id,
                systemAttributesRaw.name,
                systemAttributesRaw.codename,
                systemAttributesRaw.lastModified
        );
    }
}
