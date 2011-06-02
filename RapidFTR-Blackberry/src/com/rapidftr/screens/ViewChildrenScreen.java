package com.rapidftr.screens;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.ScrollChangeListener;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.controls.ChildrenListField;
import com.rapidftr.datastore.Children;
import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;

public class ViewChildrenScreen extends CustomScreen {

	private static final int ROW_HEIGHT = 100;
	private ChildrenListField childrenList;

	public ViewChildrenScreen() {
		super();
		layoutScreen();
	}

	private void layoutScreen() {
		add(new LabelField("All children"));
		add(new SeparatorField());
		childrenList = new ChildrenListField() {
			public ViewChildrenController getViewChildController() {
				return getController();
			}
		};
		getMainManager().setScrollListener(new ScrollChangeListener() {

			public void scrollChanged(Manager manager, int newHorizontalScroll,
					int newVerticalScroll) {
				childrenList.addChild();
			}
		});
		add(childrenList);
	}

	public void setChildren(Children children) {
		childrenList.displayChildren(children);
		childrenList.setRowHeight(ROW_HEIGHT);
	}

	private ViewChildrenController getController() {
		return (ViewChildrenController) controller;
	}

	protected void makeMenu(Menu menu, int instance) {
		if (!childrenList.isEmpty()) {
			final Child child = childrenList.getSelectedChild();
			MenuItem editChildMenu = new MenuItem("Open Record", 1, 1) {
				public void run() {
					getController().viewChild(child);
				}

			};
			menu.add(editChildMenu);

			MenuItem sortByName = new MenuItem("Sort by Name", 1, 1) {
				public void run() {
					getController().sortByName();
				}
			};
			menu.add(sortByName);

			MenuItem sortByRecentlyAdded = new MenuItem(
					"Sort by Recently Added", 1, 1) {
				public void run() {
					getController().sortByRecentlyAdded();
				}
			};
			menu.add(sortByRecentlyAdded);

			MenuItem sortByRecentlyModified = new MenuItem(
					"Sort by Recently Updated", 1, 1) {
				public void run() {
					getController().sortByRecentlyUpdated();
				}
			};
			menu.add(sortByRecentlyModified);
			
			MenuItem flagRecordAsSuspectMenu = null;
	        if("true".equals(child.getField(Child.FLAGGED_KEY))){
	        	flagRecordAsSuspectMenu = new MenuItem("Flag Information", 1, 1) {
	    			public void run() {
	    				Dialog.alert(child.flagInformation());
	    			}
	    		};
	        }
	        if(flagRecordAsSuspectMenu != null)
	        	menu.add(flagRecordAsSuspectMenu);
		}
		super.makeMenu(menu, instance);
	}

	public void refresh() {
		childrenList.refresh();
	}

}
