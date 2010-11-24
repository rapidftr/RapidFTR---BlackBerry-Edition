package com.rapidftr.services;

import java.util.Hashtable;

import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.Settings;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginService extends RequestAwareService {
	private Settings settings;
	public static final String USER_NAME = "user_name";

	public LoginService(HttpService httpService, Settings settings) {
		super(httpService);
		this.settings = settings;
	}

    public void login(String userName, String password) {
        Arg[] postArgs = new Arg[]{new Arg("user_name", userName),
                new Arg("password", password)};

        if (settings.isOfflineLogin() && settings.isAuthorised()
                && userName.equals(settings.getLastUserLoginName())
                && password.equals(settings.getLastUsedPassword())) {
            
        } else {

            Hashtable context = new Hashtable();
            context.put(USER_NAME, userName);

            requestHandler.startNewProcess();
            requestHandler.post("sessions", postArgs, HttpUtility.makeJSONHeader(),
                    null, context);
        }
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
		settings.clear();
	}

	public void onRequestSuccess(Object context, Response result) {
		Hashtable table = (Hashtable) context;
		settings.setAuthorisationToken(parseAuthorizationToken(result));
		settings.setCurrentlyLoggedIn((String) table.get(USER_NAME));
	}

	public void onRequestFailure(Object context, Exception exception) {
		requestHandler.markProcessFailed(" Login Failed Due to "
				+ exception.getMessage()+". ");
	}

}
