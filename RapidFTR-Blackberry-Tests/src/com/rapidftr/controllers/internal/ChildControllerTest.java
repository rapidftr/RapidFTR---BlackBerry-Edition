package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChildControllerTest {

	private ManageChildScreen screen;
	private UiStack uiStack;
	private ChildController controller;

	@Before
	public void setUp() {
		screen = mock(ManageChildScreen.class);
		uiStack = mock(UiStack.class);
		controller = new ChildController(screen, null, null, null, uiStack, null, null) {
		};
	}

	@Test
	public void shouldSetControllerOnScreenInstance() {
		verify(screen).setController(controller);
	}

	@Test
	public void shouldShowNewScreenOnChangeScreen() {
		CustomScreen newScreen = mock(CustomScreen.class);
		controller.changeScreen(newScreen);
		verify(newScreen).setUp();
	}

	@Test
	public void shouldPushScreenToUiStack() {
		controller.show();
		verify(uiStack).pushScreen(screen);
	}

}
