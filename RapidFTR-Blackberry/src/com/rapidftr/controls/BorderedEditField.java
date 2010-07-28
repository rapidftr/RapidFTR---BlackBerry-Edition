package com.rapidftr.controls;

import com.rapidftr.utilities.Styles;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;

public class BorderedEditField extends BasicEditField {

	private int fieldHeight;
	private int fieldWidth;
	private int padding;

	public BorderedEditField(String text) {
		super("", text);
		padding = 8;
		fieldHeight = Font.getDefault().getHeight() + padding;
		fieldWidth = Display.getWidth() - padding;
	}

	public int getPreferredWidth() {
		return fieldWidth;
	}

	public int getPreferredHeight() {
		return fieldHeight;
	}

	protected void layout(int arg0, int arg1) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	protected void paint(Graphics graphics) {

		graphics.setColor(Styles.COLOR_FIELD_BACKGROUND);
		graphics.drawRoundRect(0, 0, fieldWidth, fieldHeight, 8, 8);

		graphics.setColor(Color.WHITE);
		graphics.drawText(super.getText(), padding, padding / 2 + 1);

		super.paint(graphics);
	}

}