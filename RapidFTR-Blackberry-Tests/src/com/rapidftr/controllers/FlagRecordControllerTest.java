package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.Children;
import com.rapidftr.model.Child;
import com.rapidftr.screens.FlagReasonScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FlagRecordControllerTest {

	    private FlagReasonScreen flagReasonScreen;
	    private UiStack uiStack;
	    private FlagRecordController controller;
	    private Dispatcher dispatcher;
		private Child child;

	    @Before
	    public void setup() {
	        flagReasonScreen = mock(FlagReasonScreen.class);
	        uiStack = mock(UiStack.class);
	        dispatcher = mock(Dispatcher.class);
	        controller = new FlagRecordController(this.flagReasonScreen, uiStack, dispatcher);
	        child = mock(Child.class);
	    }

	    @Test
	    public void shouldSetChildInFlagReasonScreen() {
	        controller.flagRecord(child);
	        verify(flagReasonScreen).setChild(child);
	    }
	
}
