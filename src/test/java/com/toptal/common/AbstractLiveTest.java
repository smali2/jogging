// Bismillah Hirrahman Nirrahim

package com.toptal.common;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.Serializable;

import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.toptal.Consts;
import com.toptal.test.IMarshaller;



public abstract class AbstractLiveTest<T extends Serializable> {

    protected final Class<T> clazz;

    @Autowired
    protected IMarshaller marshaller;

    public AbstractLiveTest(final Class<T> clazzToSet) {
        super();

        Preconditions.checkNotNull(clazzToSet);
        clazz = clazzToSet;
    }

    // template method

    public abstract void create();

    public abstract String createAsUri();

    protected final void create(final T resource) {
        createAsUri(resource);
    }

    protected final String createAsUri(final T resource) {
        final Response response = createAsResponse(resource);
        Preconditions.checkState(response.getStatusCode() == 201, "create operation: " + response.getStatusCode());

        final String locationOfCreatedResource = response.getHeader(HttpHeaders.LOCATION);
        Preconditions.checkNotNull(locationOfCreatedResource);
        return locationOfCreatedResource;
    }

    final Response createAsResponse(final T resource) {
        Preconditions.checkNotNull(resource);

        final String resourceAsString = marshaller.encode(resource);
        return RestAssured.given()
            .contentType(marshaller.getMime())
            .body(resourceAsString)
            .post(getURL());
    }

    //

    protected String getURL() {
        return "http://localhost:" + Consts.APPLICATION_PORT;
    }

}
