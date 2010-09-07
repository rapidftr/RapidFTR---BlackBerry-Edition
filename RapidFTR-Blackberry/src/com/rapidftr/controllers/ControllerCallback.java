package com.rapidftr.controllers;

import com.sun.me.web.request.Response;

public interface ControllerCallback {
	void onRequestFailure(Exception exception);

	void onRequestSuccess(Object context, Response result);


}
