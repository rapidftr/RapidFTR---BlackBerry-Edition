package com.rapidftr.services.impl;

import com.rapidftr.services.LoginService;

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

	public boolean login(String userName, String password) {
		boolean isValid = (userName.equalsIgnoreCase(TEST_USER));
		
		if ( isValid ) {
			loggedInUser = userName.toLowerCase();
		}
		
		return isValid;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}
}
