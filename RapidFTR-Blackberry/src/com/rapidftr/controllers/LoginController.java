package com.rapidftr.controllers;

import net.rim.device.api.system.GPRSInfo;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.controllers.internal.RequestAwareController;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.process.Process;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.utilities.Constants;

public class LoginController extends RequestAwareController {

    Process callingProcess;
    private ConnectionFactory connectionFactory;
    private final String OFFLINE_LOGIN_ERROR_MESSAGE = "You are working offline. You must authenticate with your credentials from the last successful online log in.";
    private boolean isPasswordRecoveryClicked = false;
    
    public LoginController(LoginScreen screen, UiStack uiStack,
                           LoginService loginService,
                           ConnectionFactory connectionFactory,
                           Dispatcher dispatcher) {
        super(screen, uiStack, loginService, dispatcher);
        this.connectionFactory = connectionFactory;
    }

    public void login(String userName, String password) {
        getScreenCallBack().setProgressMessage("Signing In ...");
        LoginService loginService = (LoginService) service;
        if (connectionFactory.isNotConnected()) {
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

    public void requestPasswordRecovery(String userName){
    	isPasswordRecoveryClicked = true;
    	getScreenCallBack().setProgressMessage("Requesting Password recovery ...");
    	LoginService loginService = (LoginService)service;
    	loginService.recoverPassword(userName, GPRSInfo.imeiToString(GPRSInfo.getIMEI()));
    	
    }
    public void onProcessComplete(boolean status) {
        if (!isPasswordRecoveryClicked && status) {
            popScreen();
            if (callingProcess != null) {
                callingProcess.startProcess();
            }
        }
        isPasswordRecoveryClicked = false;
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
