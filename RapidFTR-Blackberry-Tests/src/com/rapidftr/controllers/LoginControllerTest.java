package com.rapidftr.controllers;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpRequestHandler;
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
	private SettingsStore settingsStore;
	private LoginController loginController;
	private  HttpRequestHandler listener;
	private RequestCallBackImpl requestCallback;
	@Before
	public void setup() {
		this.loginService = mock(LoginService.class);
		loginScreen = mock(LoginScreen.class);
		uiStack = mock(UiStack.class);
		settingsStore = mock(SettingsStore.class);
		requestCallback = new RequestCallBackImpl(loginScreen,loginController);
		listener = new HttpRequestHandler(requestCallback);
		loginController = new LoginController(loginScreen, uiStack,
				loginService, settingsStore);
		loginService.setListener(listener);
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
	public void shouldSaveTheLastEnteredUsernameAndAuthorizationTokenAndPopOutLoginScreenInorderToGetBackToHomeScreen() throws Exception {

		loginController.login("abcd", "abcd"); // Just to make the
												// isRequestInProgress flag to
												// set
		Hashtable context = new Hashtable();
		String userName = "name";
		context.put(LoginController.USER_NAME, userName);

		String authorisationToken = "token";
		//loginController.on(context, authorisationToken);
		Response response = mock(Response.class);
		Result result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.getAsString("session.token")).thenReturn("token");
		loginController.onRequestSuccess(context, response);

		verify(settingsStore).setLastUsedUsername(userName);
		verify(settingsStore).setAuthorisationToken(authorisationToken);
		verify(loginScreen).popScreen(uiStack);
	}

	@Test
	public void shouldUpdateTheScreenWithConnectionProblemErrorMessage() {
		loginController.login("abcd", "abcd"); // Just to make the
												// requestInProgress flag to
												// set

		requestCallback.handleConnectionProblem();
		verify(loginScreen).handleConnectionProblem();
	}

	@Test
	public void shouldUpdateTheScreenWithLoginFailedErrorMessage() {
		loginController.login("abcd", "abcd"); // Just to make the
												// isRequestInProgress flag to
												// set

		requestCallback.handleUnauthorized();
		verify(loginScreen).handleAuthenticationFailure();
	}

}
