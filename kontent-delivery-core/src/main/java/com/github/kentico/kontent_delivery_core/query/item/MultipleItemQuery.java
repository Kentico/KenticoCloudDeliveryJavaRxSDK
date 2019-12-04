/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.query.item;

import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.adapters.IHttpAdapter;
import com.github.kentico.kontent_delivery_core.adapters.IRxAdapter;
import com.github.kentico.kontent_delivery_core.interfaces.item.IContentItem;
import com.github.kentico.kontent_delivery_core.models.common.Filters;
import com.github.kentico.kontent_delivery_core.models.common.OrderType;
import com.github.kentico.kontent_delivery_core.models.common.Parameters;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;
import com.github.kentico.kontent_delivery_core.models.item.DeliveryItemListingResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public final class MultipleItemQuery<TItem extends IContentItem> extends BaseItemQuery<MultipleItemQuery<TItem>>{

    public MultipleItemQuery(IDeliveryConfig config, IRxAdapter requestService, IHttpAdapter httpAdapter) {
        super(config, requestService, httpAdapter);
    }

    @Override
    public String getQueryUrl(){
        return this.queryService.getUrl(this.config.getDeliveryPaths().getItemsPath(), this.parameters, this.queryConfig);
    }

    // type
    public MultipleItemQuery<TItem> type(String type){
        this.parameters.add(new Filters.EqualsFilter("system.type", type));
        return this;
    }

    // filters
    public MultipleItemQuery<TItem> equalsFilter(String field, String value){
        this.parameters.add(new Filters.EqualsFilter(field, value));
        return this;
    }

    public MultipleItemQuery<TItem> allFilter(String field,  List<String> values){
        this.parameters.add(new Filters.AllFilter(field, values));
        return this;
    }

    public MultipleItemQuery<TItem> rangeFilter(String field, int lowerValue, int higherValue){
        this.parameters.add(new Filters.RangeFilter(field, lowerValue, higherValue));
        return this;
    }

    public MultipleItemQuery<TItem> anyFilter(String field, List<String> values){
        this.parameters.add(new Filters.AnyFilter(field, values));
        return this;
    }

    public MultipleItemQuery<TItem> containsFilter(String field, List<String> values){
        this.parameters.add(new Filters.ContainsFilter(field, values));
        return this;
    }

    public MultipleItemQuery<TItem> inFilter(String field, List<String> values){
        this.parameters.add(new Filters.Infilter(field, values));
        return this;
    }

    public MultipleItemQuery<TItem> greaterThanFilter(String field, String value){
        this.parameters.add(new Filters.GreaterThanFilter(field, value));
        return this;
    }

    public MultipleItemQuery<TItem> greaterThanOrEqualFilter(String field, String value){
        this.parameters.add(new Filters.GreaterThanOrEqualFilter(field, value));
        return this;
    }

    public MultipleItemQuery<TItem> lessThanFilter(String field, String value){
        this.parameters.add(new Filters.LessThanFilter(field, value));
        return this;
    }

    public MultipleItemQuery<TItem> lessThanOrEqualFilter(String field, String value){
        this.parameters.add(new Filters.LessThanOrEqualFilter(field, value));
        return this;
    }

    // parameters
    public MultipleItemQuery<TItem> elementsParameter(List<String> elements){
        this.parameters.add(new Parameters.ElementsParameter(elements));
        return this;
    }

    public MultipleItemQuery<TItem> languageParameter(String language){
        this.parameters.add(new Parameters.LanguageParameter(language));
        return this;
    }

    public MultipleItemQuery<TItem> depthParameter(int limit){
        this.parameters.add(new Parameters.DepthParameter(limit));
        return this;
    }

    public MultipleItemQuery<TItem> limitParameter(int limit){
        this.parameters.add(new Parameters.LimitParameter(limit));
        return this;
    }

    public MultipleItemQuery<TItem> skipParameter(int skip){
        this.parameters.add(new Parameters.SkipParameter(skip));
        return this;
    }

    public MultipleItemQuery<TItem> orderParameter(String field,  OrderType type){
        this.parameters.add(new Parameters.OrderParameter(field, type));
        return this;
    }

    public Observable<DeliveryItemListingResponse<TItem>> getObservable(){
        return this.queryService.<JSONObject>getObservable(this.getQueryUrl(), this.queryConfig, this.getHeaders())
                .map(new Function<JSONObject, DeliveryItemListingResponse<TItem>>() {
                    @Override
                    public DeliveryItemListingResponse<TItem> apply(JSONObject jsonObject) {
                        try {
                            return responseMapService.<TItem>mapItemListingResponse(jsonObject);
                        } catch (JSONException | IOException | IllegalAccessException ex) {
                            throw new KontentException("Could not get multiple items response with error: " + ex.getMessage(), ex);
                        }
                    }
                });
    }

    @Override
    public DeliveryItemListingResponse<TItem> get() {
        try {
            return responseMapService.<TItem>mapItemListingResponse(this.queryService.getJson(this.getQueryUrl(), this.queryConfig, this.getHeaders()));
        } catch (JSONException | IOException | IllegalAccessException ex) {
            throw new KontentException("Could not get multiple items response with error: " + ex.getMessage(), ex);
        }
    }
}
