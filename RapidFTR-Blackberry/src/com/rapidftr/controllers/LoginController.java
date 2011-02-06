package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.RequestAwareController;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.process.Process;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.LoginService;

public class LoginController extends RequestAwareController {

	Process callingProcess;
    private final String OFFLINE_LOGIN_ERROR_MESSAGE = "You are working offline. You must authenticate with your credentials from the last successful online log in.";

    public LoginController(LoginScreen screen, UiStack uiStack,
			LoginService loginService) {
		super(screen, uiStack, loginService);
	}

    public void login(String userName, String password) {
        getScreenCallBack().setProgressMessage("Signing In ...");
        LoginService loginService = (LoginService) service;
        if (ConnectionFactory.isNotConnected()) {
            if (loginService.offlineLogin(userName, password)) {
                homeScreen();
                clearLoginScreen();
            } else {
                ((LoginScreen) currentScreen).onProcessFail(OFFLINE_LOGIN_ERROR_MESSAGE);
            }

        } else {
            loginService.login(userName, password);
        }
    }

	public void onProcessComplete(boolean status) {
		if (status) {
			popScreen();
			if (callingProcess != null) {
				callingProcess.startProcess();
			}
		}
	}

	public void showLoginScreen(Process callingProcess) {
		this.callingProcess = callingProcess;
		show();
	}

	public void homeScreen() {
		uiStack.clear();
		dispatcher.homeScreen();
	}

	public void clearLoginScreen() {
		((LoginScreen) currentScreen).resetCredentials(true);
	}

    public void synchronizeForms() {
        dispatcher.synchronizeForms();
    }

}
