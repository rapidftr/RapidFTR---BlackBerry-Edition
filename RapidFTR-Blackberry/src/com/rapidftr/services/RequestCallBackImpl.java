package com.rapidftr.services;

import com.rapidftr.net.ControllerCallback;
import com.rapidftr.net.ServiceCallback;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.net.ScreenCallBack;
import com.sun.me.web.request.Response;

public class RequestCallBackImpl implements RequestCallBack {
	private ScreenCallBack screenCallback;
	private ServiceCallback serviceCallback;
	private ControllerCallback controllerCallback;

	public RequestCallBackImpl() {

	}

	public void handleConnectionProblem() {
		if (ifScreenCallbackExists()) {
			screenCallback.handleConnectionProblem();
		}
	}

	public void handleException(Exception exception) {
		if (ifScreenCallbackExists()) {
			serviceCallback.onRequestFailure(exception);
		}
	}

	public void handleUnauthorized() {
		if (ifScreenCallbackExists()) {
			screenCallback.handleAuthenticationFailure();
		}
	}

	public void onSuccess(Object context, Response result) {
		if (ifServiceCallbackExists()) {
			serviceCallback.onRequestSuccess(context, result);
		}
	}

	public void writeProgress(Object context, int bytes, int total) {
		// TODO Auto-generated method stub

	}

	public void updateRequestProgress(int size) {
		if (ifScreenCallbackExists()) {
			screenCallback.updateRequestProgress(size);
		}
	}

	public void onProcessComplete() {
		if (ifScreenCallbackExists()) {
			screenCallback.onProcessComplete();
		}
		if (ifControllerCallbackExists()) {
			controllerCallback.afterProcessComplete();
		}
	}

	public void onProcessFail(String failureMessage) {
		if (ifScreenCallbackExists()) {
			screenCallback.onProcessFail(failureMessage);
		}
	}

	public void setScreenCallback(ScreenCallBack screenCallback) {
		this.screenCallback = screenCallback;
	}

	public void setServiceCallback(ServiceCallback serviceCallback) {
		this.serviceCallback = serviceCallback;
	}

	public void setControllerCallback(ControllerCallback controllerCallback) {
		this.controllerCallback = controllerCallback;
	}

	private boolean ifScreenCallbackExists() {
		return screenCallback != null;
	}

	private boolean ifServiceCallbackExists() {
		return serviceCallback != null;
	}

	private boolean ifControllerCallbackExists() {
		return controllerCallback != null;
	}

	public void updateProgressMessage(String msg) {
		screenCallback.setProgressMessage(msg);
		
	}

}
