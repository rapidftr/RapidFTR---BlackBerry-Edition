package com.rapidftr.net;

import com.sun.me.web.request.Response;

public interface RequestCallBack {

	void handleUnauthorized();

	void handleException(Exception exception);

	void handleConnectionProblem();

	void onSuccess(Object context, Response result);

	void updateRequestProgress(int size);
	
	void updateProgressMessage(String msg);

	void writeProgress(Object context, int bytes, int total);

	void onProcessComplete();

	void onProcessFail(String failureMessage);

}
