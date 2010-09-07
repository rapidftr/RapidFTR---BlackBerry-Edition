package com.rapidftr.controllers;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.RequestCallBackImpl;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Response;

import java.util.Hashtable;

public class LoginController extends Controller implements ControllerCallback {

	public static final String USER_NAME = "user_name";

	private final LoginService loginService;
	private final SettingsStore settingsStore;
	private final HttpRequestHandler listener;

	public LoginController(LoginScreen screen, UiStack uiStack,
			LoginService loginService, SettingsStore settingsStore) {
		super(screen, uiStack);
		this.loginService = loginService;
		RequestCallBackImpl callback = new RequestCallBackImpl(screen,this);
		listener = new HttpRequestHandler(callback);
		loginService.setListener(listener);
		this.settingsStore = settingsStore;
	}

	public void login(String userName, String password) {
		// requestInProgress = true;
		listener.setRequestInProgress();
		((LoginScreen) screen).setProgressMsg("Signing In ...");
		loginService.login(userName, password);
	}

	public void loginCancelled() {
		listener.cancelRequestInProgress();
		// requestInProgress = false;
		loginService.cancelLogin();
	}

	private String parseAuthorizationToken(Response response) {
		try {
			return response.getResult().getAsString("session.token");
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from login service in unexpected format");
		}
	}

	public void onRequestFailure(Exception exception) {
		// TODO Auto-generated method stub

	}

	public void onRequestSuccess(Object context, Response result) {

		Hashtable table = (Hashtable) context;
		String userName = (String) table.get(USER_NAME);
		settingsStore.setLastUsedUsername(userName);
		settingsStore.setAuthorisationToken(parseAuthorizationToken(result));
		settingsStore.setCurrentlyLoggedIn(userName);
		popScreen();

	}
}
