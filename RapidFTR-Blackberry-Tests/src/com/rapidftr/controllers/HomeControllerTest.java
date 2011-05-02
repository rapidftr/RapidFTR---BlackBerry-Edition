package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.Settings;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HomeControllerTest {

    private CustomScreen screen;
    private UiStack uiStack;
    private Settings settings;
    private Dispatcher dispatcher;
    private HomeController homeController;

    @Before
    public void setup() {
        screen = mock(CustomScreen.class);
        dispatcher = mock(Dispatcher.class);
        uiStack = mock(UiStack.class);
        settings = mock(Settings.class);

        homeController = new HomeController(screen, uiStack, settings, dispatcher);
    }

    @Test
    public void shouldShowViewChildrenScreen() {
        homeController.viewChildren();
        verify(dispatcher).viewChildren();
    }

    @Test
    public void shouldShowNewChildScreen() {
        homeController.newChild();
        verify(dispatcher).newChild();
    }

    @Test
    public void shouldSynchronizeChildren() {
        homeController.synchronize();
        verify(dispatcher).synchronize();
    }

    @Test
    public void shouldShowSearchChildrenScreen() {
        homeController.showSearch();
        verify(dispatcher).searchChild();
    }

    @Test
    public void shouldResetDevice() {
        homeController.cleanAll();
        verify(dispatcher).resetDevice();
    }

    @Test
    public void shouldShowLoginWithNoAttachedProcess() {
        homeController.logIn();
        verify(dispatcher).login(null);
    }

    @Test
    public void shouldShowContactScreen() {
        homeController.showcontact();
        verify(dispatcher).showcontact();
    }
}
