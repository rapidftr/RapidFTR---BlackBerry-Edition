package com.rapidftr.utilities;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Request;
import com.sun.me.web.request.Response;

import java.io.IOException;

public class HttpServer {

    private int requestTimeout;
    private final SettingsStore settings;

    public HttpServer(SettingsStore settings) {
        this.settings = settings;
        requestTimeout = Properties.getInstance().getHttpRequestTimeout();
    }

    public Response postToServer(Arg[] postParams) {
        try {
            return Request.post("http://dev.rapidftr.com:3000/sessions", postParams, getHttpArgs(), null, null);
        } catch (IOException e) {
            // TODO: Decide what to do with a connection problem
            throw new RuntimeException("Problem POSTing to server");
        }
    }

    public Response getFromServer(String uri) {
        try {
            // TODO: Perhaps inspect response code: if unauthorized, throw NotAuthorizedException?
            return Request.get(buildConnectionUrl(uri), new Arg[0], getHttpArgs(), null);
        } catch (IOException e) {
            // TODO: Decide what to do with a connection problem
            throw new RuntimeException("Problem GETing from server");
        }
    }

    private String buildConnectionUrl(String uri) {
        String url = getUrlPrefix() + uri + ";ConnectionTimeout=" + requestTimeout;
        return url;
    }

    private String getUrlPrefix() {
        String hostName = Properties.getInstance().getHostName();
        int port = Properties.getInstance().getPort();

        return "http://" + hostName + ":" + port + "/";
    }

    private Arg[] getHttpArgs() {
        final Arg authArg = new Arg(Arg.AUTHORIZATION, getAuthorizationToken());
        final Arg acceptJson = new Arg("Accept", "application/json");

        final Arg[] args = {authArg, acceptJson};

        return args;
    }

    private String getAuthorizationToken() {
        return "RFTR_Token " + settings.getAuthorizationToken();
    }

}
