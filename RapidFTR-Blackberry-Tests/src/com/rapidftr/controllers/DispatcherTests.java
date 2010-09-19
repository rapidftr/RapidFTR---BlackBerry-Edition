package com.rapidftr.controllers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.controllers.internal.Dispatcher;

public class DispatcherTests {

	private Dispatcher dispatcher;

	@Mock
	private HomeScreenController homeScreenController;
	@Mock
	private LoginController loginController;
	@Mock
	private SynchronizeFormsController synchronizeFormsController;
	@Mock
	private ChildController childController;
	@Mock
	private SyncChildController uploadChildRecordsController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dispatcher = new Dispatcher(homeScreenController, loginController,
				synchronizeFormsController, childController,
				uploadChildRecordsController);

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
		verify(childController).viewChildren();
	}

	@Test
	public void shouldShowNewChildScreen() {
		dispatcher.newChild();
		verify(childController).newChild();
	}

}
