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
import com.github.kentico.kontent_delivery_core.models.item.ContentItem;
import com.github.kentico.kontent_delivery_core.models.item.DeliveryItemResponse;
import com.github.kentico.kontent_delivery_core.models.item.ElementMapping;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;
import com.github.kentico.kontent_delivery_core.services.IDeliveryService;
import com.github.kentico.kontent_delivery_rx.DeliveryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ListContentItem extends BaseOfficialTest {

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

    @Override
    public void Example() {

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
                .withTypeResolvers(typeResolvers));

        // Use simple request to get data
        Article article = deliveryService.<Article>item("on_roasts")
                .get()
                .getItem();

        // This is not part of example
        assertThat(article, instanceOf(Article.class));

        // Use RxJava2 to get the data
        deliveryService.<Article>item("on_roasts")
                .elementsParameter(Arrays.asList("title", "summary", "post_date", "teaser_image"))
                .depthParameter(1)
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
