package com.rapidftr.utilities;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Request;
import com.sun.me.web.request.Response;

import java.io.IOException;

public class HttpServer {

    private static HttpServer instance;

    private int requestTimeout;

    public static synchronized HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }

        return instance;
    }

    public HttpServer() {
        requestTimeout = Properties.getInstance().getHttpRequestTimeout();
    }

    private String getUrlPrefix() {
        String hostName = Properties.getInstance().getHostName();
        int port = Properties.getInstance().getPort();

        return "http://" + hostName + ":" + port + "/";
    }

    public Response postToServer(Arg[] postParams) {
        try {
            return Request.post("http://dev.rapidftr.com:3000/sessions", postParams, getHttpArgs(), null, null);
        } catch (IOException e) {
            // TODO: Decide what to do with a connection problem
            throw new RuntimeException("Problem POSTing to server");
        }
    }

    private String buildConnectionUrl(String uri) {
        String url = getUrlPrefix() + uri + ";ConnectionTimeout=" + requestTimeout;
        return url;
    }

    private Arg[] getHttpArgs() {
		final Arg authArg = new Arg(Arg.AUTHORIZATION, getAuthorizationToken());
		final Arg acceptJson = new Arg("Accept", "application/json");

		final Arg[] args = { authArg, acceptJson };

		return args ;
	}

    private String getAuthorizationToken() {
        return "";
    }

}
