package com.rapidftr.controllers;


import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.ScreenCallBack;

import java.io.IOException;

public class LoginControllerTest {
	private LoginService loginService;
	private LoginScreen loginScreen;
	private UiStack uiStack;
	private LoginController loginController;
	private ScreenCallBack screenCallBack;
    private ConnectionFactory connectionFactory;
    private Dispatcher dispatcher;

    
	@Before
	public void setup() {
		this.loginService = mock(LoginService.class);
		loginScreen = mock(LoginScreen.class);
		uiStack = mock(UiStack.class);
        connectionFactory = mock(ConnectionFactory.class);
        dispatcher = mock(Dispatcher.class);
        connectionFactory = mock(ConnectionFactory.class);
        when(connectionFactory.isNotConnected()).thenReturn(false);
		loginController = new LoginController(loginScreen, uiStack,
				loginService, connectionFactory, dispatcher);
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

        whenUserIsConnected();
		loginController.login(userName, password);
		verify(loginService).login(userName, password);
	}

	@Test
	public void shouldUpdateTheScreenWithConnectionProblemErrorMessage() {
        whenUserIsConnected();

		loginController.login("abcd", "abcd");
		screenCallBack.onConnectionProblem();
		verify(loginScreen).onConnectionProblem();
	}

    @Test
	public void shouldUpdateTheScreenWithLoginFailedErrorMessage() {
        whenUserIsConnected();

		loginController.login("abcd", "abcd");
		screenCallBack.onAuthenticationFailure();
		verify(loginScreen).onAuthenticationFailure();
	}

    @Test
    public void shouldAttemptOfflineLoginWhenThereIsNoConnection() {
        String userName = "user";
        String password = "password";

        whenThereIsNoConnection();

        loginController.login(userName, password);

        verify(loginService).offlineLogin(userName, password);
}

    @Test
    public void shouldClearLoginScreenAndGoToHomeScreenOnSucessfullOfflineLogin() {
        whenThereIsNoConnection();
        when(loginService.offlineLogin(anyString(), anyString())).thenReturn(true);

        loginController.login("user", "password");

        verify(uiStack).clear();
        verify(loginScreen).resetCredentials(true);
        verify(dispatcher).homeScreen();
    }

    @Test
    public void shouldShowOfflineLoginErrorMessageOnFailedOfflineLogin() {
        whenThereIsNoConnection();
        when(loginService.offlineLogin(anyString(), anyString())).thenReturn(false);

        loginController.login("user", "password");

        verify(loginScreen).onProcessFail("You are working offline. You must authenticate with your credentials from the last successful online log in.");
    }

    private void whenThereIsNoConnection() {
        when(connectionFactory.isNotConnected()).thenReturn(true);
    }

    private void whenUserIsConnected() {
        when(connectionFactory.isNotConnected()).thenReturn(false);
    }
}
