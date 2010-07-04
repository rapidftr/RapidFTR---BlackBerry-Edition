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
    private ViewChildrenController viewChildrenController;

    @Before
    public void setup(){
        loginController = mock(LoginController.class);
        viewChildrenController = mock(ViewChildrenController.class);
        applicationRootScreen = mock(ApplicationRootScreen.class);
        uiStack = mock(UiStack.class);
    }

    @Test
    public void should_show_root_screen_on_start() {

        ApplicationRootController controller = new ApplicationRootController(applicationRootScreen, uiStack, loginController, viewChildrenController);

        controller.start();

        verify(uiStack).pushScreen(applicationRootScreen);
    }

    @Test
    public void should_show_login_controller_when_the_user_clicks_login() {
        ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, null, loginController, viewChildrenController);
        rootController.login();
        verify(loginController).show();
    }

    @Test
    public void should_show_view_children_controller_when_user_clicks_view_children() {
        ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, null, loginController, viewChildrenController);
        rootController.viewChildren();
        verify(viewChildrenController).show();
    }

    @Test
    public void should_set_self_as_the_screens_controller() {
        ApplicationRootController controller = new ApplicationRootController(applicationRootScreen, uiStack, loginController, viewChildrenController);

        verify(applicationRootScreen).setApplicationRootController(controller);
    }

}
