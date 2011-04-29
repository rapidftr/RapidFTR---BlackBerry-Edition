package com.rapidftr.model;

import com.rapidftr.layouts.Tabs;

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
import net.rim.device.api.util.Arrays;

public class TabControl extends VerticalFieldManager implements
		FocusChangeListener {

	private final Tab[] listOfTabs;
	private final LabelField[] listOfLabels;
	private final HorizontalFieldManager labelArea;
	private final VerticalFieldManager tabArea;
	private final VerticalFieldManager[] tabManagers;
	private int currentTab = -1;

	public TabControl(Tab[] listOfTabsToControl) {
		
		super(VerticalFieldManager.USE_ALL_WIDTH);
		
		this.listOfTabs = listOfTabsToControl;
		this.listOfLabels = new LabelField[this.listOfTabs.length];
		this.labelArea = new HorizontalFieldManager(HORIZONTAL_SCROLL);
		this.tabArea = new VerticalFieldManager(){
			protected void paint(Graphics graphics) {
				int originalAlpha = graphics.getGlobalAlpha();
				graphics.setGlobalAlpha(50);
			    graphics.setBackgroundColor(Color.GRAY);
			    graphics.clear();
			    graphics.setGlobalAlpha(originalAlpha);
				super.paint(graphics);
			}
		};

		this.tabManagers = new VerticalFieldManager[this.listOfTabs.length];
		for (int i = 0; i < this.tabManagers.length; i++) {
			this.tabManagers[i] = new VerticalFieldManager();
		}

		for (int i = 0; i < this.listOfTabs.length; i++) {
			String labelText = this.prepareTabLabelForDisplay(this.listOfTabs[i].getLabel());
			LabelField tabLabel = new LabelField(labelText,LabelField.FOCUSABLE){
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.WHITE);
					super.paint(graphics);
				}
			};
			XYEdges labelBorderSizes = new XYEdges(1,1,0,1);
			Border labelBorders = BorderFactory.createSimpleBorder(labelBorderSizes);
			tabLabel.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
			tabLabel.setBorder(labelBorders);
			tabLabel.setFocusListener(this);
			this.listOfLabels[i] = tabLabel;
			this.labelArea.add(tabLabel);
			this.listOfTabs[i].RenderOn(this.tabManagers[i]);
		}
		this.add(this.labelArea);
		this.add(new SeparatorField());
		this.add(this.tabArea);
		
		this.selectTab(this.listOfLabels[0]);
	}
	
	private String prepareTabLabelForDisplay(String initialLabel){
		return " " + initialLabel+" ";
	}
	
	private void deSelectTab(final LabelField tabLabel){
			tabLabel.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
			this.tabArea.delete(this.tabManagers[this.currentTab]);
			this.currentTab = -1;
	}

	private void selectTab(final LabelField tabLabel) {
		int  tabIndex= this.getTabIndex(tabLabel);
		Manager curTabManager = this.tabManagers[tabIndex];
		this.tabArea.add(curTabManager);
		int topColor = Color.ROYALBLUE;
		int bottomColor = Color.BLUE;
		tabLabel.setBackground(BackgroundFactory.createLinearGradientBackground(topColor, topColor, bottomColor, bottomColor));
		this.currentTab = tabIndex;
	}
	
	private void switchToTab(final LabelField tabLabelField){
	    this.deSelectTab(getCurrentTabField());
		this.selectTab(tabLabelField);
	}

	public void focusChanged(Field field, int eventType) {
		if (eventType == FOCUS_GAINED) {
			if (field instanceof LabelField) {
				this.switchToTab((LabelField)field);
			}
		}
	}
	
	private int getTabIndex(final LabelField tabLabel){
		return Arrays.getIndex((Object[])this.listOfLabels,(Object) tabLabel);
	}

	public String getSelectedTab() {
		return getCurrentTabField().getText().trim();
	}

	private LabelField getCurrentTabField() {
		return this.listOfLabels[this.currentTab];
	}
}
