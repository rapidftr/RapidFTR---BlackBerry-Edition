package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.UiStack;

public class ViewChildrenController extends Controller {
	private final ChildrenRecordStore store;

	public ViewChildrenController(ViewChildrenScreen screen, UiStack uiStack, ChildrenRecordStore store) {
		super(screen, uiStack);
		this.store = store;
	}

	public void viewAllChildren() {
		uiStack.clear();
		viewChildren(store.getAll());
	}

	public void viewChildren(Children children) {
		getViewChildrenScreen().setChildren(children.getChildrenAndImages());
		show();
	}

	private ViewChildrenScreen getViewChildrenScreen(){
		return (ViewChildrenScreen)currentScreen;
	}

	public void viewChild(Child child) {
		dispatcher.viewChild(child);
	}

	public void sortByName() {
		viewChildren(store.getAllSortedByName());
	}

	public void sortByRecentlyAdded() {
		viewChildren(store.getAllSortedByRecentlyAdded());
	}

	public void sortByRecentlyUpdated() {
		viewChildren(store.getAllSortedByRecentlyUpdated());
	}
	
	public void popScreen() {
		homeScreen();
	}
}
