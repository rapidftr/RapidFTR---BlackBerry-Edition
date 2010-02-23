package com.rapidftr.services.impl;

import java.util.Hashtable;

import com.rapidftr.services.LoginService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.HttpServer;
import com.rapidftr.utilities.Properties;

public class LoginServiceImpl implements LoginService {
	private static final String TEST_USER = "DONAL";

	private static LoginService instance;

	private String loggedInUser;

	public static synchronized LoginService getInstance() {
		if (instance == null) {
			instance = new LoginServiceImpl();
		}

		return instance;
	}

	private LoginServiceImpl() {
	}

	public boolean login(String userName, String password)
			throws ServiceException {
		boolean isValid = (userName.equalsIgnoreCase(TEST_USER));

		if (isValid) {
			loggedInUser = userName.toLowerCase();

			// now, get the Authenticity Token & server-side cookie
			// and save them to the Properties
			try {
				setSessionParameters();
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
		}

		return isValid;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	private void setSessionParameters() throws Exception {
		HttpServer server = HttpServer.getInstance();

		Hashtable sessionParameters = server.getSessionParameters();
		
		Properties properties = Properties.getInstance();
		
		properties.setAuthenticityToken( (String)sessionParameters.get("token"));
		properties.setSessionCookie( (String)sessionParameters.get("cookie"));
	}
}
