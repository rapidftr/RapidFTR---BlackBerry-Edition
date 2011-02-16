package com.rapidftr.net;

import com.sun.me.web.request.Response;

import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class ResponseErrors {
    private Vector errors;
    private static final String COULD_NOT_CONNECT = "Could not connect";

    public ResponseErrors() {
        errors = new Vector();
    }

    public void add(Response response) {
        errors.addElement(response);
    }

    public int size() {
        return errors.size();
    }


    public String getMessage() {
        Enumeration errorResponses = errors.elements();
        while(errorResponses.hasMoreElements()) {
            Response response = (Response) errorResponses.nextElement();
            final int responseCode = response.getResponseCode();
            if (responseCode == HttpConnection.HTTP_FORBIDDEN ||
                    response.getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED) {
                return "Authentication Failure";
            }
            final Exception exception = response.getException();
            if (exception != null && exception instanceof IOException) {
                return COULD_NOT_CONNECT;
            }
        }
        return "Errors have occurred";
    }
}
