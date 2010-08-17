package com.rapidftr.net;

public interface HttpServiceListener {

	void onConnectionProblem();
	void onAuthenticationFailure();


}
