package com.rapidftr;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controllers.ResetDeviceController;
import com.rapidftr.controllers.SyncController;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.HomeScreen;
import com.rapidftr.screens.LoginScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.SyncScreen;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.UiStack;
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
	 * The default constructor. Creates all of the RIM UI components and pushes
	 * the application's root screen onto the UI stack.
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
		ChildStoreService childStoreService = new ChildStoreService(
				childRecordStore);
		FormService formService = new FormService(httpService, formStore);
		ChildSyncService childSyncService = new ChildSyncService(httpService, childRecordStore);

		UiStack uiStack = new UiStack(this);

		HomeScreen homeScreen = new HomeScreen();
		LoginScreen loginScreen = new LoginScreen(settings);
		ViewChildScreen viewChildScreen = new ViewChildScreen();
		ViewChildrenScreen viewChildrenScreen = new ViewChildrenScreen();
		SearchChildScreen searchChildScreen = new SearchChildScreen();
		ManageChildScreen newChildScreen = new ManageChildScreen(settings);
	    SyncScreen syncScreen = new SyncScreen();
		LoginController loginController = new LoginController(loginScreen,
				uiStack, loginService);
		// loginScreen.setController(loginController);
		ResetDeviceController restController = new ResetDeviceController(formService, childSyncService, loginService);
		HomeScreenController homeScreenController = new HomeScreenController(homeScreen, uiStack);
		ChildController childController = new ChildController(
				newChildScreen, viewChildScreen, searchChildScreen,
				viewChildrenScreen, uiStack, formStore, childStoreService);
		SyncController syncController = new SyncController(syncScreen,uiStack,childSyncService,formService);

		Dispatcher dispatcher = new Dispatcher(homeScreenController,
				loginController,
				childController, syncController,restController);
		dispatcher.homeScreen();

	}

	private void enablePermission(int permission) {
		ApplicationPermissionsManager manager = ApplicationPermissionsManager
				.getInstance();

		if (manager.getApplicationPermissions().getPermission(permission) == ApplicationPermissions.VALUE_ALLOW) {
			System.out.println("Got correct permissions");
			return;
		}

		ApplicationPermissions requestedPermissions = new ApplicationPermissions();

		if (!requestedPermissions.containsPermissionKey(permission)) {
			requestedPermissions.addPermission(permission);
		}

		ApplicationPermissionsManager.getInstance().invokePermissionsRequest(
				requestedPermissions);
	}
}
