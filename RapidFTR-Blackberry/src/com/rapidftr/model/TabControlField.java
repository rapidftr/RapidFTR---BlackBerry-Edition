package com.rapidftr.model;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.layouts.TabAction;
import com.rapidftr.layouts.Tabs;

public class TabControlField extends VerticalFieldManager {

	private final TabControl tabControl;
	private final TabBody tabArea;

	public TabControlField(Tabs tabs) {
		super(VerticalFieldManager.USE_ALL_WIDTH);
		
		tabControl = new TabControl(this);
		tabArea = new TabBody();
		
		tabs.forEachTab(new TabAction() {
			public void execute(Tab tab) {
				tabControl.add(tab);
			}
		});
		add(new SeparatorField());
		add(this.tabArea);
	}
	
	public String getSelectedTab() {
		return getCurrentTabField().getText().trim();
	}

	private TabHandleField getCurrentTabField() {
		return tabControl.getCurrentTab();
	}
	
	public void show(Manager tabManager){
		tabArea.show(tabManager);
	}
}
