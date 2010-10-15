package com.rapidftr.controllers;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.ScreenCallBack;

public class LoginControllerTest {
	private LoginService loginService;
	private LoginScreen loginScreen;
	private UiStack uiStack;
	private LoginController loginController;
	private ScreenCallBack screenCallBack;
	@Before
	public void setup() {
		this.loginService = mock(LoginService.class);
		loginScreen = mock(LoginScreen.class);
		uiStack = mock(UiStack.class);
		loginController = new LoginController(loginScreen, uiStack,
				loginService);
		screenCallBack = loginController.getScreenCallBack();
	}

	@Test
	public void shouldLoginFromFormService() throws IOException {
		loginController.login("rapidftr", "rapidftr");
		verify(loginService).login("rapidftr", "rapidftr");
	}

	@Test
	public void shouldCancelLoginRequest() {
		loginController.loginCancelled();
		verify(loginService).cancelRequest();
	}
	
	@Test
	public void shouldAttemptLoginOverHttpProviderWithGivenCredentials()
			throws Exception {
		String userName = "zskjh";
		String password = "ksdhfkl";
		loginController.login(userName, password);
		verify(loginService).login(userName, password);
	}


	@Test
	public void shouldUpdateTheScreenWithConnectionProblemErrorMessage() {
		loginController.login("abcd", "abcd"); 
		screenCallBack.onConnectionProblem();
		verify(loginScreen).onConnectionProblem();
	}

	@Test
	public void shouldUpdateTheScreenWithLoginFailedErrorMessage() {
		loginController.login("abcd", "abcd"); 
		screenCallBack.onAuthenticationFailure();
		verify(loginScreen).onAuthenticationFailure();
	}

}
