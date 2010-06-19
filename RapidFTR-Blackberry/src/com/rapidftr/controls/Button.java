package com.rapidftr.controls;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;

public class Button extends ButtonField {

	private int width;
	
	public Button(String label, int width) {
		super(label, FIELD_HCENTER | USE_ALL_WIDTH);
		this.width = width;
	}

	public int getPreferredWidth() {
		return width;
	}
	
    protected void layout(int width, int height) {
    	int h = 20;
        super.layout(this.width, h);
        setExtent(this.width, h);
    }

    protected void paint(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        String label = getLabel();
        int x = (getPreferredWidth() - getFont().getAdvance(label)) >> 1;
        int y = (getPreferredHeight() - getFont().getHeight()) >> 1;
        graphics.drawText(label, x, y);
    }

}
