package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rapidftr.services.LoginService;
import com.rapidftr.utilities.SettingsStore;
import org.junit.Before;
import org.junit.Test;

import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.UiStack;

public class LoginControllerTest {
    private LoginService loginService;
    private LoginScreen loginScreen;
    private UiStack uiStack;
    private SettingsStore settingsStore;
    private static final String AUTHORISATION_TOKEN = "authorisationtoken";

    @Before
    public void setup(){
        this.loginService = mock(LoginService.class);
        loginScreen = mock(LoginScreen.class);
        uiStack = mock(UiStack.class);
        settingsStore = mock(SettingsStore.class);
    }
	@Test
	public void should_initalise_and_display_the_login_form(){
        LoginController controller = new LoginController(loginScreen, uiStack, loginService, settingsStore);
		
		controller.show();
		verify(uiStack).pushScreen(loginScreen);
		
	}
	
	@Test
	public void should_set_self_as_the_login_screens_controller() {
		LoginController controller = new LoginController(loginScreen, uiStack, loginService, settingsStore);
		
		verify(loginScreen).setLoginController(controller);
	}

    @Test
    public void should_attempt_login_over_http_provider_with_given_credentials() throws Exception {
        LoginController loginController = new LoginController(loginScreen, uiStack, loginService, settingsStore);

        String userName = "zskjh";
        String password = "ksdhfkl";
        loginController.login(userName, password);

        verify(loginService).login(userName, password);
    }

    @Test
    public void should_save_the_last_entered_username() {
        LoginController loginController = new LoginController(loginScreen, uiStack, loginService, settingsStore);

        loginController.login("username", "password");

        verify(settingsStore).setLastUsedUsername("username");
    }

    @Test
    public void should_store_authorisation_token_when_login_is_successful() throws Exception {
        LoginController loginController = new LoginController(loginScreen, uiStack, loginService, settingsStore);
        when(loginService.login("username", "password")).thenReturn(AUTHORISATION_TOKEN);

        loginController.login("username", "password");

        verify(settingsStore).setAuthorisationToken(AUTHORISATION_TOKEN);
    }
}
