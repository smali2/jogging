// Bismillah Hirrahman Nirrahim

package com.jogger.common;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public abstract class AbstractBasicLiveTest<T extends Serializable> extends AbstractLiveTest<T> {

    public AbstractBasicLiveTest(final Class<T> clazzToSet) {
        super(clazzToSet);
    }

    // find - all - paginated

    @Test
    public void whenResourcesAreRetrievedPaged_then200IsReceived() {
        create();
        
        final Response response = RestAssured.get(getURL() + "?page=0&size=10");

        assertThat(response.getStatusCode(), is(200));
    }

    @Test
    public void whenPageOfResourcesAreRetrievedOutOfBounds_then404IsReceived() {
        final String url = getURL() + "?page=" + randomNumeric(5) + "&size=10";
        final Response response = RestAssured.get(url);

        assertThat(response.getStatusCode(), is(404));
    }

 
    // etags

    @Test
    public void givenResourceExists_whenRetrievingResource_thenEtagIsAlsoReturned() {
        // Given
        final String uriOfResource = createAsUri();

        // When
        final Response findOneResponse = RestAssured.given()
            .header("Accept", "application/json")
            .get(uriOfResource);

        // Then
        assertNotNull(findOneResponse.getHeader(HttpHeaders.ETAG));
    }

    @Test
    public void givenResourceWasRetrieved_whenRetrievingAgainWithEtag_thenNotModifiedReturned() {
        // Given
        final String uriOfResource = createAsUri();
        final Response findOneResponse = RestAssured.given()
            .header("Accept", "application/json")
            .get(uriOfResource);
        final String etagValue = findOneResponse.getHeader(HttpHeaders.ETAG);

        // When
        final Response secondFindOneResponse = RestAssured.given()
            .header("Accept", "application/json")
            .headers("If-None-Match", etagValue)
            .get(uriOfResource);

        // Then
        assertTrue(secondFindOneResponse.getStatusCode() == 304);
    }

    

    

}