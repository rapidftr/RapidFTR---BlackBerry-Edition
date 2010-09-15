package com.rapidftr.controllers;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.LoginService;

public class LoginController extends RequestAwareController {

	public LoginController(LoginScreen screen, UiStack uiStack,
			LoginService loginService) {
		super(screen, uiStack,loginService);
	}

	public void login(String userName, String password) {
		screenCallBack.setProgressMessage("Signing In ...");
		((LoginService)service).login(userName, password);
	}

	public void loginCancelled() {
		service.cancelRequest();
	}

	public void afterProcessComplete() {
		popScreen();
	}

	public void clearLoginState() {
		((LoginService)service).clearLoginState();
	}

}
