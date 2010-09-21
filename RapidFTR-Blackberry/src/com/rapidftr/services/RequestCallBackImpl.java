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

	public void onRequestComplete(Object context, Response result) {
		if (ifServiceCallbackExists()) {
			serviceCallback.onRequestSuccess(context, result);
		}
	}



	public void onProcessComplete() {
		if (ifScreenCallbackExists()) {
			screenCallback.onProcessComplete();
		}
		if (ifControllerCallbackExists()) {
			controllerCallback.onProcessComplete();
		}
	}

	public void onProcessFail() {
		if (ifScreenCallbackExists()) {
			screenCallback.onProcessFail();
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

	public void onRequestException(Object context, Exception exception) {
		if (ifScreenCallbackExists()) {
			serviceCallback.onRequestFailure(context,exception);
		}
	}

	public void updateRequestProgress(int finished, int totalRequests) {
		if (ifScreenCallbackExists()) {
			screenCallback.updateProgress((int) (((double) finished) / totalRequests * 100));
		}
	}

}
