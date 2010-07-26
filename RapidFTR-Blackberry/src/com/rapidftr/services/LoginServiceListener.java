package com.rapidftr.services;

import java.util.Hashtable;

import com.rapidftr.net.HttpServiceListener;

public interface LoginServiceListener extends HttpServiceListener {

	void onLoginSucees(Object context, String authorizationToken);

}
