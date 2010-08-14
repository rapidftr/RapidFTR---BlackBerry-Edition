package com.rapidftr.services;

import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import com.rapidftr.controllers.LoginController;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class LoginService implements RequestListener {

	private final HttpService httpService;
	private LoginServiceListener listener;

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
		httpService.post("sessions", postArgs,httpArgs, this, null, context);
	}

	public void done(Object context, Response result) throws Exception {

		// HttpServer.printResponse(result);

		if (result.getException() != null) {
			listener.onConnectionProblem();
			return;
		}

		if (result.getCode() != HttpConnection.HTTP_CREATED) {
			listener.onAuthenticationFailure();
			return;
		}

		listener.onLoginSucees(context, parseAuthorizationToken(result));

	}

	public void readProgress(Object context, int bytes, int total) {

	}

	public void writeProgress(Object context, int bytes, int total) {

	}

	public void cancelLogin() {

		httpService.cancelRequest();
	}

	public void setListener(LoginServiceListener listener) {
		this.listener = listener;

	}

	private String parseAuthorizationToken(Response response) {
		try {
			return response.getResult().getAsString("session.token");
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from login service in unexpected format");
		}
	}

}
