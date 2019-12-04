/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.query.type;

import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.adapters.IHttpAdapter;
import com.github.kentico.kontent_delivery_core.adapters.IRxAdapter;
import com.github.kentico.kontent_delivery_core.models.common.Parameters;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;
import com.github.kentico.kontent_delivery_core.models.type.DeliveryTypeListingResponse;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MultipleTypeQuery extends BaseTypeQuery<MultipleTypeQuery> {

    public MultipleTypeQuery(IDeliveryConfig config, IRxAdapter requestService, IHttpAdapter httpAdapter) {
        super(config, requestService, httpAdapter);
    }

    @Override
    public String getQueryUrl(){
        return this.queryService.getUrl(this.config.getDeliveryPaths().getTypesPath(), this.parameters, this.queryConfig);
    }

    public MultipleTypeQuery limitParameter(int limit){
        this.parameters.add(new Parameters.LimitParameter(limit));
        return this;
    }

    public MultipleTypeQuery skipParameter(int skip){
        this.parameters.add(new Parameters.SkipParameter(skip));
        return this;
    }

    // observable
    public Observable<DeliveryTypeListingResponse> getObservable() {
        return this.queryService.<JSONObject>getObservable(this.getQueryUrl(), this.queryConfig, this.getHeaders())
                .map(new Function<JSONObject, DeliveryTypeListingResponse>() {
                    @Override
                    public DeliveryTypeListingResponse apply(JSONObject jsonObject) {
                        try {
                            return responseMapService.mapDeliveryMultipleTypesResponse(jsonObject);
                        } catch (IOException ex) {
                            throw new KontentException("Could not get types response with error: " + ex.getMessage(), ex);
                        }
                    }
                });
    }

    @Override
    public DeliveryTypeListingResponse get() {
        try {
            return responseMapService.mapDeliveryMultipleTypesResponse(this.queryService.getJson(this.getQueryUrl(), this.queryConfig, this.getHeaders()));
        } catch (IOException ex) {
            throw new KontentException("Could not get types response with error: " + ex.getMessage(), ex);
        }
    }
}
