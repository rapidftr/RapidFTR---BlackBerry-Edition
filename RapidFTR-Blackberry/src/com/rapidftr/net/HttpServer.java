package com.rapidftr.net;

import com.rapidftr.utilities.HttpSettings;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.*;

import java.io.IOException;

public class HttpServer {

	private RequestPool requestPool;
	private HttpSettings settings;
    private HttpGateway httpGateway;

    public HttpServer(HttpSettings settings) {
		this(settings, new HttpGateway(new ConnectionFactory()));
	}

    public HttpServer(HttpSettings settings, HttpGateway httpGateway) {
		this.settings = settings;
        this.httpGateway = httpGateway;
        requestPool = RequestPool.getInstance(httpGateway);
	}

	public void postToServer(String url, Arg[] postParams, Arg[] httpArgs,
			RequestListener listener, PostData multiPart, Object context) {
		requestPool.execute(Request.createPostRequest(buildFullyQualifiedUrl(url),
				postParams, httpArgs, listener, multiPart, context));
	}

	public void getFromServer(String url, Arg[] inputParams, Arg[] httpArgs,
			RequestListener listener, Object context) {
		requestPool.execute(Request.createGetRequest(buildFullyQualifiedUrl(url),
				inputParams, httpArgs, listener, context));

	}

	public Response getFromServer(String url, Arg[] inputParams, Arg[] httpArgs)
			throws IOException {
		Request request = Request.createGetRequest(buildFullyQualifiedUrl(url), inputParams, httpArgs);
        return httpGateway.perform(request);
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
