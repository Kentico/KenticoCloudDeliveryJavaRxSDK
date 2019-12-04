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

import com.github.kentico.kontent_delivery_core.interfaces.item.common.IDeliveryResponse;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryConfig;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryParameter;
import com.github.kentico.kontent_delivery_core.models.common.Header;

import java.util.List;

import io.reactivex.Observable;

public interface IQuery {

     /**
     * Gets headers for request
     * @return List of headers
     */
     List<Header> getHeaders();

     /**
     * Adds parameter to query
     * @param queryParameter Query parameter
     * @return Query
     */
    IQuery addParameter(IQueryParameter queryParameter);

     /**
     * Gets query configuration
     * @return Query configuration
     */
     IQueryConfig getQueryConfig();

     /**
     * Sets query configuration
     * @param queryConfig Query configuration
     */
     IQuery setQueryConfig(IQueryConfig queryConfig);

     /**
     * Gets url of query action
     * @return Url used to hit Kontent
     */
     String getQueryUrl();

     /**
     * Gets observable to fetch IDeliveryResposne from Kontent
     * @return Observable to get Delivery response
     */
     Observable<? extends IDeliveryResponse> getObservable();

     /**
     * Gets delivery response
     * @return Delivery response
     */
     IDeliveryResponse get();
}
