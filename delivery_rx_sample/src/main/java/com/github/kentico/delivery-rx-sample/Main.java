/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.delivery_rx_sample;

import com.github.kentico.kontent_delivery_core.config.DeliveryConfig;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;
import com.github.kentico.kontent_delivery_rx.DeliveryService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class Main {
    public static void main( final String[] args ){
        try {

        Flowable.just("Sample rxjava2 app start")
                .subscribe(new Consumer<String>() {
                    @Override public void accept(String s) {
                        System.out.println(s);
                    }
                });

        // Implicit models test
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();
        DeliveryConfig config = DeliveryConfig.newConfig(AppConfig.KONTENT_PROJECT_ID)
            .withTypeResolvers(typeResolvers);

        // disable throwing Exceptions for unknown types
        config.withThrowExceptionForUnknownTypes(false);

        DeliveryService service = new DeliveryService(config);

        System.out.println("Implicit models test:" + service.items().get().getItems().get(0).getSystem().getCodename());

        Demo demo = new Demo();
        demo.runTests();

        } catch (Throwable t) {
            System.out.println(t);
        }
    }
}


