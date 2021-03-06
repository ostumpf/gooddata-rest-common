/*
 * Copyright (C) 2004-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

import static com.gooddata.util.ResourceUtils.OBJECT_MAPPER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GDDateTimeSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        final GDDateTimeClass foo = new GDDateTimeClass(new DateTime(2012, 3, 20, 14, 31, 5, 3, DateTimeZone.UTC));
        final String json = OBJECT_MAPPER.writeValueAsString(foo);
        final JsonNode node = OBJECT_MAPPER.readTree(json);

        assertThat(node.path("date").textValue(), is("2012-03-20 14:31:05"));
    }
}