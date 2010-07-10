package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.services.ChildServiceImpl;
import com.rapidftr.utilities.SettingsStore;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ViewChildrenControllerTest {

    private ChildServiceImpl childService;
    private ViewChildrenScreen viewChildrenScreen;
    private UiStack uiStack;
    private SettingsStore settingsStore;
    private ApplicationRootController applicationRootController;

    @Before
    public void setup() {
        this.childService = mock(ChildServiceImpl.class);
        viewChildrenScreen = mock(ViewChildrenScreen.class);
        uiStack = mock(UiStack.class);
        settingsStore = mock(SettingsStore.class);
        applicationRootController = mock(ApplicationRootController.class);

    }

    @Test
    public void should_initalise_and_display_the_view_children_form() {
        ViewChildrenController controller = new ViewChildrenController(viewChildrenScreen, uiStack, childService);

        controller.show();
        verify(uiStack).pushScreen(viewChildrenScreen);

    }

    @Test
    public void should_set_self_as_the_view_children_screens_controller() {
        ViewChildrenController controller = new ViewChildrenController(viewChildrenScreen, uiStack, childService);

        verify(viewChildrenScreen).setViewChildrenController(controller);
    }

    @Test
    public void should_fetch_children_from_service_and_push_into_screen() {
        ViewChildrenController controller = new ViewChildrenController(viewChildrenScreen, uiStack, childService);

        Child[] children = new Child[] { new Child("Tom") };
        when(childService.getAllChildren()).thenReturn(children);

        controller.show();
        
        verify(childService).getAllChildren();
        verify(viewChildrenScreen).setChildren(children);
    }

    @Test
    public void should_show_child_details_when_a_single_child_is_clicked() {
        ViewChildrenController controller = new ViewChildrenController(viewChildrenScreen, uiStack, childService);
        Child child = new Child("Tom");

        controller.showChild(child);

        verify(applicationRootController).viewChild(child);
    }
}
