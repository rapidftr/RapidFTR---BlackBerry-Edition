package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;

public class ManageChildController extends Controller {

    private final FormStore store;
    private ChildrenRecordStore childRecordStore;

    public ManageChildController(ManageChildScreen screen,
                                 UiStack uiStack,
                                 FormStore store,
                                 ChildrenRecordStore childRecordStore,
                                 Dispatcher dispatcher) {
        super(screen, uiStack, dispatcher);
        this.store = store;
        this.childRecordStore = childRecordStore;
    }

    public void editChild(Child child) {
        getManageChildScreen().setEditForms(store.getForms(), child);
        show();
    }

    public void newChild() {
        getManageChildScreen().setForms(store.getForms());
        show();
    }

    private ManageChildScreen getManageChildScreen() {
        return ((ManageChildScreen) currentScreen);
    }

    public void takeSnapshotAndUpdateWithNewImage(ImageCaptureListener imageCaptureListener) {
        SnapshotController snapshotController = new SnapshotController(new SnapshotScreen(),
                uiStack,
                dispatcher,
                imageCaptureListener);

        snapshotController.show();
    }

    public void saveChild(Child child) {
        childRecordStore.addOrUpdate(child);
    }

    public void viewChild(Child child) {
        dispatcher.viewChild(child);
    }

}
