package com.rapidftr.model;

import java.util.Hashtable;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

class TabControl implements FocusChangeListener{

	private final TabControlField tabControlField;
	private Hashtable tabMap = new Hashtable();
	private HorizontalFieldManager labelArea;
	private TabHandleField currentTab;

	protected TabControl(TabControlField tabControl, HorizontalFieldManager labelArea) {
		this.tabControlField = tabControl;
		this.labelArea = labelArea;
		this.tabControlField.add(labelArea);
	}

	public TabHandleField getCurrentTab() {
		return currentTab;
	}

	public TabControl(TabControlField tabControl) {
		this(tabControl, new HorizontalFieldManager(
				HorizontalFieldManager.HORIZONTAL_SCROLL));
	}

	public void add(Tab tab) {
		TabHandleField tabHandle = new TabHandleField(tab.getLabel());
		VerticalFieldManager tabManager = prepareTab(tab);
		if(tabMap.isEmpty()){
			setDefaults(tabHandle, tabManager);
		}
		tabMap.put(tabHandle, tabManager);
		tabHandle.setFocusListener(this);
		labelArea.add(tabHandle);
	}

	public void focusChanged(Field field, int eventType) {
		if (eventType == FOCUS_GAINED) {
			if (field instanceof TabHandleField) {
				switchToSelectedTab((TabHandleField)field);
			}
		}		
	}

	private void switchToSelectedTab(TabHandleField field) {
		currentTab.deSelect();
		tabControlField.show((Manager)tabMap.get(field));
		field.select();
		currentTab = field;
	}

	private VerticalFieldManager prepareTab(Tab tab) {
		VerticalFieldManager tabManager = new VerticalFieldManager();
		tab.render(tabManager);
		return tabManager;
	}
	
	private void setDefaults(TabHandleField tabHandle, Manager tabManager){
		currentTab = tabHandle;
		currentTab.select();
		tabControlField.show(tabManager);
	}
	
}