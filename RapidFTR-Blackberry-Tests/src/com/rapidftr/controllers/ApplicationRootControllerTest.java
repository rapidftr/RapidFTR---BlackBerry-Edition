package com.rapidftr.controllers;

import com.rapidftr.model.Child;
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
    private NewChildController newChildController;
    private ViewChildController viewChildController;

    @Before
    public void setup() {
        loginController = mock(LoginController.class);
        viewChildrenController = mock(ViewChildrenController.class);
        applicationRootScreen = mock(ApplicationRootScreen.class);
        newChildController = mock(NewChildController.class);
        viewChildController = mock(ViewChildController.class);
        uiStack = mock(UiStack.class);
    }

    @Test
    public void should_show_root_screen_on_start() {

        ApplicationRootController controller = new ApplicationRootController(applicationRootScreen, uiStack, loginController, viewChildrenController, newChildController, viewChildController);

        controller.start();

        verify(uiStack).pushScreen(applicationRootScreen);
    }

    @Test
    public void should_show_login_controller_when_the_user_clicks_login() {
        ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, null, loginController, viewChildrenController, newChildController, viewChildController);
        rootController.login();
        verify(loginController).show();
    }

    @Test
    public void should_show_view_children_controller_when_user_clicks_view_children() {
        ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, null, loginController, viewChildrenController, newChildController, viewChildController);
        rootController.viewChildren();
        verify(viewChildrenController).show();
    }
    
    @Test
     public void should_show_new_child_controller_when_user_clicks_new_child() {
         ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, null, loginController, viewChildrenController, newChildController, viewChildController);
         rootController.newChildRecord();
         verify(newChildController).show();
     }

    @Test
    public void should_set_self_as_the_screens_controller() {
        ApplicationRootController controller = new ApplicationRootController(applicationRootScreen, uiStack, loginController, viewChildrenController, newChildController, viewChildController);

        verify(applicationRootScreen).setApplicationRootController(controller);
    }

    @Test
    public void should_show_new_child_form() {
        ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, uiStack, loginController, viewChildrenController, newChildController, viewChildController);

        Child child = new Child("Tom");
        rootController.viewChild(child);
        verify(viewChildController).show(child);
    }

    @Test
    public void should_inject_itself_into_controllers_that_need_it() {
        ApplicationRootController rootController = new ApplicationRootController(applicationRootScreen, uiStack, loginController, viewChildrenController, newChildController, viewChildController);
        verify(viewChildrenController).setApplicationRootController(rootController);
    }

}
