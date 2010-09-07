package com.rapidftr.services;

import java.util.Hashtable;

import com.rapidftr.controllers.LoginController;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;

public class LoginService {

	private final HttpService httpService;
	private RequestListener requestListener;

	public LoginService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void login(String userName, String password) {
		Arg[] postArgs = new Arg[] { new Arg("user_name", userName),
				new Arg("password", password) };

		Hashtable context = new Hashtable();
		context.put(LoginController.USER_NAME, userName);

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		httpService.post("sessions", postArgs, httpArgs, requestListener, null,
				context);
	}

	public void cancelLogin() {
		httpService.cancelRequest();
	}

	public void setListener(RequestListener requestListener) {
		this.requestListener = requestListener;
	}

}
