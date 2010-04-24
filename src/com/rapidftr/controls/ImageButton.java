package com.rapidftr.controls;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;

import com.rapidftr.utilities.Utilities;

public class ImageButton extends ButtonField {
	private Bitmap bitmap;

	public ImageButton(String imageName, int height) {
		super();

		bitmap = Utilities.getScaledBitmap(imageName, height);
	}

	public void paint(Graphics graphics) {
		// Draw the background image and then call paint.
		graphics.drawBitmap(0, 0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);
		
		super.paint(graphics);
	}
	
	public int getPreferredWidth() {
		return bitmap.getWidth();
	}
	
	public int getPreferredHeight() {
		return bitmap.getHeight();
	}
}
