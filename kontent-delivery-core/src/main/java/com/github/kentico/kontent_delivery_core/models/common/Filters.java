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
 * Represents URL filter used for querying items
 */
public class Filters {

    public static class EqualsFilter implements IQueryParameter {

        private String field;
        private String value;

        public EqualsFilter(
                 String field,
                String value
        ) {
            this.field = field;
            this.value = value;
        }

        public String getParam(){

            return this.field.trim();
        }

        public String getParamValue(){
            if (this.value == null) {
                return null;
            }

            return this.value.trim();
        }
    }

    public static class AllFilter implements IQueryParameter {

        private String field;
        private List<String> values;

        public AllFilter(
                 String field,
                List<String> values
        ) {
            this.field = field;
            this.values = values;
        }

        public String getParam(){

            return this.field.trim() + "[all]";
        }

        public String getParamValue(){
            return StringHelper.join(values, ",");
        }
    }

    public static class AnyFilter implements IQueryParameter {

        private String field;
        private List<String> values;

        public AnyFilter(
                 String field,
                List<String> values
        ) {
            this.field = field;
            this.values = values;
        }

        public String getParam(){

            return this.field.trim() + "[any]";
        }

        public String getParamValue(){
            return StringHelper.join(values, ",");
        }
    }

    public static class ContainsFilter implements IQueryParameter {

        private String field;
        private List<String> values;

        public ContainsFilter(
                 String field,
                List<String> values
        ) {
            this.field = field;
            this.values = values;
        }

        public String getParam(){

            return this.field.trim() + "[contains]";
        }

        public String getParamValue(){
            return StringHelper.join(values, ",");
        }
    }

    public static class Infilter implements IQueryParameter {

        private String field;
        private List<String> values;

        public Infilter(
                 String field,
                List<String> values
        ) {
            this.field = field;
            this.values = values;
        }

        public String getParam(){

            return this.field.trim() + "[in]";
        }

        public String getParamValue(){
            return StringHelper.join(values, ",");
        }
    }


    public static class GreaterThanFilter implements IQueryParameter {

        private String field;
        private String value;

        public GreaterThanFilter(
                 String field,
                String value
        ) {
            this.field = field;
            this.value = value;
        }

        public String getParam(){

            return this.field.trim() + "[gt]";
        }

        public String getParamValue(){
            return this.value.trim();
        }
    }

    public static class LessThanFilter implements IQueryParameter {

        private String field;
        private String value;

        public LessThanFilter(
                 String field,
                String value
        ) {
            this.field = field;
            this.value = value;
        }

        public String getParam(){

            return this.field.trim() + "[lt]";
        }

        public String getParamValue(){
            return this.value.trim();
        }
    }

    public static class LessThanOrEqualFilter implements IQueryParameter {

        private String field;
        private String value;

        public LessThanOrEqualFilter(
                 String field,
                String value
        ) {
            this.field = field;
            this.value = value;
        }

        public String getParam(){

            return this.field.trim() + "[lte]";
        }

        public String getParamValue(){
            return this.value.trim();
        }
    }
    public static class GreaterThanOrEqualFilter implements IQueryParameter {

        private String field;
        private String value;

        public GreaterThanOrEqualFilter(
                 String field,
                String value
        ) {
            this.field = field;
            this.value = value;
        }

        public String getParam(){

            return this.field.trim() + "[gte]";
        }

        public String getParamValue(){
            return this.value.trim();
        }
    }
    public static class RangeFilter implements IQueryParameter {

        private String field;
        private int lowerValue;
        private int higherValue;

        public RangeFilter(
                 String field,
                int lowerValue,
                int higherValue
        ) {
            this.field = field;
            this.lowerValue = lowerValue;
            this.higherValue = higherValue;
        }

        public String getParam(){

            return this.field.trim() + "[range]";
        }

        public String getParamValue(){
            return lowerValue + "," + higherValue;
        }
    }
}
