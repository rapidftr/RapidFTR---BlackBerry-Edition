package com.rapidftr.screens;

import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.datastore.Children;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildrenListField;
import com.rapidftr.screens.internal.CustomScreen;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

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
		childrenList = new ChildrenListField(){
            public ViewChildrenController getViewChildController() {
                return getController();
            }
        };
		add(childrenList);
	}

	public void setChildren(Children children) {
		Child[] childArray = children.toArray(); 
		Object[] childrenAndImages = new Object[children.count()];
		for (int i = 0; i < childArray.length; i++) {
			Object[] childImagePair = new Object[2];
			Child child = childArray[i];
			Bitmap image = child.getThumbnail();
			childImagePair[0] = child;
			childImagePair[1] = image;
			childrenAndImages[i] = childImagePair;
			
		}
		childrenList.set(childrenAndImages);
		childrenList.setRowHeight(ROW_HEIGHT);
	}

	private ViewChildrenController getController() {
		return (ViewChildrenController) controller;
	}

	protected void makeMenu(Menu menu, int instance) {
		if (!childrenList.isEmpty()) {
			MenuItem editChildMenu = new MenuItem("Open Record", 1, 1) {
				public void run() {
					Child child = childrenList.getSelectedChild();
					getController().viewChild(child);
				}

			};
			menu.add(editChildMenu);

			MenuItem sortByName = new MenuItem(
					"Sort by Name", 1, 1) {
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
		}
		super.makeMenu(menu, instance);
	}


}
