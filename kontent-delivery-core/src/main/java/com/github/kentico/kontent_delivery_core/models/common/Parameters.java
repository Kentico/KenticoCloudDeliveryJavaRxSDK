/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_core.models.common;

import com.github.kentico.kontent_delivery_core.interfaces.item.common.IQueryParameter;
import com.github.kentico.kontent_delivery_core.utils.StringHelper;

import java.util.List;

/**
 * URL parameter
 */

public class Parameters {

    public static class ElementsParameter implements IQueryParameter {

        private List<String> elements;

        public ElementsParameter(
                 List<String> elements
        ) {
            this.elements = elements;
        }

        public String getParam(){

            return "elements";
        }

        public String getParamValue(){
            return StringHelper.join(this.elements, ",");
        }
    }

    public static class LimitParameter implements IQueryParameter {

        private int limit;

        public LimitParameter(
                int limit
        ) {
            this.limit = limit;
        }

        public String getParam(){

            return "limit";
        }

        public String getParamValue(){
            return Integer.toString(this.limit);
        }
    }

    public static class SkipParameter implements IQueryParameter {

        private int skip;

        public SkipParameter(
                int skip
        ) {
            this.skip = skip;
        }

        public String getParam(){

            return "skip";
        }

        public String getParamValue(){
            return Integer.toString(this.skip);
        }
    }

    public static class OrderParameter implements IQueryParameter {

        private String field;
        private OrderType orderType;

        public OrderParameter(
                 String field,
                 OrderType type
        ) {
            orderType = type;
            this.field = field;
        }

        public String getParam(){
            return "order";
        }

        public String getParamValue(){
            return field + "[" + orderType.toString() + "]";
        }
    }

    public static class DepthParameter implements IQueryParameter{

        private int depth;

        public DepthParameter(
                int depth
        ) {
            this.depth = depth;
        }

        public String getParam(){

            return "depth";
        }

        public String getParamValue(){
            return Integer.toString(this.depth);
        }
    }

    public static class LanguageParameter implements IQueryParameter {

        private String language;

        public LanguageParameter(
                 String language
        ) {
            this.language = language;
        }

        public String getParam(){

            return "language";
        }

        public String getParamValue() {
            return this.language;
        }
    }
}
