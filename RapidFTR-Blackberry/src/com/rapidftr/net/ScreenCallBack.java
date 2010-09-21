package com.rapidftr.net;

public interface ScreenCallBack {

	void onConnectionProblem();

	void onAuthenticationFailure();

	void onProcessStart();
	
	void onProcessComplete();

	void onProcessFail(String failureMessage);

	void updateProgress(int progress);

	void setProgressMessage(String message);
}
