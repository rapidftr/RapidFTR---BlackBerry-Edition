package com.rapidftr.services;

import com.rapidftr.net.HttpServiceListener;

public interface LoginServiceListener extends HttpServiceListener {

	void onLoginSucees(Object context, String authorizationToken);

}
