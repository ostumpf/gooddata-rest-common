/*
 * Copyright (C) 2004-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.collections;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.Test;

import java.net.URI;

import static com.gooddata.collections.PageRequest.DEFAULT_LIMIT;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PageRequestTest {

    @Test
    public void shouldGetAndSetValues() throws Exception {
        final PageRequest pageRequest = new PageRequest("foo", 123);

        assertThat(pageRequest.getOffset(), is("foo"));
        pageRequest.setOffset("bar");
        assertThat(pageRequest.getOffset(), is("bar"));

        assertThat(pageRequest.getLimit(), is(123));
        pageRequest.setLimit(987);
        assertThat(pageRequest.getLimit(), is(987));
    }

    @Test
    public void testGetPageUri() throws Exception {
        final PageRequest pageRequest = new PageRequest(12, 10);
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("test_uri");
        final URI pageUri = pageRequest.getPageUri(uriBuilder);
        assertThat(pageUri, notNullValue());
        assertThat(pageUri.toString(), is("test_uri?offset=12&limit=10"));
    }

    @Test
    public void testGetPageUriWithStringOffset() throws Exception {
        final PageRequest pageRequest = new PageRequest("17", 10);
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("test_uri");
        final URI pageUri = pageRequest.getPageUri(uriBuilder);
        assertThat(pageUri, notNullValue());
        assertThat(pageUri.toString(), is("test_uri?offset=17&limit=10"));
    }

    @Test
    public void testUpdateWithPageParams() throws Exception {
        final PageRequest pageRequest = new PageRequest(12, 10);
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("test_uri/{test}");
        final String pageUri = pageRequest.updateWithPageParams(uriBuilder).build().toUriString();
        assertThat(pageUri, is("test_uri/{test}?offset=12&limit=10"));
    }

    @Test
    public void testUpdateWithPageParamsWithStringOffset() throws Exception {
        final PageRequest pageRequest = new PageRequest("17", 10);
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("test_uri/{test}");
        final String pageUri = pageRequest.updateWithPageParams(uriBuilder).build().toUriString();
        assertThat(pageUri, is("test_uri/{test}?offset=17&limit=10"));
    }

    @Test
    public void testUpdateWithPageParamsIdempotency() throws Exception {
        final PageRequest pageRequest = new PageRequest(12, 10);
        final UriComponentsBuilder uriBuilder1 = UriComponentsBuilder.fromUriString("test_uri/{test}");
        final UriComponentsBuilder uriBuilder2 = pageRequest.updateWithPageParams(uriBuilder1);
        final UriComponentsBuilder uriBuilder3 = pageRequest.updateWithPageParams(uriBuilder2);
        final String pageUri = uriBuilder3.build().toUriString();
        assertThat(pageUri, is("test_uri/{test}?offset=12&limit=10"));
    }

    @Test
    public void testGetPageUriDefaultValue() throws Exception {
        final PageRequest pageRequest = new PageRequest();
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("test_uri");
        final URI pageUri = pageRequest.getPageUri(uriBuilder);
        assertThat(pageUri, notNullValue());
        assertThat(pageUri.toString(), is("test_uri?limit=100"));
    }

    @Test
    public void testGetSanitizedLimit() throws Exception {
        final PageRequest pageRequest = new PageRequest();
        pageRequest.setLimit(-2);
        assertThat(pageRequest.getSanitizedLimit(), is(DEFAULT_LIMIT));
    }

    @Test
    public void shouldReturnDefaultForZero() throws Exception {
        final PageRequest pageRequest = new PageRequest(0);
        assertThat(pageRequest.getSanitizedLimit(), is(DEFAULT_LIMIT));
    }

    @Test
    public void shouldReturnMaxForSanitizedLimit() throws Exception {
        final PageRequest pageRequest = new PageRequest(100);
        assertThat(pageRequest.getSanitizedLimit(10), is(10));
    }

    @Test
    public void equals() {
        assertThat(new PageRequest(), is(not(new PageRequest(10))));

        assertThat(new PageRequest(10), is(new PageRequest(10)));
        assertThat(new PageRequest(10), is(not(new PageRequest(11))));

        assertThat(new PageRequest(1, 2), is(new PageRequest(1, 2)));
        assertThat(new PageRequest(1, 2), is(new PageRequest("1", 2)));
        assertThat(new PageRequest(1, 2), is(not(new PageRequest("meh", 2))));
        assertThat(new PageRequest(1, 2), is(not(new PageRequest("1", 3))));
    }

    @Test
    public void testToString() {
        assertThat(new PageRequest(1, 2).toString(), is("PageRequest[offset=1,limit=2]"));
    }

    @Test
    public void shouldVerifyEquals() throws Exception {
        EqualsVerifier.forClass(PageRequest.class)
                .withRedefinedSubclass(PageRequestChild.class)
                .suppress(Warning.NONFINAL_FIELDS) // this is java bean
                .verify();
    }

}
