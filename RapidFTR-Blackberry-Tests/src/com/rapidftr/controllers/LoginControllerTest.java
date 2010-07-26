package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.utilities.SettingsStore;

public class LoginControllerTest {
	private LoginService loginService;
	private LoginScreen loginScreen;
	private UiStack uiStack;
	private SettingsStore settingsStore;
	private LoginController loginController;

	@Before
	public void setup() {
		this.loginService = mock(LoginService.class);
		loginScreen = mock(LoginScreen.class);
		uiStack = mock(UiStack.class);
		settingsStore = mock(SettingsStore.class);
		loginController = new LoginController(loginScreen, uiStack,
				loginService, settingsStore);
	}

	@Test
	public void shouldSetLoginServiceListenerOnLoginService() {
		verify(loginService).setListener(loginController);
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
	public void shouldSaveTheLastEnteredUsernameAndAuthorizationTokenAndPopOutLoginScreenInorderToGetBackToHomeScreen() {

		loginController.login("abcd", "abcd"); // Just to make the
												// isRequestInProgress flag to
												// set
		Hashtable context = new Hashtable();
		String userName = "name";
		context.put(LoginController.USER_NAME, userName);

		String authorisationToken = "token";
		loginController.onLoginSucees(context, authorisationToken);

		verify(settingsStore).setLastUsedUsername(userName);
		verify(settingsStore).setAuthorisationToken(authorisationToken);
		verify(loginScreen).popScreen(uiStack);
	}

	@Test
	public void shouldUpdateTheScreenWithConnectionProblemErrorMessage() {
		loginController.login("abcd", "abcd"); // Just to make the
												// requestInProgress flag to
												// set

		loginController.onConnectionProblem();
		verify(loginScreen).onConnectionProblem();
	}

	@Test
	public void shouldUpdateTheScreenWithLoginFailedErrorMessage() {
		loginController.login("abcd", "abcd"); // Just to make the
												// isRequestInProgress flag to
												// set

		loginController.onAuthenticationFailure();
		verify(loginScreen).onLoginFailed();
	}

}
