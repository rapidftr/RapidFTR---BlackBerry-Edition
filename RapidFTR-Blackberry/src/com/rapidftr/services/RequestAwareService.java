package com.rapidftr.services;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.HttpService;
import com.rapidftr.net.ServiceCallback;

public abstract class RequestAwareService implements ServiceCallback {
	//protected HttpService httpService;
	HttpRequestHandler requestHandler;

	RequestAwareService(HttpService httpService) {
		RequestCallBackImpl requestCallBack = new RequestCallBackImpl();
		requestCallBack.setServiceCallback(this);
		requestHandler = new HttpRequestHandler(httpService);
		requestHandler.setRequestCallBack(requestCallBack);
	}


	public HttpRequestHandler getRequestHandler() {
		return requestHandler;
	}

	public void cancelRequest() {
		requestHandler.cancelRequestInProgress();
		//httpService.cancelRequest();
	}

	public void onRequestFailure(Exception exception) {
		// requestHandler.markProcessFailed();
	}

}
