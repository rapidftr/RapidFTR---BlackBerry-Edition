package com.rapidftr.services;

import com.rapidftr.net.RequestCallBack;
import com.sun.me.web.request.Response;

public class RequestCallBackImpl implements RequestCallBack {
	private ScreenCallBack screenCallback;
	private ServiceCallback serviceCallback;
	private ControllerCallback controllerCallback;

	public RequestCallBackImpl() {

	}

	public void onConnectionProblem() {
		if (ifScreenCallbackExists()) {
			screenCallback.onConnectionProblem();
		}
	}


	public void onAuthenticationFailure() {
		if (ifScreenCallbackExists()) {
			screenCallback.onAuthenticationFailure();
		}
	}

	public void onRequestSuccess(Object context, Response result) {
		if (ifServiceCallbackExists()) {
			serviceCallback.onRequestSuccess(context, result);
		}
	}



	public void onProcessSuccess() {
		if (ifScreenCallbackExists()) {
			screenCallback.onProcessSuccess();
		}
		if (ifControllerCallbackExists()) {
			controllerCallback.onProcessComplete();
		}
	}

	public void onProcessFail(String failureMessage) {
		if (ifScreenCallbackExists()) {
			screenCallback.onProcessFail(failureMessage);
		}
		if (ifControllerCallbackExists()) {
			controllerCallback.onProcessComplete();
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

	public void onRequestFailure(Object context, Exception exception) {
		if (ifScreenCallbackExists()) {
			serviceCallback.onRequestFailure(context,exception);
		}
	}

	public void updateRequestProgress(int finished, int totalRequests) {
		if (ifScreenCallbackExists()) {
			screenCallback.updateProgress((int) (((double) finished) / totalRequests * 100));
		}
	}

	public void onProcessStart() {
		if (ifControllerCallbackExists()) {
			controllerCallback.beforeProcessStart();
		}
	}

}
