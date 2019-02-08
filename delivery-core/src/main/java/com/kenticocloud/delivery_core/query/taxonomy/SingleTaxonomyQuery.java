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
import com.kenticocloud.delivery_core.models.exceptions.KenticoCloudException;
import com.kenticocloud.delivery_core.models.taxonomy.DeliveryTaxonomyResponse;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SingleTaxonomyQuery extends BaseTaxonomyQuery<SingleTaxonomyQuery> {

    private final String taxonomyCodename;

    public SingleTaxonomyQuery(IDeliveryConfig config, IRxAdapter requestService, IHttpAdapter httpAdapter, String taxonomyCodename) {
        super(config, requestService, httpAdapter);
        this.taxonomyCodename = taxonomyCodename;
    }

    @Override
    public String getQueryUrl(){

        return this.queryService.getUrl(this.config.getDeliveryPaths().getTaxonomiesPath(this.taxonomyCodename), this.parameters, this.queryConfig);
    }

    // observable
    public Observable<DeliveryTaxonomyResponse> getObservable() {
        return this.queryService.<JSONObject>getObservable(this.getQueryUrl(), this.queryConfig, this.getHeaders())
                .map(new Function<JSONObject, DeliveryTaxonomyResponse>() {
                    @Override
                    public DeliveryTaxonomyResponse apply(JSONObject jsonObject) {
                        try {
                            return responseMapService.mapDeliveryTaxonomyResponse(jsonObject);
                        } catch (IOException ex) {
                            throw new KenticoCloudException("Could not get taxonomy response with error: " + ex.getMessage(), ex);
                        }
                    }
                });
    }

    @Override
    public DeliveryTaxonomyResponse get() {
        try {
            return responseMapService.mapDeliveryTaxonomyResponse(this.queryService.getJson(this.getQueryUrl(), this.queryConfig, this.getHeaders()));
        } catch (IOException ex) {
            throw new KenticoCloudException("Could not get taxonomy response with error: " + ex.getMessage(), ex);
        }
    }
}
