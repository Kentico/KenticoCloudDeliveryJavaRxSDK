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
import com.github.kentico.kontent_delivery_core.elements.AssetsElement;
import com.github.kentico.kontent_delivery_core.elements.DateTimeElement;
import com.github.kentico.kontent_delivery_core.elements.TextElement;
import com.github.kentico.kontent_delivery_core.interfaces.item.IContentItem;
import com.github.kentico.kontent_delivery_core.models.common.OrderType;
import com.github.kentico.kontent_delivery_core.models.common.QueryConfig;
import com.github.kentico.kontent_delivery_core.models.item.ContentItem;
import com.github.kentico.kontent_delivery_core.models.item.DeliveryItemListingResponse;
import com.github.kentico.kontent_delivery_core.models.item.DeliveryItemResponse;
import com.github.kentico.kontent_delivery_core.models.item.ElementMapping;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;
import com.github.kentico.kontent_delivery_core.services.IDeliveryService;
import com.github.kentico.kontent_delivery_rx.DeliveryService;

import java.util.*;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Created by JanL on 19.02.2018.
 */

public class Tutorials {

    private static final Date baseDate = new Date(1537443615891L);

    // Prepare strongly typed model
    public final class Article extends ContentItem {

        public static final String TYPE = "article";

        @ElementMapping("summary")
        public TextElement summary;

        @ElementMapping("title")
        public TextElement title;

        @ElementMapping("teaser_image")
        public AssetsElement teaserImage;

        @ElementMapping("post_date")
        public DateTimeElement postDate;
    }

    public final class ScheduledArticle extends ContentItem {

        public static final String TYPE = "article";

        @ElementMapping("summary")
        public TextElement summary;

        @ElementMapping("title")
        public TextElement title;

        @ElementMapping("teaser_image")
        public AssetsElement teaserImage;

        @ElementMapping("post_date")
        public DateTimeElement postDate;

        @ElementMapping("publish_from")
        public DateTimeElement publishFrom;

        @ElementMapping("publish_until")
        public DateTimeElement publishUntil;

        public Date getPublishFrom() {
            return publishFrom.getValue();
        }

        public Date getPublishUntil() {
            return publishUntil.getValue();
        }
    }

