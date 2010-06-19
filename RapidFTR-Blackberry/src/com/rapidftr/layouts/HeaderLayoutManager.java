package com.rapidftr.layouts;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;

import com.rapidftr.utilities.Styles;

public class HeaderLayoutManager extends Manager {
	private LabelField idField;
	private LabelField headerField;
	
	public HeaderLayoutManager(String headerText, String registrationId) {
		super(0);

		final Font titleFont = Styles.getTitleFont();		
		final Font headerFont = Styles.getHeaderFont();

		idField = new LabelField("Registration Id: " + registrationId);

		idField.setFont(titleFont);

		headerField = new LabelField(headerText) {
			public void paint(Graphics graphics) {
				graphics.setColor(0x00008800);
				super.paint(graphics);
			}
		};

		headerField.setFont(headerFont);

		add(idField);
		add(headerField);
	}

	protected void sublayout(int width, int height) {
		layoutChild(idField, width, 25);
		layoutChild(headerField, width, 25);

		setPositionChild(idField, (width - idField.getWidth()) / 2, 0);
		setPositionChild(headerField, (width - headerField.getWidth()) / 2, 15);

		int actualHeight = idField.getHeight() + headerField.getHeight();

		setExtent(width, actualHeight);
	}
}
