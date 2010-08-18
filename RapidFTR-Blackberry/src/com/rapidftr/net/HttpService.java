package com.rapidftr.net;

import java.io.IOException;

import com.rapidftr.utilities.Properties;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpService {

	private final HttpServer httpServer;
	private final SettingsStore settingsStrore;

	public HttpService(HttpServer httpServer, SettingsStore settingsStrore) {
		this.httpServer = httpServer;
		this.settingsStrore = settingsStrore;
		Properties.getInstance().getHttpRequestTimeout();
	}

	
	/*
	 * Pass Request listener to make asynchronous request
	 */
	public void get(String url, Arg[] inputArgs, Arg[] httpArgs,
			RequestListener listener) {
		httpArgs = appendAuthenticationToken(httpArgs);
		httpServer.getFromServer(url, inputArgs, httpArgs, listener);
	}
	
	/*
	 * Use this for synchronous request
	 */
	
	public Response get(String url, Arg[] inputArgs, Arg[] httpArgs) throws IOException {
		httpArgs = appendAuthenticationToken(httpArgs);
		return httpServer.getFromServer(url, inputArgs, httpArgs);
	}
	

	private Arg[] appendAuthenticationToken(Arg[] args) {
		Arg[] newArgs = new Arg[args.length + 1];
		System.arraycopy(args, 0, newArgs, 0, args.length);
		newArgs[args.length] = new Arg(Arg.AUTHORIZATION,
				getAuthorizationToken());
		return newArgs;
	}

	public void post(String url,Arg[] postArgs,Arg[] httpArgs,
			RequestListener listener, PostData postData, Object context){
		httpArgs = appendAuthenticationToken(httpArgs);
		httpServer.postToServer(url, postArgs, httpArgs, listener, postData,
				context);
	}

	public void cancelRequest() {
		httpServer.cancelRequest();
	}

	private String getAuthorizationToken() {
		return "RFTR_Token " + settingsStrore.getAuthorizationToken();
	}

}
