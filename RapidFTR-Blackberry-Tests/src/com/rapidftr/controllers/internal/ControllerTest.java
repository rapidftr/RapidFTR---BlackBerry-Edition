package com.rapidftr.controllers.internal;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

public class ControllerTest {

	private CustomScreen screen;
	private UiStack uiStack;
	private Controller controller;

	@Before
	public void setUp() {
		screen = mock(CustomScreen.class);
		uiStack = mock(UiStack.class);
		controller = new Controller(screen, uiStack) {
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
