package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ViewChildControllerTest {

    @Mock
    private ViewChildScreen screen;
    @Mock
    private UiStack uiStack;
    @Mock
    private FormStore formStore;
    @Mock
    private Dispatcher dispatcher;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInvokeFlagRecordOnDispatcher() {
        ViewChildController controller = new ViewChildController(screen, uiStack, formStore, dispatcher);
        Child child = ChildFactory.newChild();

        controller.flagRecord(child);

        verify(dispatcher).flagRecord(child);
    }
}
