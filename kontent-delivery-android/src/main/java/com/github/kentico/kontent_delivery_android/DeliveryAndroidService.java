/*
 * Copyright 2018 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_android;


import com.github.kentico.kontent_delivery_core.adapters.IHttpAdapter;
import com.github.kentico.kontent_delivery_core.adapters.IRxAdapter;
import com.github.kentico.kontent_delivery_core.config.DeliveryConfig;
import com.github.kentico.kontent_delivery_core.services.BaseDeliveryService;
import com.github.kentico.kontent_delivery_core.services.IDeliveryService;

public class DeliveryAndroidService extends BaseDeliveryService implements IDeliveryService {

    private IRxAdapter rxAdapter;
    private IHttpAdapter httpAdapter;

    /**
     * Initializes delivery service for Android
     * @param config Delivery client configuration
     */
    public DeliveryAndroidService(DeliveryConfig config) {
        super(config);
        this.rxAdapter = new AndroidRxAdapter();
        this.httpAdapter = new AndroidHttpAdapter();
    }

    @Override
    public IRxAdapter getRxAdapter() {
        return rxAdapter;
    }

    @Override
    public IHttpAdapter getHttpAdapter() {
        return httpAdapter;
    }

}
