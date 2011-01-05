package com.rapidftr.controls;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

import com.rapidftr.utilities.Styles;

public class Button extends Field {
	private int fieldWidth;
	private int fieldHeight;
	private String text;
	private int padding = 8;

	private int backgroundColour;
	private Bitmap bitmap;
                 
	public Button(String text) {
		super(Field.FOCUSABLE);
		backgroundColour = Styles.COLOR_FIELD_BACKGROUND;
		this.text = text;
		Font defaultFont = Font.getDefault();
		fieldHeight = defaultFont.getHeight() + padding;
		fieldWidth = defaultFont.getAdvance(text) + (padding * 2);
		this.setPadding(2, 2, 2, 2);
	}

	public Button(Bitmap bitmap) {
		super(Field.FOCUSABLE);
		this.bitmap = bitmap;
		fieldHeight = bitmap.getHeight() + 8;
		fieldWidth = bitmap.getWidth() + 8;
	}

	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(1);
		return true;
	}

	protected void onFocus(int direction) {
		backgroundColour = Styles.COLOR_FIELD_HIGHLIGHT;
		invalidate();
	}

	protected void onUnfocus() {
		backgroundColour = Styles.COLOR_FIELD_BACKGROUND;
		invalidate();
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

	protected void drawFocus(Graphics graphics, boolean on) {

	}

	protected void fieldChangeNotify(int context) {
		try {
			this.getChangeListener().fieldChanged(this, context);
		} catch (Exception e) {
			System.out.println(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}

	protected void paint(Graphics graphics) {

		if (bitmap != null) {
			graphics.setColor(backgroundColour);
			graphics.fillRect(0, 0, fieldWidth, fieldHeight);
			graphics.drawBitmap(new  XYRect(4, 4, bitmap.getWidth()-1, bitmap
					.getHeight()-1), bitmap, 0, 0);
			return;
		}

		graphics.setColor(backgroundColour);
		graphics.fillRoundRect(0, 0, fieldWidth, fieldHeight, 8, 8);
		graphics.setColor(Styles.COLOR_FIELD_BACKGROUND);
		graphics.drawRoundRect(0, 0, fieldWidth, fieldHeight, 8, 8);

		graphics.setColor(Color.WHITE);
		graphics.drawText(text, (fieldWidth / 2)
				- (getFont().getAdvance(text) / 2), padding / 2 + 1);

	}

	public static void setOptimimWidthForButtonGroup(Vector buttonGroup) {

		int maxWidth = 0;

		for (Enumeration buttons = buttonGroup.elements(); buttons
				.hasMoreElements();) {
			Button button = (Button) buttons.nextElement();
			int width = button.fieldWidth;
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		for (Enumeration buttons = buttonGroup.elements(); buttons
				.hasMoreElements();) {
			Button button = (Button) buttons.nextElement();
			button.fieldWidth = maxWidth;
		}

	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		fieldHeight = bitmap.getHeight() + 8;
		fieldWidth = bitmap.getWidth() + 8;
	}

}
