package com.rapidftr.screens;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildrenListField;
import com.rapidftr.screens.internal.CustomScreen;

public class ViewChildrenScreen extends CustomScreen {

	private ChildrenListField childrenListField;
	private int numberOfChildren;

	public ViewChildrenScreen() {
		super();
		layoutScreen();
	}

	private void layoutScreen() {
		add(new LabelField("All children"));
		add(new SeparatorField());
		childrenListField = new ChildrenListField() {
			protected boolean navigationClick(int i, int i1) {
				if (this.getSelectedIndex() > 0) {
					Object child = this.get(this, this.getSelectedIndex());
					if (child instanceof Child) {
						((ChildController) controller)
								.viewChild((Child) child);
						return super.navigationClick(i, i1);
					}
				}
				return false;
			}
		};
		add(childrenListField);
	}

	public void setChildren(Child[] children) {
		numberOfChildren = children.length;
		childrenListField.set(children);
	}

	public void cleanUp() {

	}

	public void setUp() {

	}

	protected void makeMenu(Menu menu, int instance) {
		if (numberOfChildren > 0) {
			MenuItem editChildMenu = new MenuItem("Open Record", 1, 1) {
				public void run() {
					int selectedIndex = childrenListField.getSelectedIndex();
					Child child = (Child) childrenListField.get(childrenListField, selectedIndex);
					((ChildController) controller).viewChild(child);
				}
			};
			menu.add(editChildMenu);
		}
		super.makeMenu(menu, instance);
	}

}
