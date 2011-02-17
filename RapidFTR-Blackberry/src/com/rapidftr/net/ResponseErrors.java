package com.rapidftr.net;

import com.sun.me.web.request.Response;

import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

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
