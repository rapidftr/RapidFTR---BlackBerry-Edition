package com.rapidftr;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

import com.rapidftr.controllers.Dispatcher;
import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controllers.ManageChildController;
import com.rapidftr.controllers.SearchChildController;
import com.rapidftr.controllers.SyncChildController;
import com.rapidftr.controllers.SynchronizeFormsController;
import com.rapidftr.controllers.ViewChildController;
import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.HomeScreen;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.SyncChildScreen;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.services.ChildStoreService;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.LoginService;
import com.rapidftr.utilities.SettingsStore;

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
	 * The default constructor. Creates all of the RIM UI components and pushes the application's root screen onto the UI stack.
	 */
	public Main() {
		enablePermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		enablePermission(ApplicationPermissions.PERMISSION_FILE_API);

		SettingsStore settings = new SettingsStore();

		FormStore formStore = new FormStore();

		ChildrenRecordStore childRecordStore = new ChildrenRecordStore();

		HttpServer httpServer = new HttpServer();
		HttpService httpService = new HttpService(httpServer, settings);

		LoginService loginService = new LoginService(httpService, settings);

		ChildStoreService childStoreService = new ChildStoreService(childRecordStore);

		UiStack uiStack = new UiStack(this);

		LoginScreen loginScreen = new LoginScreen(settings);
		LoginController loginController = new LoginController(loginScreen, uiStack, loginService);
		loginScreen.setController(loginController);

		HomeScreen homeScreen = new HomeScreen();
		HomeScreenController homeScreenController = new HomeScreenController(homeScreen, uiStack);

		ViewChildScreen viewChildScreen = new ViewChildScreen();
		ViewChildController viewChildController = new ViewChildController(viewChildScreen, uiStack);

		ViewChildrenScreen viewChildrenScreen = new ViewChildrenScreen();
		ViewChildrenController viewChildrenController = new ViewChildrenController(viewChildrenScreen, uiStack, childStoreService);

		SynchronizeFormsScreen synchronizeFormsScreen = new SynchronizeFormsScreen();
		SynchronizeFormsController synchronizeFormsController = new SynchronizeFormsController(new FormService(httpService, formStore), uiStack, synchronizeFormsScreen);

		ManageChildScreen newChildScreen = new ManageChildScreen(settings);
		ManageChildController newChildController = new ManageChildController(newChildScreen, uiStack, formStore, childStoreService);
		SyncChildScreen uploadChildRecordsScreen = new SyncChildScreen();

		ChildSyncService childRecordsUploadService = new ChildSyncService(httpService, childRecordStore);
		SyncChildController uploadChildRecordsController = new SyncChildController(uploadChildRecordsScreen, uiStack, childRecordsUploadService);

		SearchChildScreen searchChildScreen = new SearchChildScreen();
		SearchChildController searchChildController = new SearchChildController(searchChildScreen, uiStack);

		Dispatcher dispatcher = new Dispatcher(homeScreenController, loginController, viewChildrenController, viewChildController, synchronizeFormsController, newChildController, uploadChildRecordsController, searchChildController);

		dispatcher.homeScreen();

	}

	private void enablePermission(int permission) {
		ApplicationPermissionsManager manager = ApplicationPermissionsManager.getInstance();

		if (manager.getApplicationPermissions().getPermission(permission) == ApplicationPermissions.VALUE_ALLOW) {
			System.out.println("Got correct permissions");
			return;
		}

		ApplicationPermissions requestedPermissions = new ApplicationPermissions();

		if (!requestedPermissions.containsPermissionKey(permission)) {
			requestedPermissions.addPermission(permission);
		}

		ApplicationPermissionsManager.getInstance().invokePermissionsRequest(requestedPermissions);
	}
}
