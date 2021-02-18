// Bismillah Hirrahman Nirrahim

package com.jogger.common;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import org.hamcrest.core.AnyOf;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.jogger.web.util.HTTPLinkHeaderUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public abstract class AbstractDiscoverabilityLiveTest<T extends Serializable> extends AbstractLiveTest<T> {

    public AbstractDiscoverabilityLiveTest(final Class<T> clazzToSet) {
        super(clazzToSet);
    }

    // tests

    // discoverability

    @Test
    public void whenInvalidPOSTIsSentToValidURIOfResource_thenAllowHeaderListsTheAllowedActions() {
        // Given
        final String uriOfExistingResource = createAsUri();

        // When
        final Response res = RestAssured.post(uriOfExistingResource);

        // Then
        final String allowHeader = res.getHeader(HttpHeaders.ALLOW);
        assertThat(allowHeader, AnyOf.anyOf(containsString("GET"), containsString("PUT"), containsString("DELETE")));
    }



    @Test
    public void whenResourceIsRetrieved_thenUriToGetAllResourcesIsDiscoverable() {
        // Given
        final String uriOfExistingResource = createAsUri();

        // When
        final Response getResponse = RestAssured.get(uriOfExistingResource);

        // Then
        final String uriToAllResources = HTTPLinkHeaderUtil.extractURIByRel(getResponse.getHeader("Link"), "collection");

        final Response getAllResponse = RestAssured.get(uriToAllResources);
        assertThat(getAllResponse.getStatusCode(), is(200));
    }

    // template method

}

