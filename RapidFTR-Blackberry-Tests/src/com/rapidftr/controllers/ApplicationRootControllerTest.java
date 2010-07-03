package com.rapidftr.controllers;

import com.rapidftr.screens.ApplicationRootScreen;
import com.rapidftr.screens.UiStack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ApplicationRootControllerTest {
    private LoginController loginController;
    private ApplicationRootScreen applicationRootScreen;
    private UiStack uiStack;

    @Before
    public void setup(){
        loginController = mock(LoginController.class);
        applicationRootScreen = mock(ApplicationRootScreen.class);
        uiStack = mock(UiStack.class);
    }

    @Test
    public void should_show_root_screen_on_start() {

        ApplicationRootController controller = new ApplicationRootController(loginController, applicationRootScreen, uiStack);

        controller.start();

        verify(uiStack).pushScreen(applicationRootScreen);
    }

    @Test
    public void should_show_login_controller_when_the_user_clicks_login() {

        ApplicationRootController rootController = new ApplicationRootController(loginController, applicationRootScreen, null);
        rootController.login();
        verify(loginController).show();
    }

    @Test
    public void should_set_self_as_the_screens_controller() {
        ApplicationRootController controller = new ApplicationRootController(loginController, applicationRootScreen, uiStack);

        verify(applicationRootScreen).setApplicationRootController(controller);
    }

}
