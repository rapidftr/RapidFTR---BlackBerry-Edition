package com.rapidftr.controllers;

public interface ScreenCallBack {

	void handleConnectionProblem();

	void handleAuthenticationFailure();

	void updateRequestProgress(int progress);

}
