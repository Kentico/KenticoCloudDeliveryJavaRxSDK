/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_tests.official;

import com.github.kentico.kontent_delivery_core.config.DeliveryConfig;
import com.github.kentico.kontent_delivery_core.models.type.ContentType;
import com.github.kentico.kontent_delivery_core.models.type.DeliveryTypeResponse;
import com.github.kentico.kontent_delivery_core.services.IDeliveryService;
import com.github.kentico.kontent_delivery_rx.DeliveryService;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ViewContentType extends BaseOfficialTest{

    @Override
    public void Example() {

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("e391c776-9d1e-4e1a-8a5a-1c327c2586b6"));

        // Use simple request to get data
        ContentType type = deliveryService.type("coffee")
                .get()
                .getType();

        // Test, not part of example
        assertThat(type, instanceOf(ContentType.class));

        // Use RxJava2 to get the data
        deliveryService.type("coffee")
                .getObservable()
                .subscribe(new Observer<DeliveryTypeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeliveryTypeResponse response) {

                        // Get type from response
                        ContentType type = response.getType();

                        // Print codename of content type
                        System.out.println(type.getSystem().getCodename());

                        // This is NOT part of the example
                        assertThat(type, instanceOf(ContentType.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // We should not get an error in official example
                        assertEquals(true, false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}


