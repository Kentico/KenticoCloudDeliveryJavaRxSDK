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

import com.kentico.delivery.core.config.DeliveryConfig;
import com.kentico.delivery.core.interfaces.item.common.IQueryParameter;
import com.kentico.delivery.core.interfaces.item.item.IContentItem;
import com.kentico.delivery.core.models.common.Filters;
import com.kentico.delivery.core.models.common.OrderType;
import com.kentico.delivery.core.models.common.QueryConfig;
import com.kentico.delivery.core.models.element.DeliveryContentTypeElementResponse;
import com.kentico.delivery.core.models.exceptions.KenticoCloudResponseException;
import com.kentico.delivery.core.models.item.DeliveryItemListingResponse;
import com.kentico.delivery.core.models.item.DeliveryItemResponse;
import com.kentico.delivery.core.models.taxonomy.DeliveryTaxonomyListingResponse;
import com.kentico.delivery.core.models.taxonomy.DeliveryTaxonomyResponse;
import com.kentico.delivery.core.models.type.DeliveryTypeListingResponse;
import com.kentico.delivery.core.models.type.DeliveryTypeResponse;
import com.kentico.delivery.core.query.element.SingleContentTypeElementQuery;
import com.kentico.delivery.core.query.item.MultipleItemQuery;
import com.kentico.delivery.core.query.item.SingleItemQuery;
import com.kentico.delivery.core.query.taxonomy.MultipleTaxonomyQuery;
import com.kentico.delivery.core.query.taxonomy.SingleTaxonomyQuery;
import com.kentico.delivery.core.query.type.MultipleTypeQuery;
import com.kentico.delivery.core.query.type.SingleTypeQuery;
import com.kentico.delivery.core.services.IDeliveryService;
import com.kentico.delivery.java.DeliveryService;
import com.kentico.delivery.sample.javaapp.models.Article;
import com.kentico.delivery.sample.javaapp.models.Cafe;

import java.util.ArrayList;
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
            public void onNext(DeliveryItemListingResponse<Article> response) {
                List<Article> articles = response.getItems();
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

        System.out.println(this.deliveryService.items().depthParameter(1).setUsePreviewMode(false).getQueryUrl());

        DeliveryItemListingResponse<Cafe> response2 = this.deliveryService.<Cafe>items().type(Cafe.TYPE).get();

        List<Cafe> cafes = response2.getItems();

        try {
            DeliveryItemListingResponse<IContentItem> response = this.deliveryService.items().get();
            System.out.println(response.getItems().get(0).getElements().get(0).getName());
        }
        catch (KenticoCloudResponseException ex){
            String requestId = ex.getRequestId();
        }
        catch (Exception ex){
            String message = ex.getMessage();
        }

        String url = deliveryService.items()
                .equalsFilter("elements.title", "Warrior")
                .limitParameter(5)
                .depthParameter(2)
                .skipParameter(1)
                .getQueryUrl();
        System.out.println(url);

        MultipleItemQuery<Cafe> query = deliveryService.<Cafe>items()
                .equalsFilter("elements.title", "London")
                .limitParameter(5)
                .depthParameter(2)
                .orderParameter("elements.street", OrderType.Desc);

        MultipleItemQuery<Cafe> query2 = deliveryService.<Cafe>items()
                .addParameter(new Filters.EqualsFilter("elements.title", "London"));

        MultipleItemQuery<Cafe> query3 = deliveryService.<Cafe>items()
                .addParameter(new CustomFilter("id"));

        // items
        SingleItemQuery<Cafe> cafeQuery = deliveryService.<Cafe>item("boston");
        MultipleItemQuery<Cafe> cafesQuery = deliveryService.<Cafe>items();

        Cafe cafe = cafeQuery.get().getItem();
        List<Cafe> cafes3 = cafesQuery.get().getItems();

        cafeQuery.getObservable().subscribe(new Observer<DeliveryItemResponse<Cafe>>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {

                                                }

                                                @Override
                                                public void onNext(DeliveryItemResponse<Cafe> cafeDeliveryItemResponse) {

                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });

                // types
                SingleTypeQuery typeQuery = deliveryService.type("Cafe");
        MultipleTypeQuery typesQuery = deliveryService.types();

        // taxonomies
        SingleTaxonomyQuery taxonomyQuery = deliveryService.taxonomy("personas");
        MultipleTaxonomyQuery taxonomiesQuery = deliveryService.taxonomies();

        // elements
        SingleContentTypeElementQuery elementQuery = deliveryService.contenTypeElement("cafe", "country");
    }


    private IDeliveryService getDeliveryService(){
        return new DeliveryService(new DeliveryConfig(AppConfig.KENTICO_CLOUD_PROJECT_ID, AppConfig.getTypeResolvers(), AppConfig.PREVIEW_API_KEY).setThrowExceptionForUnknownTypes(false));
    }

    public static class CustomFilter implements IQueryParameter {

        private String clientId;

        public CustomFilter(String clientId){
            this.clientId = clientId;
        }

        @Override
        public String getParam() {
            return "clientId";
        }

        @Override
        public String getParamValue() {
            return this.clientId;
        }
    }
}
