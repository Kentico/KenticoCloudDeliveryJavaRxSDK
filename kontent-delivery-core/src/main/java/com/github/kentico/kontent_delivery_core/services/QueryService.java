/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.services;

import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryConfig;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryParameter;
import com.github.kentico.kontent_delivery_core.utils.ErrorHelper;
import com.github.kentico.kontent_delivery_core.adapters.IHttpAdapter;
import com.github.kentico.kontent_delivery_core.adapters.IRxAdapter;
import com.github.kentico.kontent_delivery_core.models.common.CommonKontentResponses;
import com.github.kentico.kontent_delivery_core.models.common.Header;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentResponseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public final class QueryService implements IQueryService{

    private IDeliveryConfig config;
    private IRxAdapter rxAdapter;
    private IHttpAdapter httpAdapter;

    public QueryService(IDeliveryConfig config, IRxAdapter rxAdapter, IHttpAdapter httpAdapter){
        this.config = config;
        this.rxAdapter = rxAdapter;
        this.httpAdapter = httpAdapter;
    }

    private String getDeliveryOrPreviewUrl(IQueryConfig queryConfig){
        return queryConfig.getUsePreviewMode() ? this.config.getDeliveryPreviewApiUrl() : this.config.getDeliveryApiUrl();
    }

    private String getBaseUrl(IQueryConfig queryConfig){
        return this.getDeliveryOrPreviewUrl(queryConfig) + '/' + this.config.getProjectId();
    }

    private String addParametersToUrl( String url, List<IQueryParameter> parameters){
        if (parameters == null){
            return url;
        }

        StringBuilder urlBuilder = new StringBuilder(url);
        for(int i = 0; i < parameters.size(); i++) {
            IQueryParameter parameter = parameters.get(i);
            if (i == 0) {
                // first item
                urlBuilder.append("?").append(parameter.getParam()).append("=").append(parameter.getParamValue());
            } else {
                // after first items
                urlBuilder.append("&").append(parameter.getParam()).append("=").append(parameter.getParamValue());
            }
        }
        url = urlBuilder.toString();

        return url;
    }

    @Override
    public String getUrl( String action, List<IQueryParameter> parameters, IQueryConfig queryConfig){
        return addParametersToUrl(getBaseUrl(queryConfig) + action, parameters);
    }

    @Override
    public List<Header> getHeaders(IQueryConfig queryConfig) {
        List<Header> headers = new ArrayList<>();

        if (queryConfig.getUsePreviewMode()){
            String previewApiKey = this.config.getPreviewApiKey();
            if (previewApiKey == null || previewApiKey.isEmpty()){
                throw new KontentException("Preview API key is not defined", null);
            }

            headers.add(new Header(this.config.getDeliveryProperties().getAuthorizationHeader(), this.config.getDeliveryProperties().getAuthorizationHeaderValue(previewApiKey)));
        }

        if (queryConfig.getWaitForLoadingNewContent()){
            headers.add(new Header(this.config.getDeliveryProperties().getWaitForLoadingNewContentHeader(), "true"));
        }

        if (queryConfig.getUsePreviewMode()){
            String securedApiKey = this.config.getSecuredApiKey();
            if (securedApiKey == null || securedApiKey.isEmpty()){
                throw new KontentException("Secured API key is not defined", null);
            }

            headers.add(new Header(this.config.getDeliveryProperties().getAuthorizationHeader(), this.config.getDeliveryProperties().getAuthorizationHeaderValue(securedApiKey)));
        }

        return headers;
    }

    @Override
    public Observable<JSONObject> getObservable(String url, IQueryConfig queryConfig, List<Header> headers) {
        Observable<String> jsonObservable = this.rxAdapter.get(url, queryConfig, headers);

        final QueryService that = this;

        return jsonObservable.map(
                new Function<String, JSONObject>() {
                    @Override
                    public JSONObject apply(String json){
                        try {
                            JSONObject jsonObject = new JSONObject(json);

                            that.handleDeliveryError(jsonObject);

                            return jsonObject;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new KontentException("Could not parse json", e);
                        }
                    }
                }
        );
    }

    @Override
    public JSONObject getJson(String url, IQueryConfig queryConfig, List<Header> headers) {
        String json = this.httpAdapter.get(url, queryConfig, headers);

        try {
            JSONObject jsonObject = new JSONObject(json);

            this.handleDeliveryError(jsonObject);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new KontentException("Could not parse json", e);
        }
    }

    private void handleDeliveryError(JSONObject jsonObject){
        if (jsonObject == null){
            throw new KontentException("Invalid json", null);
        }

        if (this.isDeliveryError(jsonObject)){
            CommonKontentResponses.DeliveryErrorRaw errorRaw = ErrorHelper.getDeliveryError(jsonObject);

            throw new KontentResponseException(errorRaw.message, errorRaw.requestId, errorRaw.errorCode, errorRaw.specificCode);
        }
    }

    private boolean isDeliveryError(JSONObject jsonObject){
        if (jsonObject == null){
            throw new KontentException("Invalid json", null);
        }

        // check if response contains error_code and if it does, treat it as error
        return jsonObject.has("error_code");
    }
}
