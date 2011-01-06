package com.rapidftr.services;

import com.rapidftr.net.HttpBatchRequestHandler;
import com.rapidftr.net.HttpService;

public abstract class RequestAwareService implements ServiceCallback {
	HttpBatchRequestHandler requestHandler;

	RequestAwareService(HttpService httpService) {
		RequestCallBackImpl requestCallBack = new RequestCallBackImpl();
		requestCallBack.setServiceCallback(this);
		requestHandler = new HttpBatchRequestHandler(httpService);
		requestHandler.setRequestCallBack(requestCallBack);
	}


	public HttpBatchRequestHandler getRequestHandler() {
		return requestHandler;
	}

	public void cancelRequest() {
		requestHandler.terminateProcess();
	}
}
