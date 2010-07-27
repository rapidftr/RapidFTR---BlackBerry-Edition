package com.rapidftr.controls;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;

import com.rapidftr.utilities.Styles;

public class BorderedEditField extends BasicEditField {
	private int iRectWidth;
	private int iRectX;
	
	public BorderedEditField(String label, String defaultValue, Font defaultFont, int maxSize) {
		super(label, defaultValue, maxSize, 0);

		iRectWidth = defaultFont.getAdvance('e') * maxSize;

		iRectX = defaultFont.getAdvance(getLabel());	
		
		this.setFont(defaultFont);
	}
	
	public BorderedEditField(String label, String defaultValue, Font defaultFont) {
		this(label, defaultValue, defaultFont, 20);
	}

	public BorderedEditField(String label, String defaultValue) {
		this(label, defaultValue, Styles.getDefaultFont(), 20);
	}
	
	public BorderedEditField(String label, String defaultValue, int maxSize) {
		this(label, defaultValue, Styles.getDefaultFont(), maxSize);
	}
	
	public int getPreferredHeight() {
		return 20;
	}

	public void layout(int width, int height) {
		super.layout(width, getPreferredHeight());
		setExtent(width, getPreferredHeight());

	}

	public void paint(Graphics g) {
		g.setColor(0x000000);
		g.drawRect(iRectX, 0, iRectWidth, 16);
		super.paint(g);
	}
}
