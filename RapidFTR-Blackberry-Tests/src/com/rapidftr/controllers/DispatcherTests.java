package com.rapidftr.controllers;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.model.Child;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DispatcherTests {

	private HomeScreenController homeScreenController;
	private LoginController loginController;
	private ViewChildrenController viewChildrenController;
	private SynchronizeFormsController synchronizeFormsController;
	private Dispatcher dispatcher;
	private ChildController childController;
	private SyncChildController uploadChildRecordsController;
	private SearchChildController searchChildController;

	@Before
	public void setUp() {
		homeScreenController = mock(HomeScreenController.class);
		loginController = mock(LoginController.class);
		viewChildrenController = mock(ViewChildrenController.class);
		synchronizeFormsController = mock(SynchronizeFormsController.class);
		childController = mock(ChildController.class);
		uploadChildRecordsController = mock(SyncChildController.class);
		searchChildController = mock(SearchChildController.class);
		dispatcher = new Dispatcher(homeScreenController, loginController,
				viewChildrenController,
				synchronizeFormsController, childController,
				uploadChildRecordsController, searchChildController);

	}

	@Test
	public void shouldShowHomeScrenn() {

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
		dispatcher.viewChildren();
		verify(viewChildrenController).show();
	}

	@Test
	public void shouldShowViewChildScreen() {
		Child child = mock(Child.class);
		dispatcher.viewChild(child);
		verify(childController).viewChild(child);
	}

	@Test
	public void shouldShowNewChildScreen() {
		dispatcher.newChild();
		verify(childController).newChild();
	}

}
