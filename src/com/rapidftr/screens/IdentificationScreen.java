package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NumericChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.Identification;
import com.rapidftr.utilities.Styles;

public class IdentificationScreen extends DisplayPage {
	public static final int CLOSE_ACTION = 1;
	
     	
	private LayoutManager layoutManager;

	public void initializePage(Object userInfo) {
		String id = (String) userInfo;

		add(new HeaderLayoutManager("1. Identification", id));
		add(new SeparatorField());

		layoutManager = new LayoutManager();

		add(layoutManager);
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

		String name = layoutManager.nameField.getText();
		
		if ( (name == null) || (name.length() == 0) ) {
			Dialog.alert("Child name cannot be blank");
			return false;
		}
		
		data.setName(name);

		data.setAge(layoutManager.ageField.getSelectedValue());
		data.setDateOfSeparation(layoutManager.separationDateGroup.getSelectedIndex());
		data.setExactAge(layoutManager.ageAccuracyGroup.getSelectedIndex() == 0);
		data.setLastKnownLocation(layoutManager.lastKnownLocField.getText());
		data.setOrigin(layoutManager.originField.getText());
		
		data.setMale(layoutManager.gendersGroup.getSelectedIndex() == 0);

		popScreen(CLOSE_ACTION, data);
		return true;
	}

	public boolean onClose() {
		popScreen(CLOSE_ACTION, null);

		return true;
	}

	/**
	 * Layout Manager
	 */

	private class LayoutManager extends Manager {
		private Font defaultFont;

		public BorderedEditField nameField;
		public NumericChoiceField ageField;
		public BorderedEditField originField;
		public BorderedEditField lastKnownLocField;
		public final RadioButtonGroup gendersGroup;
		public final RadioButtonGroup ageAccuracyGroup;
		public final RadioButtonGroup separationDateGroup;

		private LabelField sexField;
		private RadioButtonField genderMale;
		private RadioButtonField genderFemale;

		private RadioButtonField ageExact;
		private RadioButtonField ageApprox;

		private LabelField sepDateField;

		private RadioButtonField separationDateFields[] = new RadioButtonField[Identification.separationDates.length];

		private Button cancelButton;
		private Button saveButton;
		
		public LayoutManager() {
			super(0);

			defaultFont = Styles.getDefaultFont();

			final Font secondaryFont = Styles.getSecondaryFont();

			nameField = new BorderedEditField("Name: ", "", defaultFont);

			sexField = new LabelField("Sex: ");

			sexField.setFont(defaultFont);

			gendersGroup = new RadioButtonGroup();

			genderMale = new RadioButtonField(" Male", gendersGroup, true);

			genderMale.setFont(secondaryFont);

			genderFemale = new RadioButtonField(" Female", gendersGroup, false);

			genderFemale.setFont(secondaryFont);

			ageField = new NumericChoiceField("Age: ", 0, 20, 1);

			ageField.setFont(defaultFont);

			ageAccuracyGroup = new RadioButtonGroup();

			ageExact = new RadioButtonField(" Exact", ageAccuracyGroup, false);

			ageApprox = new RadioButtonField(" Approx.", ageAccuracyGroup, true);

			ageExact.setFont(secondaryFont);
			ageApprox.setFont(secondaryFont);

			originField = new BorderedEditField("Origin: ", "", defaultFont);

			originField.setFont(defaultFont);

			lastKnownLocField = new BorderedEditField("Last Known Loc.: ", "",
					defaultFont, 15);

			lastKnownLocField.setFont(defaultFont);

			sepDateField = new LabelField("Date of Separation: ");

			sepDateField.setFont(defaultFont);

			separationDateGroup = new RadioButtonGroup();

			for (int i = 0; i < Identification.separationDates.length; i++) {
				separationDateFields[i] = new RadioButtonField(
						Identification.separationDates[i], separationDateGroup, false);

				separationDateFields[i].setFont(secondaryFont);
			}

			cancelButton = new Button("Cancel", 60);
			cancelButton.setFont(defaultFont);
			
			cancelButton.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					onClose();
				}
			});
			
			saveButton = new Button("Save", 60);
			saveButton.setFont(defaultFont);
			
			saveButton.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					onSave();
				}
			});
			
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

			for (int i = 0; i < Identification.separationDates.length; i++) {
				add(separationDateFields[i]);
			}
			
			add(cancelButton);
			add(saveButton);
		}

		protected void sublayout(int width, int height) {
			layoutChild(nameField, width, 30);
			layoutChild(sexField, width, 30);
			layoutChild(genderMale, width / 4, 30);
			layoutChild(genderFemale, width / 4, 30);
			layoutChild(ageField, 50, 30);
			layoutChild(ageExact, width / 4, 30);
			layoutChild(ageApprox, width / 4, 30);
			layoutChild(originField, width, 30);
			layoutChild(lastKnownLocField, width, 30);
			layoutChild(sepDateField, width, 30);
			layoutChild(cancelButton, width/2, 30);
			layoutChild(saveButton, width/2, 30);

			for (int i = 0; i < separationDateFields.length; i++) {
				layoutChild(separationDateFields[i], width / 2, 30);
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

			for (int i = 0; i < separationDateFields.length; i++) {
				setPositionChild(separationDateFields[i], 50, 120 + (15 * i));
			}

			setPositionChild(cancelButton, 10, 200);
			setPositionChild(saveButton, 100, 200);
			
			int actualHeight = 240;

			setExtent(width, actualHeight);
		}
	}
}
