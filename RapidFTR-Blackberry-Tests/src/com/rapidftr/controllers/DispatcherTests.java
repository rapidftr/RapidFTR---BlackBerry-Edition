package com.rapidftr.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

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
	private ChildController childController;
	@Mock
	private SyncController syncController;
	@Mock
	private ResetDeviceController resetDeviceController;

	private ContactScreenController contactScreenController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dispatcher = new Dispatcher(homeScreenController, loginController,
				childController, syncController, resetDeviceController, contactScreenController);

	}

	@Test
	public void shouldShowHomeScrenn() {

		dispatcher.homeScreen();
		verify(homeScreenController).show();
	}

	@Test
	public void shouldShowLoginScreen() {
		com.rapidftr.process.Process process = mock(com.rapidftr.process.Process.class);
		dispatcher.login(process);
		verify(loginController).showLoginScreen(process);
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

	
	@Test
	public void shouldResetDevice() {
		dispatcher.resetDevice();
		verify(resetDeviceController).resetDevice();
	}
}
