/*
 * Copyright 2018 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kenticocloud.delivery_core.query.taxonomy;

import com.kenticocloud.delivery_core.adapters.IHttpAdapter;
import com.kenticocloud.delivery_core.adapters.IRxAdapter;
import com.kenticocloud.delivery_core.config.IDeliveryConfig;
import com.kenticocloud.delivery_core.models.common.Parameters;
import com.kenticocloud.delivery_core.models.exceptions.KenticoCloudException;
import com.kenticocloud.delivery_core.models.taxonomy.DeliveryTaxonomyListingResponse;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MultipleTaxonomyQuery extends BaseTaxonomyQuery<MultipleTaxonomyQuery> {

    public MultipleTaxonomyQuery(IDeliveryConfig config, IRxAdapter requestService, IHttpAdapter httpAdapter) {
        super(config, requestService, httpAdapter);
    }

    @Override
    public String getQueryUrl(){
        return this.queryService.getUrl(this.config.getDeliveryPaths().getTaxonomiesPath(), this.parameters, this.queryConfig);
    }

    public MultipleTaxonomyQuery limitParameter(int limit){
        this.parameters.add(new Parameters.LimitParameter(limit));
        return this;
    }

    public MultipleTaxonomyQuery skipParameter(int skip){
        this.parameters.add(new Parameters.SkipParameter(skip));
        return this;
    }

    public Observable<DeliveryTaxonomyListingResponse> getObservable() {
        return this.queryService.<JSONObject>getObservable(this.getQueryUrl(), this.queryConfig, this.getHeaders())
                .map(new Function<JSONObject, DeliveryTaxonomyListingResponse>() {
                    @Override
                    public DeliveryTaxonomyListingResponse apply(JSONObject jsonObject) {
                        try {
                            return responseMapService.mapDeliveryTaxonomyListingResponse(jsonObject);
                        } catch (IOException ex) {
                            throw new KenticoCloudException("Could not get taxonomies response with error: " + ex.getMessage(), ex);
                        }
                    }
                });
    }

    @Override
    public DeliveryTaxonomyListingResponse get() {
        try {
            return responseMapService.mapDeliveryTaxonomyListingResponse(this.queryService.getJson(this.getQueryUrl(), this.queryConfig, this.getHeaders()));
        } catch (IOException ex) {
            throw new KenticoCloudException("Could not get taxonomies response with error: " + ex.getMessage(), ex);
        }
    }

}
