package com.rapidftr.controllers;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.screens.HomeScreen;
import com.rapidftr.screens.UiStack;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HomeScreenControllerTest {

	private HomeScreen screen;
	private UiStack uiStack;
	private Dispatcher dispatcher;
	private HomeScreenController homeScreenController;

	@Before
	public void setUp() {
		uiStack = mock(UiStack.class);
		screen = mock(HomeScreen.class);
		dispatcher = mock(Dispatcher.class);
		homeScreenController = new HomeScreenController(screen, uiStack);
		homeScreenController.setDispatcher(dispatcher);
	}

	@Test
	public void shouldDelegateLoginActionToDispatcher() {
		homeScreenController.login();
		verify(dispatcher).login();
	}

	@Test
	public void shouldDelegateViewChildrenActionToDispatcher() {
		homeScreenController.viewChildren();
		verify(dispatcher).viewChildern();

	}

	@Test
	public void shouldDelegateSynchronizeFormActionToDispatcher() {
		homeScreenController.synchronizeForms();
		verify(dispatcher).synchronizeForms();
	}

	@Test
	public void shouldDelegateNewChildActionToDispatcher() {
		homeScreenController.newChild();
		verify(dispatcher).newChild();
	}

}
