package com.rapidftr.model;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;

class TabBodyField extends VerticalFieldManager {
	
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