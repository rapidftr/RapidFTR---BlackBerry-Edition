package com.rapidftr;

import com.rapidftr.controllers.*;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.*;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.*;
import com.rapidftr.utilities.DefaultStore;
import com.rapidftr.utilities.HttpSettings;
import com.rapidftr.utilities.Settings;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.UiApplication;

public class Main extends UiApplication {
    public boolean permissionsGranted = false;

    public static void main(String[] args) {
		Main application = new Main();
        application.enterEventDispatcher();
	}

	public Main() {

        UiStack uiStack = new UiStack(this);
        int [] requiredPermissions = new int[] {
                ApplicationPermissions.PERMISSION_INPUT_SIMULATION,
                ApplicationPermissions.PERMISSION_FILE_API,
                ApplicationPermissions.PERMISSION_RECORDING,
                ApplicationPermissions.PERMISSION_PHONE
        };

        this.permissionsGranted = makePermissionsRequest(requiredPermissions);

        DefaultStore defaultStore = new DefaultStore(new Key(
				"com.rapidftr.utilities.ftrstore"));

        ChildrenRecordStore childrenStore = new ChildrenRecordStore(
				new DefaultStore(
						new Key("com.rapidftr.utilities.childrenstore")));

        FormStore formStore = new FormStore();

        Settings settings = new Settings(defaultStore);

        HttpServer httpServer = new HttpServer(new HttpSettings(settings));

        HttpService httpService = new HttpService(httpServer, settings);

        LoginService loginService = new LoginService(httpService,
				new LoginSettings(settings));

        FormService formService = new FormService(httpService, formStore);

        ChildSyncService childSyncService = new ChildSyncService(httpService,
				childrenStore);


		HomeScreen homeScreen = new HomeScreen(settings);

		ContactInformationScreen contactScreen = new ContactInformationScreen(
				new ContactInformation(defaultStore));

		LoginScreen loginScreen = new LoginScreen(new HttpSettings(settings));

		ViewChildScreen viewChildScreen = new ViewChildScreen();

		ViewChildrenScreen viewChildrenScreen = new ViewChildrenScreen();

		SearchChildScreen searchChildScreen = new SearchChildScreen();

		ManageChildScreen newChildScreen = new ManageChildScreen(settings);

		SyncScreen syncScreen = new SyncScreen(settings);

		LoginController loginController = new LoginController(loginScreen,
				uiStack, loginService);

		ResetDeviceController restController = new ResetDeviceController(
				formService, childSyncService, loginService);

		HomeController homeScreenController = new HomeController(
				homeScreen, uiStack, settings);

		ContactInformationController contactScreenController = new ContactInformationController(
				contactScreen, uiStack, new ContactInformationSyncService(
						httpService, new ContactInformation(defaultStore)));

		ChildPhotoScreen childPhotoScreen = new ChildPhotoScreen();

		ChildHistoryScreen childHistoryScreen = new ChildHistoryScreen();

		ViewChildController childController = new ViewChildController(viewChildScreen,
				uiStack, formStore);

		SyncController syncController = new SyncController(syncScreen, uiStack,
				childSyncService, formService);

		Dispatcher dispatcher = new Dispatcher(homeScreenController,
				loginController, childController, syncController,
				restController, contactScreenController,
				new ManageChildController(newChildScreen, uiStack, formStore,
						childrenStore), new ViewChildrenController(
						viewChildrenScreen, uiStack, childrenStore),
				new ViewChildPhotoController(childPhotoScreen, uiStack),
				new ChildHistoryController(childHistoryScreen, uiStack),
				new SearchChildController(searchChildScreen, uiStack,
						childrenStore));

		dispatcher.homeScreen();

	}

    private boolean makePermissionsRequest(int[] requiredPermissions) {
        ApplicationPermissionsManager applicationPermissionsManager = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions currentPermissions = applicationPermissionsManager.getApplicationPermissions();
        ApplicationPermissions permissionsToRequest = new ApplicationPermissions();
        for(int i = 0; i < requiredPermissions.length; i++) {
            int permission = requiredPermissions[i];
            if (currentPermissions.getPermission(permission) != ApplicationPermissions.VALUE_ALLOW)
                permissionsToRequest.addPermission(permission);
        }

        if (permissionsToRequest.getPermissionKeys().length > 0)
            return applicationPermissionsManager.invokePermissionsRequest(permissionsToRequest);
        return true;
    }
}
