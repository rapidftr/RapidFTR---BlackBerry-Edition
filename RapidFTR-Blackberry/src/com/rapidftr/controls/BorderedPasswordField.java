package com.rapidftr.controls;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.PasswordEditField;

import com.rapidftr.utilities.Styles;

public class BorderedPasswordField extends PasswordEditField {
	private int iRectWidth;
	private int iRectX;
	
	public BorderedPasswordField(String label, String defaultValue) {
		super( label, defaultValue );
		
		Font defaultFont = Styles.getDefaultFont();
		int maxSize = 20;
		
		setFont( defaultFont );
		
		iRectWidth = defaultFont.getAdvance('e') * maxSize;

		iRectX = defaultFont.getAdvance(getLabel());
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
