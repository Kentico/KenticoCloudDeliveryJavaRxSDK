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
import com.github.kentico.kontent_delivery_core.models.taxonomy.Taxonomy;
import com.github.kentico.kontent_delivery_core.models.taxonomy.TaxonomyKontentResponses;
import com.github.kentico.kontent_delivery_core.models.taxonomy.TaxonomySystemAttributes;
import com.github.kentico.kontent_delivery_core.models.taxonomy.TaxonomyTerms;

import java.util.ArrayList;
import java.util.List;

public class TaxonomyMapService {

    private IDeliveryConfig config;
    private ObjectMapper objectMapper;

    public TaxonomyMapService(IDeliveryConfig config, ObjectMapper objectMapper){
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public List<Taxonomy> mapTaxonomies(List<TaxonomyKontentResponses.TaxonomyRaw> taxonomiesRaw) throws JsonProcessingException {
        List<Taxonomy> taxonomies = new ArrayList<>();

        for(TaxonomyKontentResponses.TaxonomyRaw taxonomyRaw : taxonomiesRaw){
            taxonomies.add(this.mapTaxonomy(taxonomyRaw));
        }

        return taxonomies;
    }

    public Taxonomy mapTaxonomy(TaxonomyKontentResponses.TaxonomySystemAttributesRaw system, List<TaxonomyKontentResponses.TaxonomyTermsRaw> terms) throws JsonProcessingException {
        if (system == null || terms == null){
            throw new KontentException("Cannot create an instance of taxonomy", null);
        }

        return new Taxonomy(
                this.mapSystemAttributes(system),
                this.mapListOfTaxonomyTerms(terms)
        );
    }

    public Taxonomy mapTaxonomy(TaxonomyKontentResponses.TaxonomyRaw taxonomyRaw) throws JsonProcessingException {
        if (taxonomyRaw == null){
            throw new KontentException("Cannot create an instance of taxonomy", null);
        }

        return new Taxonomy(
                this.mapSystemAttributes(taxonomyRaw.system),
                this.mapListOfTaxonomyTerms(taxonomyRaw.terms)
        );
    }

    private List<TaxonomyTerms> mapListOfTaxonomyTerms(List<TaxonomyKontentResponses.TaxonomyTermsRaw> termsListRaw){
        List<TaxonomyTerms> terms = new ArrayList<>();

        if (termsListRaw == null){
            return terms;
        }

        for(TaxonomyKontentResponses.TaxonomyTermsRaw termsRaw : termsListRaw){
            terms.add(this.mapTaxonomyTerm(termsRaw));
        }

        return terms;
    }

    private TaxonomyTerms mapTaxonomyTerm(TaxonomyKontentResponses.TaxonomyTermsRaw termsRaw){
        if (termsRaw == null){
            return null;
        }

        return new TaxonomyTerms(
                termsRaw.name,
                termsRaw.codename,
                this.mapListOfTaxonomyTerms(termsRaw.terms)
        );
    }

    private TaxonomySystemAttributes mapSystemAttributes(TaxonomyKontentResponses.TaxonomySystemAttributesRaw systemAttributesRaw) {
        return new TaxonomySystemAttributes(
                systemAttributesRaw.id,
                systemAttributesRaw.name,
                systemAttributesRaw.codename,
                systemAttributesRaw.lastModified
        );
    }
}
