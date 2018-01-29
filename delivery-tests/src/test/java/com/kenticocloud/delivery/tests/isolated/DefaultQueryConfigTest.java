/*
 * Copyright 2018 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kenticocloud.delivery.tests.isolated;

import com.kenticocloud.delivery_core.config.DeliveryConfig;
import com.kenticocloud.delivery_core.models.common.QueryConfig;
import com.kenticocloud.delivery_core.models.item.TypeResolver;
import com.kenticocloud.delivery.tests.core.BaseIsolatedTest;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DefaultQueryConfigTest extends BaseIsolatedTest {

    private static String projectId = "projectX";

    public DefaultQueryConfigTest() {
        super(new DeliveryConfig(projectId, new ArrayList<TypeResolver<?>>(), new QueryConfig(false, false)));
    }

    @Test
    public void CheckProjectId() {

        assertEquals(this.config.getProjectId(), projectId);
    }

    @Test
    public void ChecksDefaultConfiguration() {

        assertEquals(this.config.getDefaultQueryConfig().getUsePreviewMode(), this.deliveryService.items().getQueryConfig().getUsePreviewMode());
        assertEquals(this.config.getDefaultQueryConfig().getWaitForLoadingNewContent(), this.deliveryService.items().getQueryConfig().getWaitForLoadingNewContent());

    }
}
