package com.rapidftr.controllers.internal;

import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ControllerTest {

    private CustomScreen screen;
    private UiStack uiStack;
    private Controller controller;
    private Dispatcher dispatcher;

    @Before
    public void setup() {
        screen = mock(CustomScreen.class);
        uiStack = mock(UiStack.class);
        dispatcher = mock(Dispatcher.class);

        controller = new TestController(screen, uiStack, dispatcher);
    }

    @Test
    public void shouldPushCurrentScreenToUiStackWhenItIsInactive() {
        when(screen.isActive()).thenReturn(false);

        controller.show();

        verify(uiStack).pushScreen(screen);
    }

    @Test
    public void shouldSetupCurrentScreenWhenItIsAlreadyActive() {
        when(screen.isActive()).thenReturn(true);

        controller.show();

        verifyZeroInteractions(uiStack);
        verify(screen).setUp();
    }

    @Test
    public void shouldPopCurrentScreenFromUiStack() {
        controller.popScreen();
        verify(screen).popScreen(uiStack);
    }

    @Test
    public void shouldClearUiStackAndShowHomeScreen() {
        controller.homeScreen();

        verify(uiStack).clear();
        verify(dispatcher).homeScreen();
    }

    @Test
    public void shouldInvokeNewChildOnCreateNewChildRecord() {
        controller.createNewChildRecord();
        verify(dispatcher).newChild();
    }

    private class TestController extends Controller {
        public TestController(CustomScreen screen, UiStack uiStack, Dispatcher dispatcher) {
            super(screen, uiStack, dispatcher);
        }
    }
}
