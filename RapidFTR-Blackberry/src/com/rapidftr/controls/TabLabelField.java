package com.rapidftr.controls;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

class TabLabelField extends LabelField implements FocusChangeListener{

	private Vector observers = new Vector();

	public TabLabelField(String labelText) {
		super("", LabelField.FOCUSABLE);
		setText(prepareTabLabelForDisplay(labelText));
		setBorder(getBorders());
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
		this.setFocusListener(this);
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
	
	public void addFocusChangeListener(FocusChangeListener observer) {
		observers.addElement(observer);
	}
	
	private void notifyObservers(int eventType) {
		for (int i = 0; i < observers.size(); i++) {
			((FocusChangeListener) observers.elementAt(i)).focusChanged(this,eventType);
		}
	}

	public void focusChanged(Field field, int eventType) {
		notifyObservers(eventType);
	}

}