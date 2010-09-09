package com.rapidftr.controllers;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.ScreenCallBack;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.RequestCallBackImpl;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;

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
		screenCallBack = loginController.screenCallBack;
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
		loginController.login("abcd", "abcd"); // Just to make the
												// requestInProgress flag to
												// set

		screenCallBack.handleConnectionProblem();
		verify(loginScreen).handleConnectionProblem();
	}

	@Test
	public void shouldUpdateTheScreenWithLoginFailedErrorMessage() {
		loginController.login("abcd", "abcd"); // Just to make the
												// isRequestInProgress flag to
												// set

		screenCallBack.handleAuthenticationFailure();
		verify(loginScreen).handleAuthenticationFailure();
	}

}
