package com.rapidftr.controllers.internal;

import com.rapidftr.Key;
import com.rapidftr.controllers.*;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormJsonParser;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.ContactInformation;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.*;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.*;
import com.rapidftr.utilities.*;

import java.util.Calendar;

public class ControllerFactory {

    private final Settings settings;
    private final UiStack uiStack;
    private final DateFormatter dateFormatter;
    private final FormStore formStore;
    private final ChildrenRecordStore childrenRecordStore;
    private final HttpSettings httpSettings;
    private final LoginService loginService;
    private final ChildSyncService childSyncService;
    private final FormService formService;
    private Store store;
    private ContactInformationSyncService contactInformationSyncService;

    public ControllerFactory(UiStack uiStack) {
        this.uiStack = uiStack;

        store = new DefaultStore(new Key("com.rapidftr.utilities.ftrstore"));
        childrenRecordStore = new ChildrenRecordStore(
                new DefaultStore(new Key("com.rapidftr.utilities.childrenstore")));

        formStore = new FormStore(new FormJsonParser());
        settings = new Settings(store);
        httpSettings = new HttpSettings(settings);

        HttpService httpService = new HttpService(new HttpServer(httpSettings), settings);

        loginService = new LoginService(httpService, new LoginSettings(settings));
        formService = new FormService(httpService, formStore);
        dateFormatter = new DateFormatter(Calendar.getInstance().getTimeZone(), new DefaultBlackBerryDateFormat());

        childSyncService = new ChildSyncService(httpService, childrenRecordStore, dateFormatter);
        contactInformationSyncService = new ContactInformationSyncService(httpService, new ContactInformation(store));
    }

    public HomeController homeScreenControllerWith(Dispatcher dispatcher) {
        HomeScreen homeSreen = new HomeScreen(settings);
        return new HomeController(homeSreen, uiStack, settings, dispatcher);
    }

    public ManageChildController manageChildControllerWith(Dispatcher dispatcher) {
        ManageChildScreen manageChildScreen = new ManageChildScreen(dateFormatter);
        return new ManageChildController(manageChildScreen, uiStack, formStore, childrenRecordStore, dispatcher);
    }

    public ViewChildrenController viewChildrenControllerWith(Dispatcher dispatcher) {
        ViewChildrenScreen viewChildrenScreen = new ViewChildrenScreen();
        return new ViewChildrenController(viewChildrenScreen, uiStack, childrenRecordStore, dispatcher);
    }

    public ViewChildPhotoController viewChildPhotoControllerWith(Dispatcher dispatcher) {
        ChildPhotoScreen childPhotoScreen = new ChildPhotoScreen();
        return new ViewChildPhotoController(childPhotoScreen, uiStack, dispatcher);
    }

    public ChildHistoryController childHistoryControllerWith(Dispatcher dispatcher) {
        ChildHistoryScreen childHistoryScreen = new ChildHistoryScreen(dateFormatter);
        return new ChildHistoryController(childHistoryScreen, uiStack, dispatcher);
    }

    public SearchChildController searchChildControllerWith(Dispatcher dispatcher) {
        SearchChildScreen searchChildScreen = new SearchChildScreen();
        return new SearchChildController(searchChildScreen, uiStack, childrenRecordStore, dispatcher);
    }

    public LoginController loginControllerWith(Dispatcher dispatcher) {
        LoginScreen loginScreen = new LoginScreen(httpSettings);
        return new LoginController(loginScreen, uiStack, loginService, new ConnectionFactory(), dispatcher);
    }

    public ViewChildController viewChildControllerWith(Dispatcher dispatcher) {
        ViewChildScreen viewChildScreen = new ViewChildScreen();
        return new ViewChildController(viewChildScreen, uiStack, dispatcher, formStore );
    }

    public SyncController syncControllerWith(Dispatcher dispatcher) {
        SyncScreen syncScreen = new SyncScreen(settings);
        return new SyncController(syncScreen, uiStack, childSyncService, formService, dispatcher);
    }

    public ResetDeviceController resetDeviceController() {
        return new ResetDeviceController(formService, childSyncService, loginService);
    }

    public ContactInformationController contactScreenControllerWith(Dispatcher dispatcher) {
        ContactInformationScreen contactScreen = new ContactInformationScreen(new ContactInformation(store));
        return new ContactInformationController(contactScreen, uiStack, contactInformationSyncService, dispatcher);
    }
}
