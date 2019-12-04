/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.config;

import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryConfig;
import com.github.kentico.kontent_delivery_core.models.common.QueryConfig;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;

import java.util.List;

public final class DeliveryConfig implements IDeliveryConfig{

    private final DeliveryPaths deliveryPaths = new DeliveryPaths();
    private final DeliveryProperties deliveryProperties = new DeliveryProperties();

    private String deliveryApiUrl = "https://deliver.kontent.ai";
    private String deliveryPreviewApiUrl = "https://preview-deliver.kontent.ai";

    private final String projectId;
    private List<TypeResolver<?>> typeResolvers;
    private String previewApiKey;
    private String securedApiKey;
    private boolean throwExceptionForUnknownTypes = true;
    private IQueryConfig defaultQueryConfig = new QueryConfig();

    /**
     * Creates configuration builder object
     * @param projectId Id of Kontent project
     */
    public DeliveryConfig(String projectId){
        this.projectId = projectId;
    }

    /**
     * Creates configuration builder object
     * @param projectId Id of Kontent project
     */
    public static DeliveryConfig newConfig(String projectId){
        return new DeliveryConfig(projectId);
    }

    /**
     * Sets type resolvers responsible for mapping response to strongly typed object
     * @param resolvers Collection of resolvers
     */
    public DeliveryConfig withTypeResolvers(List<TypeResolver<?>> resolvers){
        this.typeResolvers = resolvers;
        return this;
    }


    /**
     * Sets preview API key
     * @param previewApiKey API key
     */
    public DeliveryConfig withPreviewApiKey(String previewApiKey){
        this.previewApiKey = previewApiKey;
        return this;
    }


    /**
     * Sets secured API key
     * @param securedApiKey API key
     */
    public DeliveryConfig withSecuredApiKey(String securedApiKey){
        this.securedApiKey = securedApiKey;
        return this;
    }

    /**
     * Sets custom URL of Kontent Endpoint
     * @param url URL
     */
    public DeliveryConfig withDeliveryApiUrl(String url){
        this.deliveryApiUrl = url;
        return this;
    }

    /**
     * Sets custom URL of Kontent preview Endpoint
     * @param url URL
     */
    public DeliveryConfig withDeliveryPreviewApiUrl(String url){
        this.deliveryPreviewApiUrl = url;
        return this;
    }

    /**
     * If enabled, SDK will throw Exception if it cannot find strongly typed model (type resolver)
     * for certain item in response
     * @param throwException Indicates if Exception should be thrown
     */
    public DeliveryConfig withThrowExceptionForUnknownTypes(boolean throwException){
        this.throwExceptionForUnknownTypes = throwException;
        return this;
    }

    /**
     * Sets default query config for all queries made within SDK. This is useful when you want to set
     * default behavior and then override it on per query level.
     * @param queryConfig  Query configuration
     */
    public DeliveryConfig withDefaultQueryConfig(IQueryConfig queryConfig){
        this.defaultQueryConfig = queryConfig;
        return this;
    }

    @Override
    public String getProjectId(){
        return this.projectId;
    }

    @Override
    public List<TypeResolver<?>> getTypeResolvers() { return this.typeResolvers; }

    @Override
    public DeliveryPaths getDeliveryPaths(){
        return this.deliveryPaths;
    }

    @Override
    public String getDeliveryApiUrl(){
        return this.deliveryApiUrl;
    }

    @Override
    public String getDeliveryPreviewApiUrl() {
        return this.deliveryPreviewApiUrl;
    }

    @Override
    public DeliveryProperties getDeliveryProperties() {
        return this.deliveryProperties;
    }

    @Override
    public IQueryConfig getDefaultQueryConfig() {
        return this.defaultQueryConfig;
    }

    @Override
    public String getPreviewApiKey() {
        return this.previewApiKey;
    }

    @Override
    public String getSecuredApiKey() {
        return this.securedApiKey;
    }

    @Override
    public boolean getThrowExceptionForUnknownTypes() {
        return this.throwExceptionForUnknownTypes;
    }
}
