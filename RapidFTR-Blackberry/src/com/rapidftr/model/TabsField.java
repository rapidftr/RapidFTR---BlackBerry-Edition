package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TabsField {

	private VerticalFieldManager control;
	private HorizontalFieldManager labelArea;
	private VerticalFieldManager tabBody;

	private Tab currentTab;
	private Vector tabs = new Vector();

	public TabsField() {
		this(new VerticalFieldManager(VerticalFieldManager.USE_ALL_WIDTH),
			 new HorizontalFieldManager(HorizontalFieldManager.HORIZONTAL_SCROLL),
			 new VerticalFieldManager());
	}

	protected TabsField(VerticalFieldManager control,
						HorizontalFieldManager labelArea,
						VerticalFieldManager tabBody) {
		this.control = control;
		this.labelArea = labelArea;
		this.tabBody = tabBody;
		control.add(labelArea);
		control.add(tabBody);
	}

	private void selectDefaultTab() {
		currentTab = getDefaultTab();
		currentTab.open();
	}

	public String getSelectedTab() {
		return currentTab.getLabel();
	}

	public void addHandle(TabLabelField handle) {
		labelArea.add(handle);
	}

	public void setBody(TabBodyField body) {
		tabBody.deleteAll();
		tabBody.add(body);
	}

	public void clearBody() {
		tabBody.deleteAll();
	}

	public void addTab(final Tab tab) {
		tabs.addElement(tab);
		tab.setCanvas(this);
		tab.addTabChangeListener(new FocusChangeListener() {
			public void focusChanged(Field field, int eventType) {
				if (field instanceof TabLabelField && eventType == FOCUS_GAINED) {
					currentTab.close();
					tab.open();
					currentTab = tab;
				}
			}
		});
	}

	private Tab getDefaultTab() {
		return (tabs.isEmpty()) ? null : (Tab) tabs.elementAt(0);
	}

	public Field draw() {
		selectDefaultTab();
		return control;
	}
}
