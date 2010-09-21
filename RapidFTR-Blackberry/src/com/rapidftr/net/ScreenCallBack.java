package com.rapidftr.net;

public interface ScreenCallBack {

	void onConnectionProblem();

	void onAuthenticationFailure();

	void onProcessComplete();

	void onProcessFail();

	void updateProgress(int progress);

	void setProgressMessage(String message);
}
