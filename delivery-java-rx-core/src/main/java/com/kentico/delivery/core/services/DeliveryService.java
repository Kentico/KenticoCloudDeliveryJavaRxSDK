/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kentico.delivery.core.services;

import com.kentico.delivery.core.adapters.IHttpAdapter;
import com.kentico.delivery.core.adapters.IRxAdapter;
import com.kentico.delivery.core.config.DeliveryClientConfig;
import com.kentico.delivery.core.interfaces.item.item.IContentItem;
import com.kentico.delivery.core.query.element.SingleContentTypeElement;
import com.kentico.delivery.core.query.item.MultipleItemQuery;
import com.kentico.delivery.core.query.item.SingleItemQuery;
import com.kentico.delivery.core.query.taxonomy.MultipleTaxonomyQuery;
import com.kentico.delivery.core.query.taxonomy.SingleTaxonomyQuery;
import com.kentico.delivery.core.query.type.MultipleTypeQuery;
import com.kentico.delivery.core.query.type.SingleTypeQuery;

public abstract class DeliveryService implements IDeliveryService {

    protected DeliveryClientConfig config;

    protected DeliveryService(DeliveryClientConfig config) {
        this.config = config;
    }

    abstract public IRxAdapter getRxAdapter();
    abstract public IHttpAdapter getHttpAdapter();

    public <TItem extends IContentItem> MultipleItemQuery<TItem> items(){
        return new MultipleItemQuery<>(this.config, this.getRxAdapter(), this.getHttpAdapter());
    }

    public <TItem extends IContentItem> SingleItemQuery<TItem> item(String itemCodename){
        return new SingleItemQuery<>(this.config, this.getRxAdapter(), this.getHttpAdapter(), itemCodename);
    }

    public SingleTypeQuery type(String typeCodename) {
        return new SingleTypeQuery(this.config, this.getRxAdapter(), this.getHttpAdapter(), typeCodename);
    }

    public MultipleTypeQuery types() {
        return new MultipleTypeQuery(this.config, this.getRxAdapter(), this.getHttpAdapter());
    }

    public MultipleTaxonomyQuery taxonomies(){
        return new MultipleTaxonomyQuery(this.config, this.getRxAdapter(), this.getHttpAdapter());
    }

    public SingleTaxonomyQuery taxonomy(String codename){
        return new SingleTaxonomyQuery(this.config, this.getRxAdapter(), this.getHttpAdapter(), codename);
    }

    public SingleContentTypeElement contenTypeElement(String typeCodename, String elementCodename){
        return new SingleContentTypeElement(this.config, this.getRxAdapter(), this.getHttpAdapter(), typeCodename, elementCodename);
    }
}
