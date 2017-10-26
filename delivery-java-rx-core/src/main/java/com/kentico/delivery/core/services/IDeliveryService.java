package com.kentico.delivery.core.services;

import com.kentico.delivery.core.interfaces.item.item.IContentItem;
import com.kentico.delivery.core.query.item.MultipleItemQuery;
import com.kentico.delivery.core.query.item.SingleItemQuery;
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
     * @return Query to get the item
     */
     <TItem extends IContentItem> MultipleItemQuery<TItem> items();

     /**
     * Query to fetch single item
     * @param itemCodename Codename of the item
     * @param <TItem> Class representing the type you want to return. Use 'IContentItem' if multiple types can be returned
     *               or if you don't know what types will be returned beforehands.
     * @return Query to get the item
     */
     <TItem extends IContentItem> SingleItemQuery<TItem> item( String itemCodename);

     /**
     * Query to fetch single type
     * @param typeCodename Type codename
     * @return Query to get the type
     */
     SingleTypeQuery type( String typeCodename);

    /**
     * Query to fetch multiple types
     * @return Qury to get multiple types
     */
     MultipleTypeQuery types();
}
