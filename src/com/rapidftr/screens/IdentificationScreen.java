package com.rapidftr.screens;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NumericChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.Identification;
import com.rapidftr.utilities.Styles;

public class IdentificationScreen extends MainScreen implements Page {
	private static final String[] separationDates = { "1 - 2 Weeks",
		"2 - 4 Weeks", "1 - 6 Months", "6 Months - 1 Year", "> 1 Year" };

	private Controller controller;

	public void setUserInfo(Object userInfo) {
		String id = (String)userInfo;
		
		add (new HeaderLayoutManager("1. Identification", id) );
		add( new SeparatorField() );

		add( new LayoutManager() );
	}
	
	public void addController(Controller controller) {
		this.controller = controller;
	}
	
	private MenuItem _cancel = new MenuItem("Cancel", 110, 10) {
		public void run() {
			onClose();
		}
	};
	
	private MenuItem _save = new MenuItem("Save", 110, 10) {
		public void run() {
			onSave();
		}
	};
	
	protected void makeMenu(Menu menu, int instance) {
		menu.add(_cancel);
		menu.add(_save);
	}
	
	public boolean onSave() {
		Identification data = new Identification();
		
		data.setAge(8);
		data.setDateOfSeparation(Identification.SEP_1_6_MTHS);
		data.setExactAge(true);
		data.setLastKnownLocation("Far City");
		data.setOrigin("Home Town");
		data.setSex(false);
		
		controller.handleSave(data);
		
		this.getUiEngine().popScreen(this);
		
		return true;		
	}
	
	public boolean onClose() {
		this.getUiEngine().popScreen(this);
		
		return true;
	}
	
	/**
	 * Layout Manager
	 */
	
	private class LayoutManager extends Manager {
		private Font defaultFont;

		private BorderedEditField nameField;
		private LabelField sexField;
		private RadioButtonField genderMale;
		private RadioButtonField genderFemale; 
		private NumericChoiceField ageField;
		private RadioButtonField ageExact;
		private RadioButtonField ageApprox; 
		private BorderedEditField originField;
		private BorderedEditField lastKnownLocField;
		private LabelField sepDateField;
		

		private RadioButtonField separationDateFields[] = new RadioButtonField[separationDates.length];

		public LayoutManager() {
			super(0);

			defaultFont = Styles.getDefaultFont();
			
			final Font secondaryFont = Styles.getSecondaryFont();

			nameField = new BorderedEditField("Name: ", "",
					defaultFont);

			nameField.setFont(defaultFont);

			sexField = new LabelField("Sex: ");

			sexField.setFont(defaultFont);

			final RadioButtonGroup gendersGroup = new RadioButtonGroup();

			genderMale = new RadioButtonField(" Male",
					gendersGroup, false);

			genderMale.setFont(secondaryFont);

			genderFemale = new RadioButtonField(" Female",
					gendersGroup, false);

			genderFemale.setFont(secondaryFont);

			ageField = new NumericChoiceField("Age: ", 0, 20, 1);

			ageField.setFont(defaultFont);
			
			final RadioButtonGroup ageAccuracyGroup = new RadioButtonGroup();

			ageExact = new RadioButtonField(" Exact",
					ageAccuracyGroup, false);

			ageApprox = new RadioButtonField(" Approx.",
					ageAccuracyGroup, true);

			ageExact.setFont(secondaryFont);
			ageApprox.setFont(secondaryFont);

			originField = new BorderedEditField("Origin: ", "",
					defaultFont);

			originField.setFont(defaultFont);

			lastKnownLocField = new BorderedEditField(
					"Last Known Loc.: ", "", defaultFont, 15);

			lastKnownLocField.setFont(defaultFont);

			sepDateField = new LabelField("Date of Separation: ");

			sepDateField.setFont(defaultFont);

			final RadioButtonGroup separationDateGroup = new RadioButtonGroup();

			for (int i = 0; i < separationDates.length; i++) {
				separationDateFields[i] = new RadioButtonField(separationDates[i],
						separationDateGroup, false);

				separationDateFields[i].setFont(secondaryFont);
			}
			
			
			add(nameField);
			add(sexField);
			
			add(genderMale);
			add(genderFemale);
			
			add(ageField);
			add(ageExact);
			add(ageApprox);
			
			add(originField);
			add(lastKnownLocField);
			
			add(sepDateField);
			
			for ( int i=0; i<separationDates.length; i++ ) {
				add(separationDateFields[i]);
			}
		}

		protected void sublayout(int width, int height) {
			layoutChild(nameField, width, 30);
			layoutChild(sexField, width, 30);
			layoutChild(genderMale, width/4, 30);
			layoutChild(genderFemale, width/4, 30);
			layoutChild(ageField, 50, 30);
			layoutChild(ageExact, width/4, 30);
			layoutChild(ageApprox, width/4, 30);
			layoutChild(originField, width, 30);
			layoutChild(lastKnownLocField, width, 30);
			layoutChild(sepDateField, width, 30);

			for ( int i=0; i<separationDateFields.length; i++ ) {
				layoutChild(separationDateFields[i], width/2, 30);
			}
			
			setPositionChild(nameField, 10, 0);
			setPositionChild(sexField, 10, 20);
			setPositionChild(genderMale, 50, 20);
			setPositionChild(genderFemale, 110, 20);
			setPositionChild(ageField, 10, 40);
			setPositionChild(ageExact, 50, 55);
			setPositionChild(ageApprox, 110, 55);
			setPositionChild(originField, 10, 70);
			setPositionChild(lastKnownLocField, 10, 90);
			setPositionChild(sepDateField, 10, 110);

			for ( int i=0; i<separationDateFields.length; i++ ) {
				setPositionChild(separationDateFields[i], 50, 120 + (15 * i));
			}
			
			int actualHeight = 220;

			setExtent(width, actualHeight);
		}	
	}
}
