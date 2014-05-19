package com.rapidftr.net;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import com.rapidftr.services.ServiceException;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpBatchRequestHandler implements RequestListener {

	private RequestCallBack requestCallBack;
	private int unprocessedRequests = 0;
	private int totalRequests = 0;
	private boolean failedRequest = false;
	private boolean processCompleted = false;
	private HttpService service;
    private ResponseErrors errors;
    private static final String PASSWORD_RECOVERY = "password_recovery_requests";

    public HttpBatchRequestHandler(HttpService httpService) {
		service = httpService;
	}

    public synchronized void startNewProcess(int total) {
        unprocessedRequests = totalRequests = total;
		failedRequest = false;
		processCompleted = false;
        errors = new ResponseErrors();  
    }
    
	public synchronized void startNewProcess() {
        startNewProcess(1);
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

			Response response = service.get(url, inputArgs, httpArgs);
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

	private synchronized void handleResponseErrors(Object context, Response response) {
		failedRequest = true;
        errors.add(response);
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

	public void markProcessComplete(Object context, Response response) {
		markRequestsAsCompleted();
		String responseMessage = null;
		try {
			if(response != null && context !=null && ((Hashtable) context).get(PASSWORD_RECOVERY) != null)
				responseMessage = response.getResult().getAsString("response");
		} catch (ResultException e) {
			throw new ServiceException("JSON returned from password recovery do not have key 'result'");
		}
		String message = (response != null && responseMessage!=null)? responseMessage: ""; 
		requestCallBack.onProcessSuccess(message);
	}

	private void markRequestsAsCompleted() {
		unprocessedRequests = totalRequests = 0;
	}

	public void markProcessFailed() {
		requestCallBack.onProcessFail(getErrorMessage());
	}

    private String getErrorMessage() {
        return errors.getMessage();
    }

    public void markProcessFailed(String failureMessage) {
		terminateProcess();
		requestCallBack.onProcessFail(failureMessage);
	}
    
    public void markProcessSuccess(String successMessage){
    	requestCallBack.onProcessSuccess(successMessage);
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
		cleanUp(context, response);

	}

	private synchronized void cleanUp(Object context, Response response) {
		if (unprocessedRequests > 0) {
			unprocessedRequests--;
		}
		checkForProcessCompletion(context, response);
	}

	public synchronized void checkForProcessCompletion(Object context, Response response) {
		if (unprocessedRequests == 0 && ! processCompleted) {
			processCompleted = true;
			if (failedRequest) {
				markProcessFailed();
			} else {
				markProcessComplete(context, response);
			}
		}
	}

    public ResponseErrors getErrors() {
        return errors;
    }

}
