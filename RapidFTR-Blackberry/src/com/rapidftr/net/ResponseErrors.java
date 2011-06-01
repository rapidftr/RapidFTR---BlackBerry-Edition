package com.rapidftr.net;

import java.util.Enumeration;
import java.util.Vector;

import com.sun.me.web.request.Response;

public class ResponseErrors {
    private Vector errors;
    
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
        if (errorResponses.hasMoreElements()) {
            Response response = (Response) errorResponses.nextElement();
            return response.getErrorMessage();
        }
        return "";
    }
}
