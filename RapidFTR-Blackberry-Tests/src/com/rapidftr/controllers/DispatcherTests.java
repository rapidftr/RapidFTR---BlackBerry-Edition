package com.rapidftr.controllers;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.model.Child;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DispatcherTests {

	private HomeScreenController homeScreenController;
	private LoginController loginController;
	private ViewChildrenController viewChildrenController;
	private ViewChildController viewChildController;
	private SynchronizeFormsController synchronizeFormsController;
	private Dispatcher dispatcher;

	@Before
	public void setUp() {
		homeScreenController = mock(HomeScreenController.class);
		loginController = mock(LoginController.class);
		viewChildrenController = mock(ViewChildrenController.class);
		viewChildController = mock(ViewChildController.class);
		synchronizeFormsController = mock(SynchronizeFormsController.class);
		dispatcher = new Dispatcher(homeScreenController, loginController,
				viewChildrenController, viewChildController,
				synchronizeFormsController);

	}

	@Test
	public void shoulShowHomeScrenn() {

		dispatcher.homeScreen();
		verify(homeScreenController).show();
	}

	@Test
	public void shouldShowLoginScreen() {
		dispatcher.login();
		verify(loginController).show();
	}

	@Test
	public void shouldShowViewChildernScreen() {
		dispatcher.viewChildern();
		verify(viewChildrenController).show();
	}

	@Test
	public void shouldShowViewChildScreen() {
		Child child = mock(Child.class);
		dispatcher.viewChild(child);
		verify(viewChildController).show(child);
	}

}
