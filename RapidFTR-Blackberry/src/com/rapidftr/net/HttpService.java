package com.rapidftr.net;

import com.rapidftr.services.UploadChildRecordsService;
import com.rapidftr.utilities.Properties;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpService {

	private final HttpServer httpServer;
	private int requestTimeout;
	private final SettingsStore settingsStrore;

	public HttpService(HttpServer httpServer, SettingsStore settingsStrore) {
		this.httpServer = httpServer;
		this.settingsStrore = settingsStrore;
		requestTimeout = Properties.getInstance().getHttpRequestTimeout();

	}

	public void get(String url, Arg[] inputArgs, RequestListener listener) {
		httpServer.getFromServer(url, inputArgs,
				httpArgsWithAuthorizationToken(), listener);
	}

	public void post(String url, Arg[] postArgs, RequestListener listener,
			Object context) {
		httpServer.postToServer(url, postArgs, httpArgs(), listener, context);
	}

	public void postWithAuthorizationToken(String url, PostData postData,RequestListener listener) {
		final Arg authArg = new Arg(Arg.AUTHORIZATION, getAuthorizationToken());
		final Arg acceptJson = new Arg("Content-Type", "multipart/form-data;boundary="+postData.getBoundary());
		final Arg[] args = { authArg, acceptJson };	
		
		httpServer.postToServer(url, null, args, listener, null,postData);
	}
	public void cancelRequest() {

		httpServer.cancelRequest();
	}

	public Arg[] httpArgs() {
		final Arg acceptJson = new Arg("Accept", "application/json");
		final Arg[] args = { acceptJson };
		return args;

	}

	public Arg[] httpArgsWithAuthorizationToken() {
		final Arg authArg = new Arg(Arg.AUTHORIZATION, getAuthorizationToken());
		final Arg acceptJson = new Arg("Accept", "application/json");
		final Arg[] args = { authArg, acceptJson };

		return args;

	}

	private String getAuthorizationToken() {
		return "RFTR_Token " + settingsStrore.getAuthorizationToken();
	}



}
