package com.rapidftr;

import com.rapidftr.controllers.*;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormJsonParser;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.*;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.*;
import com.rapidftr.utilities.*;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

import java.util.Calendar;

public class Main extends UiApplication {
    public boolean permissionsGranted = false;

    public static void main(String[] args) {
        Main application = new Main();
        Logger.register();
        application.enterEventDispatcher();
    }

    public Main() {

        UiStack uiStack = new UiStack(this);
        int[] requiredPermissions = new int[]{
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

        FormStore formStore = new FormStore(new FormJsonParser());

        Settings settings = new Settings(defaultStore);

        HttpServer httpServer = new HttpServer(new HttpSettings(settings));

        HttpService httpService = new HttpService(httpServer, settings);

        LoginService loginService = new LoginService(httpService,
                new LoginSettings(settings));

        FormService formService = new FormService(httpService, formStore);

        DefaultBlackBerryDateFormat defaultDateFormat = new DefaultBlackBerryDateFormat();
        DateFormatter dateFormatter = new DateFormatter(Calendar.getInstance().getTimeZone(), defaultDateFormat);

        ChildSyncService childSyncService = new ChildSyncService(
                httpService,
                childrenStore,
                dateFormatter);

        ContactInformationScreen contactScreen = new ContactInformationScreen(
                new ContactInformation(defaultStore));

        LoginScreen loginScreen = new LoginScreen(new HttpSettings(settings));

        ViewChildScreen viewChildScreen = new ViewChildScreen();

        SearchChildScreen searchChildScreen = new SearchChildScreen();

        SyncScreen syncScreen = new SyncScreen(settings);

        LoginController loginController = new LoginController(loginScreen,
                uiStack, loginService);

        ResetDeviceController restController = new ResetDeviceController(
                formService, childSyncService, loginService);

        ContactInformationController contactScreenController = new ContactInformationController(
                contactScreen, uiStack, new ContactInformationSyncService(
                        httpService, new ContactInformation(defaultStore)));

        ChildHistoryScreen childHistoryScreen = new ChildHistoryScreen(dateFormatter);

        ViewChildController childController = new ViewChildController(viewChildScreen,
                uiStack, formStore);

        SyncController syncController = new SyncController(syncScreen, uiStack,
                childSyncService, formService);

        SearchChildController searchChildController = new SearchChildController(searchChildScreen, uiStack,
                childrenStore);
        ChildHistoryController showHistoryController = new ChildHistoryController(childHistoryScreen, uiStack);

        Dispatcher dispatcher = new Dispatcher(
                loginController,
                childController,
                syncController,
                restController,
                contactScreenController,
                showHistoryController,
                searchChildController,
                settings,
                uiStack,
                dateFormatter,
                formStore,
                childrenStore);

        dispatcher.homeScreen();

    }

    private boolean makePermissionsRequest(int[] requiredPermissions) {
        ApplicationPermissionsManager applicationPermissionsManager = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions currentPermissions = applicationPermissionsManager.getApplicationPermissions();
        ApplicationPermissions permissionsToRequest = new ApplicationPermissions();
        for (int i = 0; i < requiredPermissions.length; i++) {
            int permission = requiredPermissions[i];
            if (currentPermissions.getPermission(permission) != ApplicationPermissions.VALUE_ALLOW)
                permissionsToRequest.addPermission(permission);
        }

        if (permissionsToRequest.getPermissionKeys().length > 0)
            return applicationPermissionsManager.invokePermissionsRequest(permissionsToRequest);
        return true;
    }
}
