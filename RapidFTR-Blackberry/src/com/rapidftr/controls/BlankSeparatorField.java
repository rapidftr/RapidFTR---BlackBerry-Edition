package com.rapidftr.controls;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class BlankSeparatorField extends Field {

	
	private final int fieldHeight;
	private int fieldWidth;

	public BlankSeparatorField(int height) {
		this.fieldHeight = height;
		this.fieldWidth = Display.getWidth();
	
		
	}
	
	protected void layout(int arg0, int arg1) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	public int getPreferredWidth() {
		return fieldWidth;
	}

	public int getPreferredHeight() {
		return fieldHeight;
	}

	protected void paint(Graphics graphics) {
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,0,fieldWidth -1,fieldHeight -1);

	}

}
