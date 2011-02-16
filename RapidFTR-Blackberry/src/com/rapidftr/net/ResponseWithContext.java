package com.rapidftr.net;

import com.sun.me.web.request.Response;

public class ResponseWithContext {
    private Response response;
    private Object context;

    public ResponseWithContext(Response response, Object context) {

        this.response = response;
        this.context = context;
    }
}
