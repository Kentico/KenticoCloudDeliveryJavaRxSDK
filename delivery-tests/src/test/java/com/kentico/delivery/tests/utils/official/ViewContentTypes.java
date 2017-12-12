/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.tests.utils.official;

import com.kentico.delivery.core.config.DeliveryConfig;
import com.kentico.delivery.core.models.item.TypeResolver;
import com.kentico.delivery.core.models.type.ContentType;
import com.kentico.delivery.core.models.type.DeliveryTypeListingResponse;
import com.kentico.delivery.core.services.IDeliveryService;
import com.kentico.delivery.java.DeliveryService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ViewContentTypes extends BaseOfficialTest{

    @Override
    public void Example() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(new DeliveryConfig("e391c776-9d1e-4e1a-8a5a-1c327c2586b6", typeResolvers));

        // Use simple request to get data
        List<ContentType> types = deliveryService.types()
                .limitParameter(3)
                .get()
                .getTypes();

        // Test, not part of example
        assertThat(types.get(0), instanceOf(ContentType.class));

        // Use RxJava2 to get the data
        deliveryService.types()
                .limitParameter(3)
                .getObservable()
                .subscribe(new Observer<DeliveryTypeListingResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeliveryTypeListingResponse response) {

                        // Get types from response
                        List<ContentType> types = response.getTypes();

                        // Print codename of first content type
                        System.out.println(types.get(0).getSystem().getCodename());

                        // This is NOT part of the example
                        assertThat(types.get(0), instanceOf(ContentType.class));
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


