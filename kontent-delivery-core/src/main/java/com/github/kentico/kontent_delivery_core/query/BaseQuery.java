/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.query;

import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.adapters.IHttpAdapter;
import com.github.kentico.kontent_delivery_core.adapters.IRxAdapter;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IDeliveryResponse;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryConfig;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryParameter;
import com.github.kentico.kontent_delivery_core.models.common.Header;
import com.github.kentico.kontent_delivery_core.models.common.QueryConfig;
import com.github.kentico.kontent_delivery_core.services.IQueryService;
import com.github.kentico.kontent_delivery_core.services.QueryService;
import com.github.kentico.kontent_delivery_core.services.ResponseMapService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public abstract class BaseQuery<TQuery extends IQuery> implements IQuery {

    protected List<IQueryParameter> parameters;
    protected IQueryConfig queryConfig;

    protected IDeliveryConfig config;
    protected ResponseMapService responseMapService;
    protected IQueryService queryService;

    protected BaseQuery(IDeliveryConfig config, IRxAdapter rxAdapter, IHttpAdapter httpAdapter){
        this.config = config;
        this.responseMapService = new ResponseMapService(config);
        this.queryService = new QueryService(config, rxAdapter, httpAdapter);

        // reset parameters
        this.parameters = new ArrayList<>();

        // apply default query configuration, this can be overridden by setters
        // do not use 'config.getDefaultQueryConfig()' directly as any further changes would
        // affect all queries = avoid modification of object itself
        IQueryConfig defaultConfig = config.getDefaultQueryConfig();
        this.queryConfig = new QueryConfig(defaultConfig.getWaitForLoadingNewContent(), defaultConfig.getUsePreviewMode());
    }

    /**
     * Gets headers for request
     * @return List of headers
     */
    public List<Header> getHeaders(){
        return this.queryService.getHeaders(this.queryConfig);
    }

    /**
     * Gets query configuration
     * @return Query configuration
     */
    public IQueryConfig getQueryConfig(){
        return this.queryConfig;
    }

    /**
     * Adds parameter to query
     * @param queryParameter Query parameter
     * @return Query
     */
    public TQuery addParameter(IQueryParameter queryParameter){
        this.parameters.add(queryParameter);
        return (TQuery)this;
    }

    /**
     * Indicates if preview mode is used for this query
     * @param usePreview Use preview mode
     */
    public TQuery setUsePreviewMode(boolean usePreview){
        this.getQueryConfig().setUsePreviewMode(usePreview);
        return (TQuery)this;
    }

    /**
     * Indicates if secured mode is used for this query
     * @param useSecuredMode Use preview mode
     */
    public TQuery setUseSecuredMode(boolean useSecuredMode){
        this.getQueryConfig().setUseSecuredMode(useSecuredMode);
        return (TQuery)this;
    }

    /**
     * Sets query configuration
     * @param queryConfig Query configuration
     */
    public TQuery setQueryConfig(IQueryConfig queryConfig){
        this.queryConfig = queryConfig;
        return (TQuery)this;
    }

    /**
     * Gets url of query action
     * @return Url used to hit Kontent
     */
    public abstract String getQueryUrl();

    /**
     * Gets observable to fetch IDeliveryResposne from Kontent
     * @return Observable to get Delivery response
     */
    public abstract Observable<? extends IDeliveryResponse> getObservable();

    /**
     * Gets delivery response
     * @return Delivery response
     */
    public abstract IDeliveryResponse get();
}
