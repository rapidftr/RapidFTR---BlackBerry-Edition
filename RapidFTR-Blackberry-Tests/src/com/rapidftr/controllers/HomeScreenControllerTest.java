package com.rapidftr.controllers;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.screens.HomeScreen;
import com.rapidftr.screens.internal.UiStack;

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
	public void shouldDelegateViewChildrenActionToDispatcher() {
		homeScreenController.viewChildren();
		verify(dispatcher).viewChildren();

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

	@Test
	public void shouldDelegateSyncAllActionToDispatcher() {
		homeScreenController.syncAll();
		verify(dispatcher).syncAll();
	}
}
