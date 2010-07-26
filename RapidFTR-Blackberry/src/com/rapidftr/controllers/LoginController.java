package com.rapidftr.controllers;

import java.util.Hashtable;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.LoginServiceListener;
import com.rapidftr.utilities.SettingsStore;

public class LoginController extends Controller implements LoginServiceListener {

	public static final String USER_NAME = "user_name";
	public static final String PASSWORD = "password";
	public static final String CALLER = "caller";

	private final LoginService loginService;
	private final SettingsStore settingsStore;
	
	private boolean requestInProgress = false;  // Needed to Synchronize between ui thread and request thread

	public LoginController(LoginScreen screen, UiStack uiStack,
			LoginService loginService, SettingsStore settingsStore) {
		super(screen, uiStack);
		this.loginService = loginService;
		loginService.setListener(this);
		this.settingsStore = settingsStore;
	}

	public void login(String userName, String password) {
		requestInProgress = true;
		((LoginScreen) screen).setProgressMsg("Signing In ...");
		loginService.login(userName, password);
	}


	public void loginCancelled() {
		requestInProgress = false;
		loginService.cancelLogin();
	}

	public void onLoginSucees(Object context, String authorizationToken) {
		if(!requestInProgress)
			return;
		
		requestInProgress = false;
		
		Hashtable table = (Hashtable) context;
		String userName = (String) table.get(USER_NAME);
		settingsStore.setLastUsedUsername(userName);
		settingsStore.setAuthorisationToken(authorizationToken);
		settingsStore.setCurrentlyLoggedIn(userName);

		// ((LoginScreen) screen).updateScreen("Login Success");

		popScreen();

	}
	public void onAuthenticationFailure() {
		if(!requestInProgress)
			return;
		requestInProgress = false;
		settingsStore.setAuthorisationToken("invalid token");
		((LoginScreen) screen).onLoginFailed();
		
	}

	public void onConnectionProblem() {

		if(!requestInProgress)
			return;
		requestInProgress = false;
		((LoginScreen) screen).onConnectionProblem();
	}

}
