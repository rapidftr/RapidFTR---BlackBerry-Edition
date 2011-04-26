package com.rapidftr.controls;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.rapidftr.utilities.Styles;

public class TitleField extends LabelField {

	private static final int OVERFLOW_TRUNC = 6;
	private int fieldHeight;
	private int fieldWidth;
	private Bitmap bitmap;
	private String userName;

	public TitleField() {
		userName = "";
		bitmap = Bitmap.getBitmapResource("res/logo_header.jpg");
		fieldHeight = bitmap.getHeight();
		fieldWidth = Display.getWidth();
		setBackground(BackgroundFactory.createSolidBackground(Styles.COLOR_FIELD_BACKGROUND));
	}

	public int getPreferredHeight() {
		return super.getPreferredHeight();
	}

	public int getPreferredWidth() {
		return super.getPreferredWidth();
	}

	protected void layout(int width, int height) {
		super.layout(width, height);
		setExtent(fieldWidth, fieldHeight);
	}

	protected void paint(Graphics graphics) {
		graphics.drawBitmap(new XYRect(0, 0, bitmap.getWidth(), bitmap
				.getHeight() - 1), bitmap, 0, 0);
		graphics.setColor(Color.WHITE);
		int textLength = this.getFont().getAdvance(userName);
		int textHeight = this.getFont().getHeight();
		if(textLength >= (this.fieldWidth - bitmap.getWidth())){
			userName = userName.substring(0,OVERFLOW_TRUNC - 1) + "...";
			textLength = this.getFont().getAdvance(userName);
		}
		graphics.drawText(userName,this.fieldWidth - textLength, bitmap.getHeight() - textHeight);
		super.paint(graphics);
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
		this.invalidate();
	}
}
