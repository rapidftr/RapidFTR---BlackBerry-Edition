package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.RequestAwareController;
import com.rapidftr.process.Process;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.LoginService;

public class LoginController extends RequestAwareController {

	Process callingProcess ;
	public LoginController(LoginScreen screen, UiStack uiStack,
			LoginService loginService) {
		super(screen, uiStack,loginService);
	}

	public void login(String userName, String password) {
		getScreenCallBack().setProgressMessage("Signing In ...");
		((LoginService)service).login(userName, password);
	}

	public void loginCancelled() {
		service.cancelRequest();
	}

	public void onProcessComplete() {
		popScreen();
		if(callingProcess!=null){
			callingProcess.startProcess();
		}
	}

	public void showLoginScreen(Process callingProcess) {
		this.callingProcess = callingProcess;	
		show();		
	}

}
