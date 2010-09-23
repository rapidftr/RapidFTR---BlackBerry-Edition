package com.rapidftr.net;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpBatchRequestHandler implements RequestListener {

	RequestCallBack requestCallBack;
	private int unprocessedRequests = 0;
	private int totalRequests = 0;

	private HttpService service;

	public HttpBatchRequestHandler(HttpService httpService) {
		service = httpService;
	}

	public void get(String url, Arg[] inputArgs, Arg[] httpArgs, Object context) {
		setUp();
		service.get(url, inputArgs, httpArgs, this, context);
	}

	public void post(String url, Arg[] postArgs, Arg[] httpArgs, PostData postData, Object context) {
		setUp();
		service.post(url, postArgs, httpArgs, this, postData, context);
	}

	public void put(String url, Arg[] postArgs, Arg[] httpArgs, PostData postData, Object context) {
		setUp();
		service.put(url, postArgs, httpArgs, this, postData, context);
	}

	// sync request
	public Response get(String url, Arg[] inputArgs, Arg[] httpArgs) throws IOException {
		Response response = service.get(url, inputArgs, httpArgs);
		if (isValidResponse(response)) {
			return response;
		} else {
			handleResponseErrors(null, response);
			return null;
		}
	}

	private boolean isValidResponse(Response response) {
		return (response.getException() == null) && (response.getCode() == HttpConnection.HTTP_OK || response.getCode() == HttpConnection.HTTP_CREATED);
	}

	private void handleResponseErrors(Object context, Response response) {
		if (response.getCode() == HttpConnection.HTTP_UNAUTHORIZED || response.getCode() == HttpConnection.HTTP_FORBIDDEN) {
			requestCallBack.onAuthenticationFailure();
			terminateProcess();
		} else if (response.getException() != null) {
			requestCallBack.onRequestFailure(context, response.getException());
		} else if (response.getCode() != HttpConnection.HTTP_OK && response.getCode() != HttpConnection.HTTP_CREATED) {
			requestCallBack.onConnectionProblem();
			terminateProcess();
		}
	}

	public void readProgress(Object context, int bytes, int total) {
		// updateRequestProgress(bytes, total);
	}

	public void writeProgress(Object context, int bytes, int total) {
		// requestCallBack.writeProgress(context, bytes, total);
	}

	public void markProcessComplete() {
		unprocessedRequests = totalRequests = 0;
		requestCallBack.onProcessSuccess();
	}

	public void markProcessFailed() {
		requestCallBack.onProcessFail(null);
	}

	public void markProcessFailed(String failureMessage) {
		requestCallBack.onProcessFail(failureMessage);
	}

	public void terminateProcess() {
		service.cancelRequest();
		unprocessedRequests = totalRequests = 0;
	}

	public RequestCallBack getRequestCallBack() {
		return requestCallBack;
	}

	private void setUp() {
		if (unprocessedRequests == 0 && totalRequests == 0) {
			requestCallBack.onProcessStart();
		}
		unprocessedRequests += 1;
		totalRequests += 1;
	}

	public void setRequestCallBack(RequestCallBack requestCallBack) {
		this.requestCallBack = requestCallBack;
	}

	public void done(Object context, Response response) {
		if (unprocessedRequests > 0) {
			unprocessedRequests--;
		}
		requestCallBack.updateRequestProgress(totalRequests - unprocessedRequests, totalRequests);

		if (isValidResponse(response)) {
			requestCallBack.onRequestSuccess(context, response);
			checkAndMarkProcessComplete();
		} else {
			handleResponseErrors(context, response);
		}
	}

	private void checkAndMarkProcessComplete() {
		if (unprocessedRequests == 0) {
			markProcessComplete();
		}
	}

}
