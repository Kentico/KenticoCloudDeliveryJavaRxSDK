/*
 * Copyright 2018 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_android;


import com.github.kentico.kontent_delivery_core.adapters.IHttpAdapter;
import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryConfig;
import com.github.kentico.kontent_delivery_core.models.common.Header;
import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class AndroidHttpAdapter implements IHttpAdapter {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public String get(String url, IQueryConfig queryConfig, List<Header> headers) {
        Request.Builder builder = new Request.Builder()
                .url(url);

        for(Header header : headers){
            builder.addHeader(header.getName(), header.getValue());
        }

        Request request = builder.build();

        Response response;
        try {
            response = okHttpClient.newCall(request).execute();

            ResponseBody body = response.body();

            if (body == null){
                throw new NullPointerException("Response from server is null");
            }

            return body.string();

        } catch (IOException e) {
            e.printStackTrace();
            throw new KontentException(e.getMessage(), e);
        }
    }
}
