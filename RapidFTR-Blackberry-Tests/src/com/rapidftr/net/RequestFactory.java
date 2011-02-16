package com.rapidftr.net;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Request;

public class RequestFactory {
    private static final String EXTRA = ";deviceside=true;ConnectionTimeout=10000";

    public static PostData createPostData() {
        Part apart = new Part("hello".getBytes(), null);
        Part[] parts = new Part[]{apart};
        return new PostData(parts, "");
    }

    public static Request createPostRequest(String url, String authToken, PostData postData, HttpBatchRequestHandler requestHandler, Object context) {
        return Request.createPostRequest(url + EXTRA,
                null, getAuthToken(authToken), requestHandler, postData, context);
    }

    private static Arg[] getAuthToken(String authToken) {
        Arg[] newArgs = new Arg[1];
        newArgs[0] = new Arg(Arg.AUTHORIZATION,
                "RFTR_Token " + authToken);
        return newArgs;
    }

    public static Request createGetRequest(String url, String authToken, HttpBatchRequestHandler requestHandler, Object context) {
        return Request.createGetRequest(url + EXTRA,
                        null, getAuthToken(authToken), requestHandler, context);
        
    }
}
