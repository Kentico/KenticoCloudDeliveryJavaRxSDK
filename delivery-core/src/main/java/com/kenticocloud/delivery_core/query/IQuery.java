/*
 * Copyright 2018 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kenticocloud.delivery_core.query;

import com.kenticocloud.delivery_core.interfaces.item.common.IDeliveryResponse;
import com.kenticocloud.delivery_core.interfaces.item.common.IQueryConfig;
import com.kenticocloud.delivery_core.interfaces.item.common.IQueryParameter;
import com.kenticocloud.delivery_core.models.common.Header;

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
     * Indicates if loading for new content header is set
     * @param wait True or false
     * @return Query
     */
     IQuery setWaitForLoadingNewContent(boolean wait);

     /**
     * Indicates if preview mode for this query is used
     * @param enablePreview Enabled or disabled
     * @return Query
     */
     IQuery setUsePreviewMode(boolean enablePreview);

     /**
     * Gets query configuration
     * @return Query configuration
     */
     IQueryConfig getQueryConfig();

     /**
     * Sets query configuration
     * @param queryConfig Query configuration
     */
     void setQueryConfig(IQueryConfig queryConfig);

     /**
     * Gets url of query action
     * @return Url used to hit Kentico Cloud
     */
     String getQueryUrl();

     /**
     * Gets observable to fetch IDeliveryResposne from Kentico Cloud
     * @return Observable to get Delivery response
     */
     Observable<? extends IDeliveryResponse> getObservable();

     /**
     * Gets delivery response
     * @return Delivery response
     */
     IDeliveryResponse get();
}
