package com.rapidftr.net;

import com.rapidftr.utilities.Properties;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpService {

	private final HttpServer httpServer;
	private int requestTimeout;

	public HttpService(HttpServer httpServer) {
		this.httpServer = httpServer;
		requestTimeout = Properties.getInstance().getHttpRequestTimeout();

	}

	public void get(String url, Arg[] inputArgs, RequestListener listener) {
		httpServer.getFromServer(url,
				inputArgs, listener);
	}

	public void post(String url, Arg[] postArgs, RequestListener listener,
			Object context) {
		httpServer.postToServer(url,
				postArgs, listener, context);
	}

	public void cancelRequest() {

		httpServer.cancelRequest();
	}


}
