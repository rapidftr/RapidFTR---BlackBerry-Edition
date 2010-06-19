package com.rapidftr.controllers;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;

public class LoginController {
	private final LoginScreen screen;
	private final UiStack uiStack;
	
	public LoginController(LoginScreen screen, UiStack uiStack){
		this.screen = screen;
		this.uiStack = uiStack;
	}
	
	public void show() {
		uiStack.pushScreen(screen);
		
	}
}
