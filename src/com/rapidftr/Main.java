package com.rapidftr;

import net.rim.device.api.ui.UiApplication;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.utilities.LocalStore;
import com.rapidftr.utilities.TestRecordUtil;
import com.rapidftr.utilities.impl.LocalStoreImpl;

public class Main extends UiApplication {

	public static final String APPLICATION_NAME = "Rapid FTR";

	/**
	 * Entry point for application.
	 */
	public static void main(String[] args) {

		// Create a new instance of the application.
		Main application = new Main();

		// To make the application enter the event thread and start processing
		// messages,
		// we invoke the enterEventDispatcher() method.
		application.enterEventDispatcher();
	}

	/**
	 * <p>
	 * The default constructor. Creates all of the RIM UI components and pushes
	 * the application's root screen onto the UI stack.
	 */
	public Main() {
		// create some test data
		//persistToStore();

		
		pushScreen(new LoginScreen());
	}

	private void persistToStore() {
		LocalStore localStore = LocalStoreImpl.getInstance();

		if (localStore.countStoredRecords() == 0) {
			ChildRecord[] testRecords = TestRecordUtil.createTestRecords();

			localStore.persist(testRecords);
		}
	}
}
