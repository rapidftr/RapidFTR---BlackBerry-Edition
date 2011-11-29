package com.rapidftr.net;

import com.sun.me.web.request.Response;

public interface RequestCallBack {
	
	void onAuthenticationFailure();

	void onConnectionProblem();

	void onRequestFailure(Object context, Exception exception);

	void onRequestSuccess(Object context, Response result);

	void updateProgressMessage(String msg);
	
	void updateRequestProgress(int finished, int totalRequests);

	void onProcessStart();

	void onProcessSuccess(String successMessage);
	
	void onProcessFail(String failureMessage);


}
