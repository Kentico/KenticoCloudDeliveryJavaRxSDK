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

import com.github.kentico.kontent_delivery_core.interfaces.item.IContentItemSystemAttributes;

import java.util.Date;

/**
 * Content item system attributes
 */
public class ContentItemSystemAttributes implements IContentItemSystemAttributes {
    private String id;
    private String name;
    private String codename;
    private String type;
    private Date lastModified;
    private String language;
    private String[] sitemap_locations;

    public ContentItemSystemAttributes(
             String id,
             String name,
             String codename,
             String type,
             Date lastModified,
             String language,
            String[] sitemapLocations
            ){
        this.id = id;
        this.name = name;
        this.language = language;
        this.type = type;
        this.lastModified = lastModified;
        this.sitemap_locations = sitemapLocations;
        this.codename = codename;
    }

    public String[] getSitemapLocations() {
        return sitemap_locations;
    }

    public String getLanguage() {
        return language;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getType() {
        return type;
    }

    public String getCodename() {
        return codename;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
