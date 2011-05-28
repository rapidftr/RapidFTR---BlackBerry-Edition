package com.rapidftr;

import com.rapidftr.controllers.*;
import com.rapidftr.controllers.internal.ControllerFactory;
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
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.UiApplication;

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

        ControllerFactory controllerFactory = new ControllerFactory(uiStack);
        Dispatcher dispatcher = new Dispatcher(controllerFactory);
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
