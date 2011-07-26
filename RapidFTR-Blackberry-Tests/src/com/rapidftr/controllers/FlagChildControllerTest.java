package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.screens.FlagReasonScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class FlagChildControllerTest {

    @Mock
    private FlagReasonScreen screen;
    @Mock
    private UiStack uiStack;
    @Mock
    private Dispatcher dispatcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldSetChildOnFlagReasonScreenAndShow() {
        FlagChildController controller = new FlagChildController(screen, uiStack, dispatcher);
        Child child = ChildFactory.newChild();

        controller.flagRecord(child);

        verify(screen).setChild(child);
        verify(uiStack).pushScreen(screen);
        verify(screen).setUp();
    }
}
