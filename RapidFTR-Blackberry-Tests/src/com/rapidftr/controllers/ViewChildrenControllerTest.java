package com.rapidftr.controllers;

import java.io.IOException;

import com.rapidftr.model.Child;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.services.ChildService;
import com.rapidftr.utilities.SettingsStore;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ViewChildrenControllerTest {

    private ChildService childService;
    private ViewChildrenScreen viewChildrenScreen;
    private UiStack uiStack;
    private SettingsStore settingsStore;
	private ViewChildrenController controller;
	private Dispatcher dispatcher;

    @Before
    public void setup() {
        childService = mock(ChildService.class);
        viewChildrenScreen = mock(ViewChildrenScreen.class);
        uiStack = mock(UiStack.class);
        settingsStore = mock(SettingsStore.class);
        dispatcher = mock(Dispatcher.class);
        controller = new ViewChildrenController(viewChildrenScreen, uiStack, childService);
        controller.setDispatcher(dispatcher);
    }

   
   
    @Test
    public void shouldFetchChildrenFromServiceAndPushIntoScreen() throws IOException {

        Child[] children = new Child[] { new Child("Tom") };
        when(childService.getAllChildren()).thenReturn(children);

        controller.show();
        
        verify(childService).getAllChildren();
        verify(viewChildrenScreen).setChildren(children);
    }

    @Test
    public void shouldShowChildDetailsWhenASingleChildIsClicked() {
        Child child = mock(Child.class);
        controller.showChild(child);
        verify(dispatcher).viewChild(child);
    }
}
