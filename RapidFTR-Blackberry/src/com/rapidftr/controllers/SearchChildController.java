package com.rapidftr.controllers;

import com.rapidftr.model.SearchChildFilter;
import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.UiStack;

public class SearchChildController extends Controller {
	
	public SearchChildController(CustomScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}

	public void search(SearchChildFilter searchChildFilter) {
		dispatcher.searchAndDisplayChildren(searchChildFilter);
	}

	public void show()
	{
			((SearchChildScreen) screen).showSearch();
			super.show();
	}
	
}
