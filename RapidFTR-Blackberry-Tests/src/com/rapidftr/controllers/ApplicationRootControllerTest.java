package com.rapidftr.controllers;

import com.rapidftr.screens.ApplicationRootScreen;
import com.rapidftr.screens.UiStack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class ApplicationRootControllerTest {
    private LoginController loginController;

    @Before
    public void setup(){
        loginController = Mockito.mock(LoginController.class);
    }

    @Test
    public void should_show_root_screen_on_start() {

        ApplicationRootScreen applicationRootScreen = new ApplicationRootScreen();
        UiStack uiStack = Mockito.mock(UiStack.class);

        ApplicationRootController controller = new ApplicationRootController(loginController, applicationRootScreen, uiStack);

        controller.start();

        verify(uiStack).pushScreen(applicationRootScreen);
    }

    @Test
    public void should_show_login_controller_when_the_user_clicks_login() {

        ApplicationRootController rootController = new ApplicationRootController(loginController, null, null);
        rootController.login();
        verify(loginController).show();
    }

}
