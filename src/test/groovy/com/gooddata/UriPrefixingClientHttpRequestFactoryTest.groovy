package com.gooddata

import org.springframework.http.HttpMethod
import org.springframework.http.client.SimpleClientHttpRequestFactory
import spock.lang.Specification

class UriPrefixingClientHttpRequestFactoryTest extends Specification {

    private static final WRAPPED = new SimpleClientHttpRequestFactory()

    def "should create prefixed request"() {
        when:
        def request = requestFactory.createRequest(URI.create('/gdc/resource'), HttpMethod.GET)

        then:
        request.URI.toString() == 'http://localhost:1234/gdc/resource'

        where:
        requestFactory << [
                new UriPrefixingClientHttpRequestFactory(WRAPPED, 'http', 'localhost', 1234),
                new UriPrefixingClientHttpRequestFactory(WRAPPED, 'http://localhost:1234')
        ]
    }

}
