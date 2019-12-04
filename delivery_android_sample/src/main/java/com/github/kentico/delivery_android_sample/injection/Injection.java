/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.delivery_android_sample.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.kentico.kontent_delivery_android.DeliveryAndroidService;
import com.github.kentico.kontent_delivery_core.config.DeliveryConfig;
import com.github.kentico.kontent_delivery_core.services.IDeliveryService;
import com.github.kentico.delivery_android_sample.app.config.AppConfig;
import com.github.kentico.delivery_android_sample.data.source.articles.ArticlesKontentSource;
import com.github.kentico.delivery_android_sample.data.source.articles.ArticlesRepository;
import com.github.kentico.delivery_android_sample.data.source.cafes.CafesKontentSource;
import com.github.kentico.delivery_android_sample.data.source.cafes.CafesRepository;
import com.github.kentico.delivery_android_sample.data.source.coffees.CoffeesKontentSource;
import com.github.kentico.delivery_android_sample.data.source.coffees.CoffeesRepository;

/**
 * Enables injection of production implementations at compile time.
 */
public class Injection {

    private static IDeliveryService deliveryService = new DeliveryAndroidService(DeliveryConfig.newConfig(AppConfig.KONTENT_PROJECT_ID)
            .withTypeResolvers(AppConfig.getTypeResolvers()));

    public static ArticlesRepository provideArticlesRepository(@NonNull Context context) {
        return ArticlesRepository.getInstance(ArticlesKontentSource.getInstance(context));
    }

    public static CafesRepository provideCafessRepository(@NonNull Context context) {
        return CafesRepository.getInstance(CafesKontentSource.getInstance(context));
    }

    public static CoffeesRepository provideCoffeesRepository(@NonNull Context context) {
        return CoffeesRepository.getInstance(CoffeesKontentSource.getInstance(context));
    }

    public static IDeliveryService provideDeliveryService() {
        return Injection.deliveryService;
    }
}