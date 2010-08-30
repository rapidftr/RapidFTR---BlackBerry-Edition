package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;
import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.ChildStoreService;

public class SearchChildController extends Controller {
	
	private final ChildStoreService childStoreService;
	public SearchChildController(CustomScreen screen, UiStack uiStack, ChildStoreService childStoreService) {
		super(screen, uiStack);
		this.childStoreService=childStoreService;
	}

	public void search(SearchChildFilter searchChildFilter) {
		
		Child child[] = childStoreService.searchChild(searchChildFilter);
		dispatcher.viewSearchResults(child);
		
	}
	public void show()
	{
			((SearchChildScreen) screen).showSearch();
			super.show();
	}
	
}
