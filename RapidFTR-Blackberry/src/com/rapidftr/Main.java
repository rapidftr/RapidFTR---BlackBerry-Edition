package com.rapidftr;

import com.rapidftr.controllers.*;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.*;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.LoginSettings;
import com.rapidftr.utilities.DefaultStore;
import com.rapidftr.utilities.HttpSettings;
import com.rapidftr.utilities.Settings;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

public class Main extends UiApplication {

    public static void main(String[] args) {
		Main application = new Main();
		application.enterEventDispatcher();
	}

	public Main() {

		enablePermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		enablePermission(ApplicationPermissions.PERMISSION_FILE_API);

		DefaultStore defaultStore = new DefaultStore(new Key("com.rapidftr.utilities.ftrstore"));
		
		ChildrenRecordStore childrenStore = new ChildrenRecordStore(
				new DefaultStore(new Key("com.rapidftr.utilities.childrenstore")));
		
		FormStore formStore = new FormStore();
		
		Settings settings = new Settings(defaultStore);

		HttpServer httpServer = new HttpServer(new HttpSettings(settings));

		HttpService httpService = new HttpService(httpServer, settings);
		
		LoginService loginService = new LoginService(httpService, new LoginSettings(settings));

		FormService formService = new FormService(httpService, formStore);
		
		ChildSyncService childSyncService = new ChildSyncService(httpService,
				childrenStore);

		UiStack uiStack = new UiStack(this);

		HomeScreen homeScreen = new HomeScreen(settings);
		
		LoginScreen loginScreen = new LoginScreen(new HttpSettings(settings));
		
		ViewChildScreen viewChildScreen = new ViewChildScreen();
		
		ViewChildrenScreen viewChildrenScreen = new ViewChildrenScreen();
		
		SearchChildScreen searchChildScreen = new SearchChildScreen();
		
		ManageChildScreen newChildScreen = new ManageChildScreen(settings);
		
		SyncScreen syncScreen = new SyncScreen();
		
		LoginController loginController = new LoginController(loginScreen,
				uiStack, loginService);
		
		ResetDeviceController restController = new ResetDeviceController(
				formService, childSyncService, loginService);
		
		HomeScreenController homeScreenController = new HomeScreenController(
				homeScreen, uiStack);
		
		ChildPhotoScreen childPhotoScreen = new ChildPhotoScreen();
		
		ChildController childController = new ChildController(newChildScreen,
				viewChildScreen, searchChildScreen, viewChildrenScreen,
				uiStack, formStore, childrenStore, childPhotoScreen);
		
		SyncController syncController = new SyncController(syncScreen, uiStack,
				childSyncService, formService);

		Dispatcher dispatcher = new Dispatcher(homeScreenController,
				loginController, childController, syncController,
				restController);
		
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
