package com.rapidftr.layouts;

import java.util.Vector;

import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.model.Tab;

public class Tabs {

	private final Child child;
	private Vector tabs = new Vector();

	public Tabs(Child child) {
		this.child = child;
	}

	public void addForm(Form form) {
		tabs.addElement(new Tab(form.toString(), form, child));
	}

	public Tab[] getTabs() {
		final Tab[] tabsArray = new Tab[tabs.size()];
		tabs.copyInto(tabsArray);
		return tabsArray;
	}

}
