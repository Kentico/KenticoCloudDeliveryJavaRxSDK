/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_tests.isolated;

import com.github.kentico.kontent_delivery_core.config.DeliveryConfig;
import com.github.kentico.kontent_delivery_core.models.common.QueryConfig;
import com.github.kentico.kontent_delivery_core.models.item.TypeResolver;
import com.github.kentico.kontent_delivery_tests.core.BaseIsolatedTest;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;

public class QueryConfigTest extends BaseIsolatedTest {

    public QueryConfigTest() {
        super(DeliveryConfig.newConfig("fake")
            .withTypeResolvers(new ArrayList<TypeResolver<?>>())
            .withDefaultQueryConfig(new QueryConfig(false, false)));
    }

    @Test
    public void ChangingQueryConfigurationShouldNotAffectGlobalConfig() {

        // should use default url
        String previewUrlA = this.deliveryService.items().getQueryUrl();
        // should use preview url
        String previewUrlB = this.deliveryService.items().setUsePreviewMode(true).getQueryUrl();
        // should use default setting = default url
        String previewUrlC = this.deliveryService.items().getQueryUrl();

        assertThat(previewUrlA, CoreMatchers.containsString(this.config.getDeliveryApiUrl()));
        assertThat(previewUrlB, CoreMatchers.containsString(this.config.getDeliveryPreviewApiUrl()));
        assertThat(previewUrlC, CoreMatchers.containsString(this.config.getDeliveryApiUrl()));
    }
}
