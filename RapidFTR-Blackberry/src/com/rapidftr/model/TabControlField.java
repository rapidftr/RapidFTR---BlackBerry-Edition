package com.rapidftr.model;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TabControlField extends VerticalFieldManager implements FocusChangeListener{

	private Tab currentTab;
	private HorizontalFieldManager labelArea = new HorizontalFieldManager(HORIZONTAL_SCROLL);
	private VerticalFieldManager tabBody = new VerticalFieldManager();

	public TabControlField(Tabs tabs) {
		super(USE_ALL_WIDTH);
		final TabControlField self = this;
		tabs.forEachTab(new TabAction(){
			public void execute(Tab tab) {
				tab.setCanvas(self);
			}
		});
		currentTab = tabs.getDefaultTab();
		currentTab.open();
		tabs.addFocusChangeObserver(this);
		add(labelArea);
		add(tabBody);
	}
	
	public String getSelectedTab() {
		return currentTab.getLabel().trim();
	}
	
	public void addHandle(TabHandleField handle){
		labelArea.add(handle);
	}
	
	public void setBody(TabBodyField body){
		tabBody.deleteAll();
		tabBody.add(body);
	}

	public void focusChanged(Field field, int eventType) {
		if(field instanceof Tab && eventType == FOCUS_GAINED){
			currentTab.close();
		    ((Tab)field).open();
		    currentTab = (Tab)field;
		}
	}

	public void clearBody() {
		tabBody.deleteAll();
	}
}
