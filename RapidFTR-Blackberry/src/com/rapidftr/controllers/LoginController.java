package com.rapidftr.controllers;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.LoginFailedException;
import com.rapidftr.services.LoginService;
import com.rapidftr.utilities.SettingsStore;

public class LoginController {
	private final LoginScreen screen;
	private final UiStack uiStack;
    private final LoginService loginService;
    private final SettingsStore settingsStore;

    public LoginController(LoginScreen screen, UiStack uiStack, LoginService loginService, SettingsStore settingsStore){
		this.screen = screen;
		this.uiStack = uiStack;
        this.loginService = loginService;
        this.settingsStore = settingsStore;
        screen.setLoginController(this);
	}
	
	public void show() {
		uiStack.pushScreen(screen);
		
	}

    public void login(String userName, String password) {
        try {
            String authorisationToken = loginService.login(userName, password);
            settingsStore.setLastUsedUsername(userName);
            settingsStore.setAuthorisationToken(authorisationToken);
        } catch (LoginFailedException loginFailed) {
            screen.loginFailed();
        }
    }
}
