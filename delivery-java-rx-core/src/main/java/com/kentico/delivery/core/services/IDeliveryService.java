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

import com.kentico.delivery.core.interfaces.item.item.IContentItem;
import com.kentico.delivery.core.query.element.SingleContentTypeElement;
import com.kentico.delivery.core.query.item.MultipleItemQuery;
import com.kentico.delivery.core.query.item.SingleItemQuery;
import com.kentico.delivery.core.query.taxonomy.MultipleTaxonomyQuery;
import com.kentico.delivery.core.query.taxonomy.SingleTaxonomyQuery;
import com.kentico.delivery.core.query.type.MultipleTypeQuery;
import com.kentico.delivery.core.query.type.SingleTypeQuery;
import com.kentico.delivery.core.request.IRequestService;

public interface IDeliveryService{

    /**
     * Request service used for HTTP requests and fetching Observable
     */
    IRequestService getRequestService();

    /**
     * Query to fetch multiple items
     * @param <TItem> Class representing the type you want to return. Use 'IContentItem' if multiple types can be returned
     *               or if you don't know what types will be returned beforehands.
     * @return Query for item
     */
     <TItem extends IContentItem> MultipleItemQuery<TItem> items();

     /**
     * Query to fetch single item
     * @param codename Codename of the item
     * @param <TItem> Class representing the type you want to return. Use 'IContentItem' if multiple types can be returned
     *               or if you don't know what types will be returned beforehands.
     * @return Query for item
     */
     <TItem extends IContentItem> SingleItemQuery<TItem> item( String codename);

     /**
     * Query to fetch single type
     * @param codename Type codename
     * @return Query for type
     */
     SingleTypeQuery type( String codename);

     /**
     * Query to fetch multiple types
     * @return Query for types
     */
     MultipleTypeQuery types();

     /**
     * Query to fetch multiple taxonomies
     * @return Query for taxonomies
     */
     MultipleTaxonomyQuery taxonomies();

    /**
     * Query to fetch single taxonomy
     * @param codename Codename of the taxonomy group
     * @return Query for taxonomy
     */
     SingleTaxonomyQuery taxonomy(String codename);

    /**
     * Query to fetch single element of content type
     * @param typeCodename Codename of the content type
     * @param elementCodename Codename of the element
     * @return Query for content type element
     */
    SingleContentTypeElement contenTypeElement(String typeCodename, String elementCodename);
}
