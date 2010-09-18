package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.model.SearchChildFilter;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

public class SearchChildController extends Controller {
	
	public SearchChildController(CustomScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}

	public void search(SearchChildFilter searchChildFilter) {
		dispatcher.searchAndDisplayChildren(searchChildFilter);
	}
	
}