    @Test
    public void testGetProjectId() {

        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers)
                .withThrowExceptionForUnknownTypes(false));

        // Use simple request to get data
        List<ContentItem> items = deliveryService.<ContentItem>items()
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(items.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<ContentItem>items()
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<ContentItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<ContentItem> response) {

                        // Get your content items
                        List<ContentItem> items = response.getItems();

                        // Get first item
                        ContentItem firstItem = items.get(0);

                        // Print the first item
                        System.out.println(firstItem.getSystem().getName());

                        // Test, not part of the example
                        assertThat(firstItem, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testGetOnlyArticles() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(Article.TYPE, new Function<Void, Article>() {
            @Override
            public Article apply(Void input) {
                return new Article();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers));

        // Use simple request to get data
        List<Article> articles = deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(articles.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<Article> response) {

                        // Get your mapped articles
                        List<Article> articles = response.getItems();

                        // Get first article
                        Article firstArticle = articles.get(0);

                        // Print the Title of first article
                        System.out.println(firstArticle.title.getValue());

                        // Test, not part of the example
                        assertThat(firstArticle, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testOrderArticlesByPublishDate() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(Article.TYPE, new Function<Void, Article>() {
            @Override
            public Article apply(Void input) {
                return new Article();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers));

        // Use simple request to get data
        List<Article> articles = deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .orderParameter("elements.post_date", OrderType.Desc)
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(articles.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .orderParameter("elements.post_date", OrderType.Desc)
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<Article> response) {

                        // Get your mapped articles
                        List<Article> articles = response.getItems();

                        // Get first article
                        Article firstArticle = articles.get(0);

                        // Print the Title of first article
                        System.out.println(firstArticle.title.getValue());

                        // Test, not part of the example
                        assertThat(firstArticle, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testGetOnlyFiveLatestArticles() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(Article.TYPE, new Function<Void, Article>() {
            @Override
            public Article apply(Void input) {
                return new Article();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers));

        // Use simple request to get data
        List<Article> articles = deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .orderParameter("elements.post_date", OrderType.Desc)
                .limitParameter(5)
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(articles.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .orderParameter("elements.post_date", OrderType.Desc)
                .limitParameter(5)
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<Article> response) {

                        // Get your mapped articles
                        List<Article> articles = response.getItems();

                        // Get first article
                        Article firstArticle = articles.get(0);

                        // Print the Title of first article
                        System.out.println(firstArticle.title.getValue());

                        // Test, not part of the example
                        assertThat(firstArticle, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    // Currently not passing due to https://github.com/Kentico/kontent-java-rx-sdk/issues/32
    @Test
    public void testGetOnlyTitlesAndImages() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(Article.TYPE, new Function<Void, Article>() {
            @Override
            public Article apply(Void input) {
                return new Article();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers));

        // Use simple request to get data
        List<Article> articles = deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .elementsParameter(Arrays.asList("title", "teaser_image"))
                .orderParameter("elements.post_date", OrderType.Desc)
                .limitParameter(5)
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(articles.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<Article>items()
                .equalsFilter("system.type", "article")
                .elementsParameter(Arrays.asList("title", "teaser_image"))
                .orderParameter("elements.post_date", OrderType.Desc)
                .limitParameter(5)
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<Article> response) {

                        // Get your mapped articles
                        List<Article> articles = response.getItems();

                        // Get first article
                        Article firstArticle = articles.get(0);

                        // Print the Title of first article
                        System.out.println(firstArticle.title.getValue());

                        // Test, not part of the example
                        assertThat(firstArticle, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testGetArticlesTaggedWithSpecificTaxonomyTerm() {

        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers)
                .withThrowExceptionForUnknownTypes(false));

        // Use simple request to get data
        List<ContentItem> items = deliveryService.<ContentItem>items()
                .containsFilter("elements.personas", Arrays.asList("coffee_lover"))
                .depthParameter(2)
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(items.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<ContentItem>items()
                .containsFilter("elements.personas", Arrays.asList("coffee_lover"))
                .depthParameter(2)
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<ContentItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<ContentItem> response) {

                        // Get your content items
                        List<ContentItem> items = response.getItems();

                        // Get first item
                        ContentItem firstItem = items.get(0);

                        // Print the first item
                        System.out.println(firstItem.getSystem().getName());

                        // Test, not part of the example
                        assertThat(firstItem, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testPreviewContent() {

        String projectId = "975bf280-fd91-488c-994c-2f04416e5ee3";
        String previewApiKey = "ew0KICAiYWxnIjogIkhTMjU2IiwNCiAgInR5cCI6ICJKV1QiDQp9.ew0KICAidWlkIjogInVzcl8wdkltdkYxd05lbWhTNmc3QTVIUkxaIiwNCiAgImVtYWlsIjogImpvaG5kb2VAcmFpbmIub3ciLA0KICAiZ2l2ZW5fbmFtZSI6ICJKb2huIiwNCiAgImZhbWlseV9uYW1lIjogIkRvZSIsDQogICJwcm9qZWN0X2lkIjogIjk3NWJmMjgwLWZkOTEtNDg4Yy05OTRjLTJmMDQ0MTZlNWVlMyIsDQogICJqdGkiOiAiRDZZOWJCMjJGNUg2RHdDYiIsDQogICJ2ZXIiOiAiMS4wLjAiLA0KICAiYXVkIjogInByZXZpZXcuZGVsaXZlci5rZW50aWNvY2xvdWQuY29tIg0KfQ.smxwBubVceUVfiI6xL7HHG56Ie4oUt_ioQvV8puicXQ";

        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig(projectId)
                .withTypeResolvers(typeResolvers)
                .withPreviewApiKey(previewApiKey)
                .withThrowExceptionForUnknownTypes(false));

        // Use simple request to get data
        ContentItem item = deliveryService.<ContentItem>item("which_brewing_fits_you_")
                .depthParameter(2)
                .get()
                .getItem();

        // Test, not part of the example
        assertThat(item, instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<ContentItem>item("which_brewing_fits_you_")
                .depthParameter(2)
                .getObservable()
                .subscribe(new Observer<DeliveryItemResponse<ContentItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemResponse<ContentItem> response) {

                        // Get your content items
                        ContentItem item = response.getItem();

                        // Print the first item
                        System.out.println(item.getSystem().getName());

                        // Test, not part of the example
                        assertThat(item, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testRetrieveContentInSpecificLanguage() {

        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers)
                .withThrowExceptionForUnknownTypes(false));

        // Use simple request to get data
        List<ContentItem> items = deliveryService.<ContentItem>items()
                .languageParameter("es-ES")
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(items.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<ContentItem>items()
                .languageParameter("es-ES")
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<ContentItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<ContentItem> response) {

                        // Get your content items
                        List<ContentItem> items = response.getItems();

                        // Get first item
                        ContentItem firstItem = items.get(0);

                        // Print the first item
                        System.out.println(firstItem.getSystem().getName());

                        // Test, not part of the example
                        assertThat(firstItem, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testLanguageFallback() {

        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("975bf280-fd91-488c-994c-2f04416e5ee3")
                .withTypeResolvers(typeResolvers)
                .withThrowExceptionForUnknownTypes(false));

        // Use simple request to get data
        ContentItem item = deliveryService.<ContentItem>item("on_roasts")
                .depthParameter(2)
                .get()
                .getItem();

        // Test, not part of the example
        assertThat(item, instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<ContentItem>item("on_roasts")
                .depthParameter(2)
                .getObservable()
                .subscribe(new Observer<DeliveryItemResponse<ContentItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemResponse<ContentItem> response) {

                        // Get your content items
                        ContentItem item = response.getItem();

                        // Print the first item
                        System.out.println(item.getSystem().getName());

                        // Test, not part of the example
                        assertThat(item, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testSchedulingContentPublishingInmemory() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(ScheduledArticle.TYPE, new Function<Void, ScheduledArticle>() {
            @Override
            public ScheduledArticle apply(Void input) {
                return new ScheduledArticle();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("03c8ff0b-fa7b-0087-8a78-234ee0e31d22")
                .withTypeResolvers(typeResolvers));

        // Use simple request to get data
        List<ScheduledArticle> articles = deliveryService.<ScheduledArticle>items()
                .equalsFilter("system.type", "article")
                .get()
                .getItems();

        List<ScheduledArticle> publishedItems = new ArrayList<>();

        for (ScheduledArticle article : articles) {
            if ((article.getPublishFrom() == null || article.getPublishFrom().before(baseDate)) && article.getPublishUntil() == null || article.getPublishUntil().after(baseDate)) {
                publishedItems.add(article);
            }
        }

        // Test, not part of the example
        assertThat(articles.get(0), instanceOf(ScheduledArticle.class));

        // Use RxJava2 to get the data
        deliveryService.<ScheduledArticle>items()
                .equalsFilter("system.type", "article")
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<ScheduledArticle>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<ScheduledArticle> response) {

                        // Get your mapped articles
                        List<ScheduledArticle> articles = response.getItems();

                        List<ScheduledArticle> publishedItems = new ArrayList<>();

                        for (ScheduledArticle article : articles) {
                            if ((article.getPublishFrom() == null || article.getPublishFrom().before(baseDate)) && article.getPublishUntil() == null || article.getPublishUntil().after(baseDate)) {
                                publishedItems.add(article);
                            }
                        }

                        // Print the Title of first article
                        System.out.println(publishedItems.get(0).title.getValue());

                        // Test, not part of the example
                        assertThat(articles.get(0), instanceOf(ScheduledArticle.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testSchedulingContentPublishingViaFilter() {

        String date = baseDate.toInstant().toString();

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(ScheduledArticle.TYPE, new Function<Void, ScheduledArticle>() {
            @Override
            public ScheduledArticle apply(Void input) {
                return new ScheduledArticle();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("03c8ff0b-fa7b-0087-8a78-234ee0e31d22").withTypeResolvers(typeResolvers));

        // Use simple request to get data
        List<ScheduledArticle> articles = deliveryService.<ScheduledArticle>items()
                .equalsFilter("system.type", "article")
                .lessThanOrEqualFilter("elements.publish_from", date)
                .greaterThanFilter("elements.publish_until", date)
                .get()
                .getItems();

        // Test, not part of the example
        assertThat(articles.get(0), instanceOf(ContentItem.class));

        // Use RxJava2 to get the data
        deliveryService.<ScheduledArticle>items()
                .equalsFilter("system.type", "article")
                .lessThanOrEqualFilter("elements.publish_from", date)
                .greaterThanFilter("elements.publish_until", date)
                .getObservable()
                .subscribe(new Observer<DeliveryItemListingResponse<ScheduledArticle>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<ScheduledArticle> response) {

                        // Get your mapped articles
                        List<ScheduledArticle> articles = response.getItems();

                        // Get first article
                        ScheduledArticle firstArticle = articles.get(0);

                        // Print the Title of first article
                        System.out.println(firstArticle.title.getValue());

                        // Test, not part of the example
                        assertThat(firstArticle, instanceOf(ContentItem.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Print the error message
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Test
    public void testReactToNotifications() {

        // Prepare array to hold all your type resolvers
        List<TypeResolver<?>> typeResolvers = new ArrayList<>();

        // Register type resolver
        typeResolvers.add(new TypeResolver<>(Article.TYPE, new Function<Void, Article>() {
            @Override
            public Article apply(Void input) {
                return new Article();
            }
        }));

        // Initialize DeliveryService for Java projects
        IDeliveryService deliveryService = new DeliveryService(DeliveryConfig.newConfig("e391c776-9d1e-4e1a-8a5a-1c327c2586b6")
                .withTypeResolvers(typeResolvers)
                .withDefaultQueryConfig(new QueryConfig(true, false)));

        // Use simple request to get data
        Article article = deliveryService.<Article>item("on_roasts")
                .get()
                .getItem();

        // This is not part of example
        assertThat(article, instanceOf(Article.class));

        // Use RxJava2 to get the data
        deliveryService.<Article>item("on_roasts")
                .getObservable()
                .subscribe(new Observer<DeliveryItemResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryItemResponse<Article> response) {

                        // Get article
                        Article article = response.getItem();

                        // Print the Title of first article
                        System.out.println(article.title.getValue());

                        // This is NOT part of the example, but we need to test that
                        // we got some item
                        assertThat(article, instanceOf(Article.class));
                        assertThat(article, instanceOf(IContentItem.class));
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
