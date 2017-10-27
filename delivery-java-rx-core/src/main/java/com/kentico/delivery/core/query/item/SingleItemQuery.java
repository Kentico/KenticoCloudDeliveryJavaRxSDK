/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.core.query.item;

import com.kentico.delivery.core.config.DeliveryClientConfig;
import com.kentico.delivery.core.interfaces.item.item.IContentItem;
import com.kentico.delivery.core.models.common.Parameters;
import com.kentico.delivery.core.models.exceptions.KenticoCloudException;
import com.kentico.delivery.core.models.item.DeliveryItemResponse;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import com.kentico.delivery.core.request.IRequestService;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SingleItemQuery<TItem extends IContentItem> extends BaseItemQuery<TItem> {

    private static final String URL_PATH = "/items/";
    private final String itemCodename;

    public SingleItemQuery(DeliveryClientConfig config, IRequestService requestService, String itemCodename) {
        super(config, requestService);
        this.itemCodename = itemCodename;
    }

    @Override
    public String getQueryUrl(){
        String action = URL_PATH + this.itemCodename;

        return this.queryService.getUrl(action, parameters);
    }

    // parameters
    public SingleItemQuery<TItem> elementsParameter( List<String> elements){
        this.parameters.add(new Parameters.ElementsParameter(elements));
        return this;
    }

    public SingleItemQuery<TItem> languageParameter( String language){
        this.parameters.add(new Parameters.LanguageParameter(language));
        return this;
    }

    public SingleItemQuery<TItem> depthParameter(int limit){
        this.parameters.add(new Parameters.DepthParameter(limit));
        return this;
    }

    // observable
    public Observable<DeliveryItemResponse<TItem>> get() {
        return this.queryService.<JSONObject>getRequest(this.getQueryUrl())
                .map(new Function<JSONObject, DeliveryItemResponse<TItem>>() {
                    @Override
                    public DeliveryItemResponse<TItem> apply(JSONObject jsonObject) throws KenticoCloudException {
                        try {
                            return responseMapService.<TItem>mapItemResponse(jsonObject);
                        } catch (JSONException | IOException | IllegalAccessException ex) {
                            //Log.e(SDKConfig.APP_TAG, ex.getMessage());
                            throw new KenticoCloudException("Could not get item response with error: " + ex.getMessage(), ex);
                        }
                    }
                });
    }
}
