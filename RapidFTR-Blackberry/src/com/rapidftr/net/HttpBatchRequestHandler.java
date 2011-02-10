package com.rapidftr.net;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpBatchRequestHandler implements RequestListener {

	RequestCallBack requestCallBack;
	private int unprocessedRequests = 0;
	private int totalRequests = 0;
	private boolean failedRequest = false;
	private boolean processCompleted = false;
	private HttpService service;

	public HttpBatchRequestHandler(HttpService httpService) {
		service = httpService;
	}

	public void startNewProcess() {
		// terminateProcess();
		// TODO create new thread pool for every process
		unprocessedRequests = totalRequests = 0;
		failedRequest = false;
		processCompleted = false;
	}

	public void get(String url, Arg[] inputArgs, Arg[] httpArgs, Object context) {
		setUp();
		service.get(url, inputArgs, httpArgs, this, context);
	}

	public void post(String url, Arg[] postArgs, Arg[] httpArgs,
			PostData postData, Object context) {
		setUp();
		service.post(url, postArgs, httpArgs, this, postData, context);
	}

	public void put(String url, Arg[] postArgs, Arg[] httpArgs,
			PostData postData, Object context) {
		setUp();
		service.put(url, postArgs, httpArgs, this, postData, context);
	}

	// sync request
	public Response get(String url, Arg[] inputArgs, Arg[] httpArgs) throws IOException {
		Response response = null;

			response = service.get(url, inputArgs, httpArgs);
			if (!isValidResponse(response)) {
				handleResponseErrors(null, response);
				return null;
			}
		
		return response;

	}

	private boolean isValidResponse(Response response) {
		return (response.getException() == null)
				&& (response.getCode() == HttpConnection.HTTP_OK || response
						.getCode() == HttpConnection.HTTP_CREATED);
	}

	private void handleResponseErrors(Object context, Response response) {
		failedRequest = true;

		if (response.getCode() == HttpConnection.HTTP_UNAUTHORIZED
				|| response.getCode() == HttpConnection.HTTP_FORBIDDEN) {
			requestCallBack.onAuthenticationFailure();
            terminateProcessWhenHttpCodeIsNotExpected();
		} else if (response.getException() != null) {
			if (response.getException() instanceof ResultException) {
				requestCallBack.onProcessFail("Please check the URL");
			} else {
				requestCallBack.onRequestFailure(context, response
						.getException());
			}

		} else if (response.getCode() == HttpConnection.HTTP_NOT_ACCEPTABLE){
            requestCallBack.onProcessFail("The format of data is not acceptable. Please check the record.");
            terminateProcessWhenHttpCodeIsNotExpected();
        } else if (response.getCode() != HttpConnection.HTTP_OK
				&& response.getCode() != HttpConnection.HTTP_CREATED) {
			requestCallBack.onConnectionProblem();
            terminateProcessWhenHttpCodeIsNotExpected();
		}
	}

    private void terminateProcessWhenHttpCodeIsNotExpected() {
        terminateProcess();
        processCompleted = true;
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
		terminateProcess();
		requestCallBack.onProcessFail(failureMessage);
	}

	public void terminateProcess() {
		service.cancelRequest();
		totalRequests = 0;
	}

	public RequestCallBack getRequestCallBack() {
		return requestCallBack;
	}

	private void setUp() {
		if (unprocessedRequests == 0 && totalRequests == 0) {
			requestCallBack.onProcessStart();
			failedRequest = false;
		}
		unprocessedRequests += 1;
		totalRequests += 1;
	}

	public void setRequestCallBack(RequestCallBack requestCallBack) {
		this.requestCallBack = requestCallBack;
	}

	public void done(Object context, Response response) {

		requestCallBack.updateRequestProgress(totalRequests
				- unprocessedRequests, totalRequests);

		if (isValidResponse(response)) {
			requestCallBack.onRequestSuccess(context, response);
		} else {
			handleResponseErrors(context, response);
		}
		cleanUp();

	}

	private synchronized void cleanUp() {
		if (unprocessedRequests > 0) {
			unprocessedRequests--;
		}
		checkForProcessCompletion();
	}

	public synchronized void checkForProcessCompletion() {
		if (unprocessedRequests == 0 && ! processCompleted) {
			processCompleted = true;
			if (failedRequest) {
				markProcessFailed();
			} else {
				markProcessComplete();
			}
		}
	}

}
