package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TabsField extends VerticalFieldManager {

	private final Child child;

	private Tab currentTab;
	private Vector tabs = new Vector();
	private HorizontalFieldManager labelArea = new HorizontalFieldManager(
			HORIZONTAL_SCROLL);
	private VerticalFieldManager tabBody = new VerticalFieldManager();

	public TabsField(Child child) {
		super(USE_ALL_WIDTH);
		this.child = child;
		add(labelArea);
		add(tabBody);
	}

	private void selectDefaultTab() {
		currentTab = getDefaultTab();
		currentTab.open();
	}

	public String getSelectedTab() {
		return currentTab.getLabel().trim();
	}

	public void addHandle(TabHandleField handle) {
		labelArea.add(handle);
	}

	public void setBody(TabBodyField body) {
		tabBody.deleteAll();
		tabBody.add(body);
	}

	public void clearBody() {
		tabBody.deleteAll();
	}

	public void addTab(Form form) {
		Tab tab = new Tab(form.toString(), form, child);
		tabs.addElement(tab);
		tab.setCanvas(this);
		tab.addTabChangeListener(new FocusChangeListener() {
			public void focusChanged(Field field, int eventType) {
				if (field instanceof Tab && eventType == FOCUS_GAINED) {
					currentTab.close();
					((Tab) field).open();
					currentTab = (Tab) field;
				}
			}
		});
	}

	private Tab getDefaultTab() {
		return (tabs.isEmpty()) ? null : (Tab) tabs.elementAt(0);
	}

	protected void onDisplay() {
		selectDefaultTab();
		super.onDisplay();
	}
}
