/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_tests.core;

import com.github.kentico.kontent_delivery_core.config.DeliveryConfig;
import com.github.kentico.kontent_delivery_core.config.IDeliveryConfig;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;
import com.github.kentico.kontent_delivery_core.services.IDeliveryService;
import com.github.kentico.kontent_delivery_rx.DeliveryService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public abstract class BaseIsolatedTest {

    protected IDeliveryService deliveryService;
    protected IDeliveryConfig config;

    protected BaseIsolatedTest(IDeliveryConfig config) {
        this.config = config;
        this.deliveryService = new DeliveryService(config);
    }

    protected BaseIsolatedTest(){
        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(SharedContentModels.SharedArticle.TYPE, new Function<Void, SharedContentModels.SharedArticle>() {
            @Override
            public SharedContentModels.SharedArticle apply(Void input) {
                return new SharedContentModels.SharedArticle();
            }
        }));

        // initialize with fake project Id as we don't want to execute any calls to Kontent API anyway
        this.config = DeliveryConfig.newConfig("fakeProject")
                .withTypeResolvers(typeResolvers);

        this.deliveryService = new DeliveryService(this.config);
    }
}
