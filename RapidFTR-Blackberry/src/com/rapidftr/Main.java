package com.rapidftr;

import java.io.IOException;
import java.util.Vector;

import com.rapidftr.controllers.*;
import com.rapidftr.controls.Button;
import com.rapidftr.datastore.ChildRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.*;
import com.rapidftr.services.ChildService;
import com.rapidftr.services.ChildService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.LoginService;
import com.rapidftr.services.LoginService;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Request;
import com.sun.me.web.request.Response;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

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
		enableEventInjection();

		SettingsStore settings = new SettingsStore();

		settings.setAuthorisationToken("invalid");
		// System.out.println("***Authorization key from Persistent Store***");
		// System.out.println(settings.getAuthorizationToken());
		// System.out.println("*********");

		FormStore formStore = new FormStore();

		 System.out.println("*******Form******");
		 Vector v = formStore.getForms();
		 System.out.println(v);

		ChildRecordStore childRecordStore = new ChildRecordStore();

		Vector children = childRecordStore.getAllChildren();

		System.out.println(children);
		HttpServer httpServer = new HttpServer(settings);
		HttpService httpService = new HttpService(httpServer);

		LoginService loginService = new LoginService(httpService);
		ChildService childService = new ChildService(httpService);
		UiStack uiStack = new UiStack(this);

		LoginScreen loginScreen = new LoginScreen(settings);
		LoginController loginController = new LoginController(loginScreen,
				uiStack, loginService, settings);
		loginScreen.setController(loginController);

		HomeScreen homeScreen = new HomeScreen();
		HomeScreenController homeScreenController = new HomeScreenController(
				homeScreen, uiStack);

		ViewChildScreen viewChildScreen = new ViewChildScreen();
		ViewChildController viewChildController = new ViewChildController(
				viewChildScreen, uiStack);

		ViewChildrenScreen viewChildrenScreen = new ViewChildrenScreen();
		ViewChildrenController viewChildrenController = new ViewChildrenController(
				viewChildrenScreen, uiStack, childService);

		SynchronizeFormsScreen synchronizeFormsScreen = new SynchronizeFormsScreen();
		SynchronizeFormsController synchronizeFormsController = new SynchronizeFormsController(
				new FormService(httpService), formStore, uiStack,
				synchronizeFormsScreen);

		NewChildScreen newChildScreen = new NewChildScreen();
		NewChildController newChildController = new NewChildController(
				newChildScreen, uiStack, formStore, childRecordStore);
		Dispatcher dispatcher = new Dispatcher(homeScreenController,
				loginController, viewChildrenController, viewChildController,
				synchronizeFormsController, newChildController);

		dispatcher.homeScreen();

	}

	private void enableEventInjection() {
		ApplicationPermissionsManager manager = ApplicationPermissionsManager
				.getInstance();

		int permission = ApplicationPermissions.PERMISSION_EVENT_INJECTOR;

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
