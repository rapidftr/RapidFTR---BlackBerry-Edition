package com.rapidftr.model;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

class TabBody extends VerticalFieldManager {
	
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