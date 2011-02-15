package com.rapidftr.net;

import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class TstRequestListener implements RequestListener {
    private Response response;

    public void done(Object context, Response result) throws Exception {
        response = result;
    }

    public void readProgress(Object context, int bytes, int total) {
    }

    public void writeProgress(Object context, int bytes, int total) {
    }

    public Response getResponse() {
        return response;
    }
}