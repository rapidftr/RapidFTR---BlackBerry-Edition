package com.rapidftr.model;

import java.util.Vector;

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

public class TabControl extends VerticalFieldManager implements
		FocusChangeListener {

	private final TabHandles tabHandles;
	private final VerticalFieldManager tabArea;
	private final VerticalFieldManager[] tabManagers;
	private int currentTab = -1;

	public TabControl(Tabs tabs) {

		super(VerticalFieldManager.USE_ALL_WIDTH);

		tabHandles = new TabHandles(this);
		tabArea = new TabBody();

		tabManagers = new VerticalFieldManager[tabs.count()];
		for (int i = 0; i < this.tabManagers.length; i++) {
			this.tabManagers[i] = new VerticalFieldManager();
		}

		tabs.forEachTab(new TabAction() {
			int i = 0;
			public void execute(Tab tab) {
				tabHandles.add(new TabHandle(tab.getLabel()));
				tab.render(tabManagers[i++]);
			}
		});

		add(new SeparatorField());
		add(this.tabArea);

		selectTab(tabHandles.getDefaultTab());
	}

	private void deSelectTab(final TabHandle tabLabel) {
		tabLabel.deSelect();
		this.tabArea.delete(this.tabManagers[this.currentTab]);
		this.currentTab = -1;
	}

	private void selectTab(final TabHandle tabLabel) {
		int tabIndex = this.getTabIndex(tabLabel);
		Manager curTabManager = this.tabManagers[tabIndex];
		this.tabArea.add(curTabManager);
		tabLabel.select();
		this.currentTab = tabIndex;
	}

	private void switchToTab(final TabHandle tabLabelField) {
		this.deSelectTab(getCurrentTabField());
		this.selectTab(tabLabelField);
	}

	public void focusChanged(Field field, int eventType) {
		if (eventType == FOCUS_GAINED) {
			if (field instanceof TabHandle) {
				this.switchToTab((TabHandle) field);
			}
		}
	}

	private int getTabIndex(final TabHandle tabHandle) {
		return tabHandles.getIndex(tabHandle);
	}

	public String getSelectedTab() {
		return getCurrentTabField().getText().trim();
	}

	private TabHandle getCurrentTabField() {
		return tabHandles.getTabAt(currentTab);
	}
}

class TabHandles {

	private final TabControl tabControl;
	private HorizontalFieldManager labelArea;
	private Vector tabs = new Vector();

	protected TabHandles(TabControl tabControl, HorizontalFieldManager labelArea) {
		this.tabControl = tabControl;
		this.labelArea = labelArea;
		this.tabControl.add(labelArea);
	}

	public TabHandles(TabControl tabControl) {
		this(tabControl, new HorizontalFieldManager(
				HorizontalFieldManager.HORIZONTAL_SCROLL));
	}
	
	public int getIndex(TabHandle tabHandle) {
		return tabs.indexOf(tabHandle);
	}
	
	public TabHandle getTabAt(int index){
		return (TabHandle) tabs.elementAt(index);
	}

	public TabHandle getDefaultTab() {
		return getTabAt(0);
	}

	public void add(TabHandle tabHandle) {
		tabs.addElement(tabHandle);
		tabHandle.setFocusListener(tabControl);
		labelArea.add(tabHandle);
	}

}

class TabHandle extends LabelField {

	public TabHandle(String labelText) {
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

class TabBody extends VerticalFieldManager {

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
