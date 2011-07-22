package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.FlagReasonScreen;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.internal.UiStack;

public class ViewChildController extends Controller {

	private ViewChildScreen viewChildScreen;
	private FormStore formStore;

	public ViewChildController(ViewChildScreen viewChildScreen, UiStack uiStack,
			FormStore formStore, Dispatcher dispatcher) {

        super(viewChildScreen, uiStack, dispatcher);
		this.viewChildScreen = viewChildScreen;
		this.formStore = formStore;
	}

	public void syncChild(Child child) {
		dispatcher.syncChild(child);
	}

	public void viewChild(Child child) {
		viewChildScreen.setChild(child, formStore.getForms());
		show();
	}

	public void editChild(Child child, String selectedTab) {
		dispatcher.editChild(child, selectedTab);
    }

	public void viewChildPhoto(Child child) {
		dispatcher.viewChildPhoto(child);
	}

	public void showHistory(Child child) {
		dispatcher.showHistory(child);
	}
	
	public void popScreen() {
		dispatcher.viewChildren();
	}

	public void flagRecord(Child child) {
		dispatcher.flagRecord(child);
	}

}
