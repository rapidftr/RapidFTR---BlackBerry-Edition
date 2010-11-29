package com.rapidftr.net;

import com.rapidftr.utilities.HttpSettings;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.*;

import java.io.IOException;

public class HttpServer {

	// private Request request = null;
	private RequestPool requestPool = RequestPool.getInstance();
	private HttpSettings settings;

	public HttpServer(HttpSettings settings) {
		this.settings = settings;
	}

	public void postToServer(String url, Arg[] postParams, Arg[] httpArgs,
			RequestListener listener, PostData multiPart, Object context) {
		requestPool.execute(Request.post(buildFullyQualifiedUrl(url),
				postParams, httpArgs, listener, multiPart, context));
	}

	public void getFromServer(String url, Arg[] inputParams, Arg[] httpArgs,
			RequestListener listener, Object context) {
		requestPool.execute(Request.get(buildFullyQualifiedUrl(url),
				inputParams, httpArgs, listener, context));

	}

	public Response getFromServer(String url, Arg[] inputParams, Arg[] httpArgs)
			throws IOException {
		return Request.get(buildFullyQualifiedUrl(url), inputParams, httpArgs,
				null);
	}

	public void cancelRequest() {
		requestPool.cancelAllRequests();
	}

	public String buildFullyQualifiedUrl(String uri) {

		String url = getUrlPrefix() + uri + ";deviceside=true"
				+ ";ConnectionTimeout=" + settings.getTimeOut();
		return url;
	}

	private String getUrlPrefix() {
		return settings.getHost() + "/";
	}

	public static void printResponse(Response res) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("*********Response*******\n");
		buffer.append("code:" + res.getCode() + "\n");
		if (res.getException() != null) {
			buffer.append(res.getException().toString() + "\n");
		}
		Arg[] headers = res.getHeaders();
		if (headers != null) {
			for (int i = 0; i < headers.length; i++) {
				buffer.append(headers[i].getKey() + ":" + headers[i].getValue()
						+ "\n");
			}
		}

		Result result = res.getResult();
		if (result != null) {
			buffer.append(result.toString());
		}
		System.out.println(buffer.toString());
	}

}
