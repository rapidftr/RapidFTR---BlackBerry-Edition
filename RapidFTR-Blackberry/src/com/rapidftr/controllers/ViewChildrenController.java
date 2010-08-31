package com.rapidftr.controllers;


import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.Comparator;

import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.services.ChildStoreService;

public class ViewChildrenController extends Controller {

	private final ChildStoreService childStoreService;

	public ViewChildrenController(ViewChildrenScreen screen, UiStack uiStack,
			ChildStoreService childStoreService) {
		super(screen, uiStack);
		this.childStoreService = childStoreService;
	}

	public void show() {
		
		Child[] children;

		children = childStoreService.getAllChildrenFromPhoneStoredAsArray();
		sortByLocationThenName(children);
		((ViewChildrenScreen) screen).setChildren(children);
		
		//edit the below line to super();
		uiStack.pushScreen(screen);

	}
	
	public void sortByLocationThenName(Child[] children) {
		Arrays.sort(children, new Comparator() {
			
			public int compare(Object o1, Object o2) {
				Child child1 = (Child)o1;
				Child child2 = (Child)o2;
				int locationComparator = ((String) child1.getField("last_known_location")).compareTo((String) child2.getField("last_known_location"));

				if (locationComparator == 0) {
					return ((String) child1.getField("name")).compareTo((String) child2.getField("name"));
				} else {
				return locationComparator;
				}
			}
		});
	}

	public void showChild(Child child) {
		dispatcher.viewChild(child);
	}



	public Child[] getAllStoredChildren()
	{
		return childStoreService.getAllChildrenFromPhoneStoredAsArray();
	
	}

	public void searchAndDispalyChildren(SearchChildFilter searchChildFilter) {
		Child children[] = childStoreService.searchChild(searchChildFilter);
		((ViewChildrenScreen) screen).setChildren(children);
		super.show();
		}

}
