package com.rapidftr.model;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

class TabHandleField extends LabelField {

	public TabHandleField(String labelText) {
		super("", LabelField.FOCUSABLE);
		setText(prepareTabLabelForDisplay(labelText));
		setBorder(getBorders());
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
	}

	public void deSelect() {
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
	}

	public void select() {
		setBackground(BackgroundFactory.createLinearGradientBackground(
				Color.ROYALBLUE, Color.ROYALBLUE, Color.BLUE, Color.BLUE));
	}

	protected void paint(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		super.paint(graphics);
	}

	private String prepareTabLabelForDisplay(String initialLabel) {
		return " " + initialLabel + " ";
	}

	private Border getBorders() {
		XYEdges labelBorderSizes = new XYEdges(1, 1, 0, 1);
		Border labelBorders = BorderFactory
				.createSimpleBorder(labelBorderSizes);
		return labelBorders;
	}

}