package com.rapidftr.model;

import java.util.Hashtable;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.layouts.TabAction;
import com.rapidftr.layouts.Tabs;

public class TabControlField extends VerticalFieldManager {

	private final TabControl tabHandles;
	private final TabBodyField tabArea;

	public TabControlField(Tabs tabs) {
		super(VerticalFieldManager.USE_ALL_WIDTH);
		
		tabHandles = new TabControl(this);
		tabArea = new TabBodyField();
		
		tabs.forEachTab(new TabAction() {
			public void execute(Tab tab) {
				tabHandles.add(tab);
			}
		});
		add(new SeparatorField());
		add(this.tabArea);
	}
	
	public String getSelectedTab() {
		return getCurrentTabField().getText().trim();
	}

	private TabHandleField getCurrentTabField() {
		return tabHandles.getCurrentTab();
	}
	
	public void show(Manager tabManager){
		tabArea.show(tabManager);
	}
}

class TabControl implements FocusChangeListener{

	private final TabControlField tabControl;
	private HorizontalFieldManager labelArea;
	private Hashtable tabMap = new Hashtable();
	private TabHandleField currentTab;

	protected TabControl(TabControlField tabControl, HorizontalFieldManager labelArea) {
		this.tabControl = tabControl;
		this.labelArea = labelArea;
		this.tabControl.add(labelArea);
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
		tabControl.show((Manager)tabMap.get(field));
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
		tabControl.show(tabManager);
	}
	
}

class TabHandleField extends LabelField {

	public TabHandleField(String labelText) {
		super("", LabelField.FOCUSABLE);
		setText(prepareTabLabelForDisplay(labelText));
		setBorder(getBorders());
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
	}

	public void deSelect() {
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
	}

	public void select() {
		setBackground(BackgroundFactory.createLinearGradientBackground(
				Color.ROYALBLUE, Color.ROYALBLUE, Color.BLUE, Color.BLUE));
	}

	protected void paint(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		super.paint(graphics);
	}

	private String prepareTabLabelForDisplay(String initialLabel) {
		return " " + initialLabel + " ";
	}

	private Border getBorders() {
		XYEdges labelBorderSizes = new XYEdges(1, 1, 0, 1);
		Border labelBorders = BorderFactory
				.createSimpleBorder(labelBorderSizes);
		return labelBorders;
	}

}

class TabBodyField extends VerticalFieldManager {
	
	private Manager currentTabManager;

	protected void paint(Graphics graphics) {
		setBackgroundColor(graphics);
	}

	public void show(Manager tabManager) {
		clear();
		add(tabManager);
		currentTabManager = tabManager;
	}

	private void clear() {
		if(null != currentTabManager){
			delete(currentTabManager);
		}
	}

	private void setBackgroundColor(Graphics graphics) {
		int originalAlpha = graphics.getGlobalAlpha();
		graphics.setGlobalAlpha(50);
		graphics.setBackgroundColor(Color.GRAY);
		graphics.clear();
		graphics.setGlobalAlpha(originalAlpha);
		super.paint(graphics);
	}
}
