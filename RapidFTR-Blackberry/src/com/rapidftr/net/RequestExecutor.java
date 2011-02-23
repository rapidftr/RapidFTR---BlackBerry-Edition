package com.rapidftr.net;

import com.sun.me.web.request.Request;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class RequestExecutor {
    private HttpGateway httpGateway;

    public RequestExecutor(HttpGateway httpGateway) {
        this.httpGateway = httpGateway;
    }

    public void execute(Request request) {
        Response response = new Response();
        try {
            response = httpGateway.perform(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setException(ex);
        } finally {
            RequestListener listener = request.getListener();
            if (listener != null) {
                try {
                    listener.done(request.getContext(), response);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }
}
