package com.rapidftr.controls;

import com.rapidftr.utilities.Styles;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class TitleField extends Field {

	private int fieldHeight;
	private int fieldWidth;
	private Bitmap bitmap;

	public TitleField() {

		bitmap = Bitmap.getBitmapResource("res/logo_header.jpg");

		Font defaultFont = Font.getDefault();
		fieldHeight = bitmap.getHeight();
		fieldWidth = Display.getWidth();
		setBackground(BackgroundFactory
				.createSolidBackground(Styles.COLOR_FIELD_BACKGROUND));
	}

	public int getPreferredHeight() {
		return super.getPreferredHeight();
	}

	public int getPreferredWidth() {
		return super.getPreferredWidth();
	}

	protected void layout(int width, int height) {

		setExtent(fieldWidth, fieldHeight);
	}

	protected void paint(Graphics graphics) {

	//	paintBackground(graphics);
		graphics.drawBitmap(new XYRect(0,0,bitmap.getWidth(),bitmap.getHeight()-1),bitmap,0,0);
	}

	

}
