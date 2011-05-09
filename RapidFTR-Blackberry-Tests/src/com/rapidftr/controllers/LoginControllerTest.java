package com.rapidftr.controllers;


import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.ScreenCallBack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LoginControllerTest {
    private LoginService loginService;
    private LoginScreen loginScreen;
    private UiStack uiStack;
    private LoginController loginController;
    private ScreenCallBack screenCallBack;
    private ConnectionFactory connectionFactory;


    @Before
    public void setup() {
        this.loginService = mock(LoginService.class);
        loginScreen = mock(LoginScreen.class);
        uiStack = mock(UiStack.class);
        connectionFactory = mock(ConnectionFactory.class);
        loginController = new LoginController(loginScreen, uiStack,
                loginService, connectionFactory);
        screenCallBack = loginController.getScreenCallBack();
    }

    @Test
    public void shouldAttemptLoginOverHttpProviderWithGivenCredentials()
            throws Exception {
        String userName = "zskjh";
        String password = "ksdhfkl";

        WhenUserIsConnected();
        loginController.login(userName, password);
        verify(loginService).login(userName, password);
    }

    @Test
    public void shouldUpdateTheScreenWithConnectionProblemErrorMessage() {
        WhenUserIsConnected();

        loginController.login("abcd", "abcd");
        screenCallBack.onConnectionProblem();

        verify(loginScreen).onConnectionProblem();
    }

    @Test
    public void shouldUpdateTheScreenWithLoginFailedErrorMessage() {
        WhenUserIsConnected();

        loginController.login("abcd", "abcd");
        screenCallBack.onAuthenticationFailure();

        verify(loginScreen).onAuthenticationFailure();
    }

    @Test
    public void shouldAttemptOfflineLoginWhenThereIsNoConnection() {
        String userName = "user";
        String password = "password";

        WhenThereIsNoConnection();

        loginController.login(userName, password);

        verify(loginService).offlineLogin(userName, password);
    }

    @Test
    public void shouldClearLoginScreenAndGoToHomeScreenOnSucessfullOfflineLogin() {
        Dispatcher dispatcher = mock(Dispatcher.class);

        loginController.setDispatcher(dispatcher);
        WhenThereIsNoConnection();
        when(loginService.offlineLogin(anyString(), anyString())).thenReturn(true);

        loginController.login("user", "password");

        verify(uiStack).clear();
        verify(loginScreen).resetCredentials(true);
        verify(dispatcher).homeScreen();
    }

    @Test
    public void shouldShowOfflineLoginErrorMessageOnFailedOfflineLogin() {
        WhenThereIsNoConnection();
        when(loginService.offlineLogin(anyString(), anyString())).thenReturn(false);

        loginController.login("user", "password");

        verify(loginScreen).onProcessFail("You are working offline. You must authenticate with your credentials from the last successful online log in.");
    }

    private void WhenThereIsNoConnection() {
        when(connectionFactory.isNotConnected()).thenReturn(true);
    }

    private void WhenUserIsConnected() {
        when(connectionFactory.isNotConnected()).thenReturn(false);
    }
}
