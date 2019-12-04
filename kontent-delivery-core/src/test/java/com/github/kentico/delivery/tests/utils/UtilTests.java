/*
 * Copyright 2019 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.kentico.kontent_delivery_tests.utils;


import com.github.kentico.kontent_delivery_core.models.exceptions.KontentException;
import com.github.kentico.kontent_delivery_core.utils.DateHelper;
import com.github.kentico.kontent_delivery_core.utils.ErrorHelper;
import com.github.kentico.kontent_delivery_core.utils.StringHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the different utils we have within the sdk, ensure we are returning the correct values
 *
 */

public class UtilTests {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void checkPraseDateException() throws Exception {
        exception.expect(ParseException.class);
        DateHelper.parseIso8601("fadsdfjlkasdjfa");
    }

    @Test
    public void checkValidDate() throws Exception {
        String dateString = "2017-11-22T11:45:51-05:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date da = df.parse(dateString);
        assertEquals(DateHelper.parseIso8601(dateString), da);
    }

    @Test
    public void checkStringJoiner() throws Exception {
        List<String> list = new ArrayList();
        list.add("test");
        list.add("test2");
        list.add("test3");
        assertEquals(StringHelper.join(list, ";"), "test;test2;test3");
    }

    @Test
    public void checkErrorHelperNullJSON()
    {
        exception.expect(KontentException.class);
        ErrorHelper.getDeliveryError(null);
    }

    @Test
    public void checkErrorParsing() throws Exception {
        assertEquals("|CwTplnT0214=.c642f3df_", ErrorHelper.getDeliveryError(TestHelper.GetJObjectFromFile("error.json")).requestId);
    }
}
