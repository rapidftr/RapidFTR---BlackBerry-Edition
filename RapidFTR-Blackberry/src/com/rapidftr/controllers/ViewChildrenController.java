package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.UiStack;

public class ViewChildrenController extends Controller {
	private final ChildrenRecordStore store;
	private int sortState;
	private final int SORT_NAME = 0;
	private final int SORT_ADDED = 1;
	private final int SORT_UPDATED = 2;

	public ViewChildrenController(ViewChildrenScreen screen, UiStack uiStack, ChildrenRecordStore store) {
		super(screen, uiStack);
		this.store = store;
		this.sortState = SORT_NAME;
	}

	public void viewAllChildren() {
		uiStack.clear();
		switch(sortState){
			case SORT_NAME:
				sortByName();
				break;
			case SORT_ADDED:
				sortByRecentlyAdded();
				break;
			case SORT_UPDATED:
				sortByRecentlyUpdated();
				break;
		}
	}

	public void viewChildren(Children children) {
		getViewChildrenScreen().setChildren(children);
		show();
	}

	private ViewChildrenScreen getViewChildrenScreen(){
		return (ViewChildrenScreen)currentScreen;
	}

	public void viewChild(Child child) {
		dispatcher.viewChild(child);
	}
	

	public void sortByName() {
		this.sortState = this.SORT_NAME;
		viewChildren(store.getAllSortedByName());
	}

	public void sortByRecentlyAdded() {
		this.sortState = this.SORT_ADDED;
		viewChildren(store.getAllSortedByRecentlyAdded());
	}

	public void sortByRecentlyUpdated() {
		this.sortState = this.SORT_UPDATED;
		viewChildren(store.getAllSortedByRecentlyUpdated());
	}
	
	public void popScreen() {
		this.sortState = SORT_NAME;
		((ViewChildrenScreen)currentScreen).refresh();
		homeScreen();
	}

	public Child getChildAt(int selectedIndex) {
		return store.getChildAt(selectedIndex);
	}

}
