/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.sample.javaapp;

import com.kentico.delivery.core.config.DeliveryClientConfig;
import com.kentico.delivery.core.models.element.DeliveryContentTypeElementResponse;
import com.kentico.delivery.core.models.item.DeliveryItemListingResponse;
import com.kentico.delivery.core.models.taxonomy.DeliveryTaxonomyListingResponse;
import com.kentico.delivery.core.models.taxonomy.DeliveryTaxonomyResponse;
import com.kentico.delivery.core.models.type.DeliveryTypeListingResponse;
import com.kentico.delivery.core.models.type.DeliveryTypeResponse;
import com.kentico.delivery.core.services.IDeliveryService;
import com.kentico.delivery.java.DeliveryService;
import com.kentico.delivery.sample.javaapp.models.Article;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class Demo {

    private final IDeliveryService deliveryService;

    public Demo(){
        this.deliveryService = this.getDeliveryService();
    }

    public void runTests(){

        this.deliveryService.<Article>items().type(Article.TYPE).getObservable().subscribe(new Observer<DeliveryItemListingResponse<Article>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(DeliveryItemListingResponse<Article> iContentItemDeliveryItemListingResponse) {
                List<Article> articles = iContentItemDeliveryItemListingResponse.getItems();
                System.out.println("Items: " + articles.get(0).getSystem().getCodename());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        this.deliveryService.types().getObservable().subscribe(new Observer<DeliveryTypeListingResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DeliveryTypeListingResponse deliveryTypeListingResponse) {
                System.out.println("Types: " + deliveryTypeListingResponse.getTypes().get(0).getSystem().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        this.deliveryService.type("about_us").getObservable().subscribe(new Observer<DeliveryTypeResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DeliveryTypeResponse deliveryTypeResponse) {
                System.out.println("Type: " + deliveryTypeResponse.getType().getSystem().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        this.deliveryService.taxonomies().getObservable().subscribe(new Observer<DeliveryTaxonomyListingResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DeliveryTaxonomyListingResponse deliveryTaxonomyListingResponse) {
                System.out.println("Taxonomies: " + deliveryTaxonomyListingResponse.getTaxonomies().get(0).getSystem().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        this.deliveryService.taxonomy("personas").getObservable().subscribe(new Observer<DeliveryTaxonomyResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DeliveryTaxonomyResponse deliveryTaxonomyResponse) {
                System.out.println("Taxonomy: " + deliveryTaxonomyResponse.getTaxonomy().getSystem().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        this.deliveryService.contenTypeElement(Article.TYPE, "title").getObservable().subscribe(new Observer<DeliveryContentTypeElementResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DeliveryContentTypeElementResponse deliveryContentTypeElementResponse) {
                System.out.println("Element: " + deliveryContentTypeElementResponse.getElement().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        System.out.println(this.deliveryService.taxonomies().get().getTaxonomies().get(0).getSystem().getName());
    }


    private IDeliveryService getDeliveryService(){
        return DeliveryService.getInstance(new DeliveryClientConfig(AppConfig.KENTICO_CLOUD_PROJECT_ID, AppConfig.getTypeResolvers()));
    }

}
