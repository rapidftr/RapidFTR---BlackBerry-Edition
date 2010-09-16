package com.rapidftr.net;

import java.io.IOException;

import com.rapidftr.utilities.Properties;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Request;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpServer {

	private Request request = null;
	private int requestTimeout;

	public HttpServer() {
		requestTimeout = Properties.getInstance().getHttpRequestTimeout();
	}

	public void postToServer(String url, Arg[] postParams, Arg[] httpArgs,
			RequestListener listener, PostData multiPart, Object context) {

		request = Request.post(buildFullyQualifiedUrl(url), postParams,
				httpArgs, listener, multiPart, context);
	}

	public void getFromServer(String url, Arg[] inputParams, Arg[] httpArgs,
			RequestListener listener,Object context) {
		request = Request.get(buildFullyQualifiedUrl(url), inputParams,
				httpArgs, listener, context);
	}
	
	public Response getFromServer(String url, Arg[] inputParams, Arg[] httpArgs) throws IOException {
		 return  Request.get(buildFullyQualifiedUrl(url), inputParams,
				httpArgs,null);
	}

	public void cancelRequest() {
		if (request != null)
			request.cancel();
	}

	public String buildFullyQualifiedUrl(String uri) {

		String url = getUrlPrefix() + uri + ";deviceside=true"
				+ ";ConnectionTimeout=" + requestTimeout;
		return url;
	}

	private String getUrlPrefix() {
		String hostName = Properties.getInstance().getHostName();
		int port = Properties.getInstance().getPort();
		return "http://" + hostName + ":" + port + "/";
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
