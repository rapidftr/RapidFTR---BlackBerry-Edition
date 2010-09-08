package com.rapidftr.net;

public interface ScreenCallBack {

	void handleConnectionProblem();

	void handleAuthenticationFailure();

	void updateRequestProgress(int progress);

	void onProcessComplete();

	void onProcessFail();

	void setProgressMessage(String message);
}
