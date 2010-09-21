package com.rapidftr.net;

import com.sun.me.web.request.Response;

public interface RequestCallBack {
	void onAuthenticationFailure();

	void onConnectionProblem();

	void onRequestException(Object context, Exception exception);

	void onRequestComplete(Object context, Response result);

	void updateProgressMessage(String msg);

	void onProcessComplete();

	void onProcessFail();

	void updateRequestProgress(int finished, int totalRequests);
}
