package com.rapidftr.services;

import java.util.Hashtable;

import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.Settings;
import com.rapidftr.utilities.DefaultStore;
import com.rapidftr.utilities.Store;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginService extends RequestAwareService {

	private static final String PORT = "port";
	private static final String HOST = "host";
	private Settings settings;
	private Store store = new DefaultStore();
	public static final String USER_NAME = "user_name";

	public LoginService(HttpService httpService, Settings settingsStore) {
		super(httpService);
		this.settings = settingsStore;
	}

	public void login(String userName, String password, String host, String port) {
		Arg[] postArgs = new Arg[] { new Arg("user_name", userName),
				new Arg("password", password) };

		Hashtable context = new Hashtable();
		context.put(USER_NAME, userName);
		context.put(HOST, host);
		context.put(PORT, port);

		requestHandler.startNewProcess();
		requestHandler.post("sessions", postArgs, HttpUtility.makeJSONHeader(),
				null, context);
	}

	private String parseAuthorizationToken(Response response) {
		try {
			return response.getResult().getAsString("session.token");
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from login service in unexpected format");
		}
	}

	public void clearLoginState() {
		store.clear();
	}

	public void onRequestSuccess(Object context, Response result) {
		Hashtable table = (Hashtable) context;
		settings.setLastUsedUsername((String) table.get(USER_NAME));
		settings.setLastHost((String)table.get(HOST));
		settings.setLastPort((String)table.get(PORT) );
		settings.setAuthorisationToken(parseAuthorizationToken(result));
		settings.setCurrentlyLoggedIn((String) table.get(USER_NAME));
	}

	public void onRequestFailure(Object context, Exception exception) {
		requestHandler.markProcessFailed(" Login Failed Due to "
				+ exception.getMessage()+". ");
	}

	public void login(Credential credentials) {
		if(credentials.isOffline()){
			
		}
	}

}
