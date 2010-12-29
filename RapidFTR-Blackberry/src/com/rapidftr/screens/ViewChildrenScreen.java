package com.rapidftr.screens;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.datastore.Children;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildrenListField;
import com.rapidftr.screens.internal.CustomScreen;

public class ViewChildrenScreen extends CustomScreen {

	private ChildrenListField field;

	public ViewChildrenScreen() {
		super();
		layoutScreen();
	}

	private void layoutScreen() {
		add(new LabelField("All children"));
		add(new SeparatorField());
		field = new ChildrenListField() {
			protected boolean navigationClick(int i, int i1) {
				if (this.getSelectedIndex() > 0) {
					Object child = this.get(this, this.getSelectedIndex());
					if (child instanceof Child) {
						((ChildController) controller).viewChild((Child) child);
						return super.navigationClick(i, i1);
					}
				}
				return false;
			}
		};
		add(field);
	}

	public void setChildren(Children children) {
		field.set(children.toArray());
	}

	public void cleanUp() {

	}

	public void setUp() {

	}

	private ChildController getController() {
		return (ChildController) controller;
	}

	protected void makeMenu(Menu menu, int instance) {
		if (!field.isEmpty()) {
			MenuItem editChildMenu = new MenuItem("Open Record", 1, 1) {
				public void run() {
					int selectedIndex = field.getSelectedIndex();
					Child child = (Child) field.get(field, selectedIndex);
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
