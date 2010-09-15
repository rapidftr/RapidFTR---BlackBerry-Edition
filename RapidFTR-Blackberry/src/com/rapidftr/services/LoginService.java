package com.rapidftr.services;

import java.util.Hashtable;

import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginService extends RequestAwareService{

	private SettingsStore settingsStore;
	public static final String USER_NAME = "user_name";
	
	public LoginService(HttpService httpService,SettingsStore settingsStore) {
		super(httpService);
		this.settingsStore = settingsStore;
	}

	public void login(String userName, String password) {
		requestHandler.setRequestInProgress();
		Arg[] postArgs = new Arg[] { new Arg("user_name", userName),
				new Arg("password", password) };

		Hashtable context = new Hashtable();
		context.put(USER_NAME, userName);

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		httpService.post("sessions", postArgs, httpArgs, requestHandler, null,
				context);
	}

	private String parseAuthorizationToken(Response response) {
		try {
			return response.getResult().getAsString("session.token");
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from login service in unexpected format");
		}
	}


	public void onRequestSuccess(Object context, Response result) {
		Hashtable table = (Hashtable) context;
		String userName = (String) table.get(USER_NAME);
		settingsStore.setLastUsedUsername(userName);
		settingsStore.setAuthorisationToken(parseAuthorizationToken(result));
		settingsStore.setCurrentlyLoggedIn(userName);
	}

	public void clearLoginState() {
		settingsStore.clearState();
	}
}
