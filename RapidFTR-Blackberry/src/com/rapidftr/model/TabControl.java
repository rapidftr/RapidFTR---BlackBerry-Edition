package com.rapidftr.model;

import com.rapidftr.layouts.TabAction;
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

	private final TabHandle[] listOfLabels;
	private final HorizontalFieldManager labelArea;
	private final VerticalFieldManager tabArea;
	private final VerticalFieldManager[] tabManagers;
	private int currentTab = -1;

	public TabControl(Tabs tabs) {

		super(VerticalFieldManager.USE_ALL_WIDTH);

		this.listOfLabels = new TabHandle[tabs.count()];
		this.labelArea = new HorizontalFieldManager(HORIZONTAL_SCROLL);
		this.tabArea = new TabBody();

		this.tabManagers = new VerticalFieldManager[tabs.count()];
		for (int i = 0; i < this.tabManagers.length; i++) {
			this.tabManagers[i] = new VerticalFieldManager();
		}

		final TabControl control = this;
		tabs.forEachTab(new TabAction() {
			int i = 0;
			public void execute(Tab tab) {
				TabHandle tabLabel = new TabHandle(tab.getLabel());
				tabLabel.setFocusListener(control);
				listOfLabels[i] = tabLabel;
				labelArea.add(tabLabel);
				tab.render(tabManagers[i]);
				i++;
			}

		});
		
		this.add(this.labelArea);
		this.add(new SeparatorField());
		this.add(this.tabArea);

		this.selectTab(this.listOfLabels[0]);
	}
	
	private void deSelectTab(final TabHandle tabLabel){
			tabLabel.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
			this.tabArea.delete(this.tabManagers[this.currentTab]);
			this.currentTab = -1;
	}

	private void selectTab(final TabHandle tabLabel) {
		int  tabIndex= this.getTabIndex(tabLabel);
		Manager curTabManager = this.tabManagers[tabIndex];
		this.tabArea.add(curTabManager);
		int topColor = Color.ROYALBLUE;
		int bottomColor = Color.BLUE;
		tabLabel.setBackground(BackgroundFactory.createLinearGradientBackground(topColor, topColor, bottomColor, bottomColor));
		this.currentTab = tabIndex;
	}
	
	private void switchToTab(final TabHandle tabLabelField){
	    this.deSelectTab(getCurrentTabField());
		this.selectTab(tabLabelField);
	}

	public void focusChanged(Field field, int eventType) {
		if (eventType == FOCUS_GAINED) {
			if (field instanceof TabHandle) {
				this.switchToTab((TabHandle)field);
			}
		}
	}
	
	private int getTabIndex(final TabHandle tabLabel){
		return Arrays.getIndex((Object[])this.listOfLabels,(Object) tabLabel);
	}

	public String getSelectedTab() {
		return getCurrentTabField().getText().trim();
	}

	private TabHandle getCurrentTabField() {
		return this.listOfLabels[this.currentTab];
	}
}

final class TabHandle extends LabelField {

	public TabHandle(String labelText) {
		super("", LabelField.FOCUSABLE);
		setText(prepareTabLabelForDisplay(labelText));
		setBorder(getBorders());
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
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

final class TabBody extends VerticalFieldManager {

	protected void paint(Graphics graphics) {
		setBackgroundColor(graphics);
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
