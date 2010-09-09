package com.rapidftr.net;

import javax.microedition.io.HttpConnection;

import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class HttpRequestHandler implements RequestListener {

	RequestCallBack requestCallBack;
	protected boolean requestInProgress;

	public HttpRequestHandler(RequestCallBack requestCallBack) {
		super();
		this.requestCallBack = requestCallBack;
	}

	public void done(Object context, Response result) throws Exception {
		
		if (!requestInProgress)
			return;
		requestInProgress = false;
		if (result.getException() != null) {
			requestCallBack.handleException(result.getException());
		} else if (result.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
			requestCallBack.handleUnauthorized();
		} else if (result.getCode() != HttpConnection.HTTP_OK && result.getCode() != HttpConnection.HTTP_CREATED ) {
			requestCallBack.handleConnectionProblem();
			return;
		} else {
			requestCallBack.onSuccess(context, result);
		}
	}

	public void readProgress(Object context, int bytes, int total) {
		updateRequestProgress(bytes, total);
	}

	public void writeProgress(Object context, int bytes, int total) {
		requestCallBack.writeProgress(context, bytes, total);

	}

	public void updateRequestProgress(int bytes, int total) {
		double size = ((double) bytes) / total;
		requestCallBack.updateRequestProgress((int) (size * 100));
	}
	
	public void markProcessComplete() {
		requestCallBack.onProcessComplete();
	}
	
	public void markProcessFailed() {
		requestCallBack.onProcessFail();
	}

	public boolean isRequestInProgress() {
		return requestInProgress;
	}

	public void setRequestInProgress() {
		this.requestInProgress = true;
	}

	public void cancelRequestInProgress() {
		this.requestInProgress = false;
	}

	public boolean checkIfRequestNotInProgress() {
		if (requestInProgress) {
			requestInProgress = false;
			return true;
		} else {
			return false;
		}
	}

}
