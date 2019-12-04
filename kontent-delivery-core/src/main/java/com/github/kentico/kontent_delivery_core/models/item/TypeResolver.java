/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.models.item;

import com.github.kentico.kontent_delivery_core.interfaces.item.IContentItem;

import io.reactivex.functions.Function;

public class TypeResolver<TItem extends IContentItem> {

    private Function<Void, TItem> resolver;
    private String type;

    /**
     * Type resolvers are used to create strongy typed objects for given Kentico content type identified by its codename
     * @param type Codename of Kentico Content type
     * @param resolver Function that returns instance of IContentItem class representing content type
     */
    public TypeResolver( String type, Function<Void, TItem> resolver){
        this.type = type;
        this.resolver = resolver;
    }

    public TItem createInstance() throws Exception {
        return this.resolver.apply(null);
    }

    public Function<Void, TItem> getResolver(){
        return this.resolver;
    }

    public String getType(){ return this.type; }
}
