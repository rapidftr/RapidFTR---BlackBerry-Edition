package com.rapidftr.layouts;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;

import com.rapidftr.controls.Button;
import com.rapidftr.utilities.Styles;

public class BorderManager extends Manager {
	public LabelField headerField;
	private Field footerField;
	private Field imageField;
	public Button retakeButton;
	public Button newInfoButton;
	public Button searchAndEditButton;
	private boolean withControls;

	public BorderManager(String headerText, Field imageField,
			Field footerField, boolean withControls) {
		super(0);

		this.withControls = withControls;

		this.footerField = footerField;
		this.imageField = imageField;

		final Font headerFont = Styles.getHeaderFont();
		
		headerField = new LabelField(headerText) {
			public void paint(Graphics graphics) {
				graphics.setColor(0x00008800);
				super.paint(graphics);
			}
		};

		headerField.setFont(headerFont);

		add(headerField);
		add(imageField);
		add(footerField);

		if (withControls) {
			int limit = 90;
			Font defaultFont = Styles.getTitleFont();
	
			retakeButton = new Button("Retake");

			retakeButton.setFont(defaultFont);

			add(retakeButton);
			
			newInfoButton = new Button("Add Details");

			newInfoButton.setFont(defaultFont);

			add(newInfoButton);
			
			searchAndEditButton = new Button("Search");

			searchAndEditButton.setFont(defaultFont);

			add(searchAndEditButton);
		}
	}

	public BorderManager(String headerText, Field imageField, Field footerField) {
		this(headerText, imageField, footerField, false);
	}

	protected void sublayout(int width, int height) {
		layoutChild(headerField, width, 30);
		layoutChild(imageField, width, height - 70);
		layoutChild(footerField, width, 30);

		if (withControls) {
			layoutChild(retakeButton, 50, 30);
			layoutChild(newInfoButton, 50, 30);
			layoutChild(searchAndEditButton, 50, 30);
		}

		int x = (width - headerField.getWidth()) / 2;

		setPositionChild(headerField, x, 0);

		x = (withControls) ? 30 : (width - imageField.getWidth()) / 2;

		setPositionChild(imageField, x, headerField.getHeight() + 10);

		int actualHeight = headerField.getHeight() + getImageHeight()
				+ footerField.getHeight() + 10;

		x = (width - footerField.getWidth()) / 2;

		int y = headerField.getHeight() + getImageHeight() + 10;

		setPositionChild(footerField, x, y);

		if (withControls) {
			x = imageField.getWidth() + 45;
			y = headerField.getHeight() + 15;
			
			setPositionChild(retakeButton, x, y);
			setPositionChild(newInfoButton, x, y + 40);
			setPositionChild(searchAndEditButton, x, y + 80);
		}

		setExtent(width, actualHeight);
	}

	private int getImageHeight() {
		return Display.getHeight() - 60;
	}
}
