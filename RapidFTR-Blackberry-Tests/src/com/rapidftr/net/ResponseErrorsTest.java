package com.rapidftr.net;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import org.junit.Test;

import com.sun.me.web.request.Response;

public class ResponseErrorsTest {

    @Test
    public void shouldGetErrorMessageWhenResponseCodeIsForbidden() {
        Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_FORBIDDEN);
        final ResponseErrors responseErrors = new ResponseErrors();
        responseErrors.add(response);
        assertEquals("Authentication Failure", responseErrors.getMessage());
    }

    @Test
    public void shouldGetErrorMessageWhenResponseCodeIsUnAuthorized() {
        Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_UNAUTHORIZED);
        final ResponseErrors responseErrors = new ResponseErrors();
        responseErrors.add(response);
        assertEquals("Authentication Failure", responseErrors.getMessage());
    }

    @Test
    public void shouldGetDefaultErrorMessage() {
        Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_BAD_REQUEST);
        final ResponseErrors responseErrors = new ResponseErrors();
        responseErrors.add(response);
        assertEquals("Error occurred", responseErrors.getMessage());
    }

    @Test
    public void shouldGetMessageWhenIOException() {
        Response response = new Response();
        response.setException(new IOException());
        final ResponseErrors responseErrors = new ResponseErrors();
        responseErrors.add(response);
        assertEquals("Cannot connect to this web address. Contact Admin for help.", responseErrors.getMessage());
    }


}
