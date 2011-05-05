package com.rapidftr.controllers;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.ConnectionFactory;
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
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        when(connectionFactory.isNotConnected()).thenReturn(false);
		loginController = new LoginController(loginScreen, uiStack,
				loginService, connectionFactory);
		screenCallBack = loginController.getScreenCallBack();
	}

	@Test
	public void shouldLoginFromFormService() throws IOException {
		loginController.login("rapidftr", "rapidftr");
		verify(loginService).login("rapidftr", "rapidftr");
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
