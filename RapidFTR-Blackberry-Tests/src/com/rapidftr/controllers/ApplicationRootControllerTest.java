package com.rapidftr.controllers;

import com.rapidftr.screens.ApplicationRootScreen;
import com.rapidftr.screens.UiStack;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class ApplicationRootControllerTest {
    private LoginController loginController;

    @Test
    public void should_show_root_screen_on_start() {

        ApplicationRootScreen applicationRootScreen = new ApplicationRootScreen();
        UiStack uiStack = Mockito.mock(UiStack.class);

        ApplicationRootController controller = new ApplicationRootController(loginController, applicationRootScreen, uiStack);

        controller.start();

        verify(uiStack).pushScreen(applicationRootScreen);
    }

}
