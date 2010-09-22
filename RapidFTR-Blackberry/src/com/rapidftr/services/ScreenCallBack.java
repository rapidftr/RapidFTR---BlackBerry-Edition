package com.rapidftr.services;

public interface ScreenCallBack {

	void onConnectionProblem();

	void onAuthenticationFailure();

	void onProcessSuccess();

	void onProcessFail(String failureMessage);

	void updateProgress(int progress);

	void setProgressMessage(String message);
}
