package com.rapidftr.screens;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.utilities.Styles;

public class AddFamilyScreen extends DisplayPage {
	public static final int CLOSE_ACTION = 1;

	private static final String OPTIONAL_RELATIVES[] = {
		"Siblings",
		"Uncles",
		"Aunts",
		"Cousins",
		"Neighbors",
		"Others"
	};
	
	private LayoutManager layoutManager;

	public void initializePage(Object userInfo) {
		String id = (String) userInfo;

		add(new HeaderLayoutManager("2. Family Details", id));
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
		Dialog.alert("saved family data");

		return true;
	}

	public boolean onClose() {
		popScreen(CLOSE_ACTION, null);

		return true;
	}

	private class LayoutManager extends Manager {
		LabelField header;

		SingleRelativeLayout motherFields;
		SingleRelativeLayout fatherFields;
		
		MultiRelativeLayout optionalRelatives[];

		public LayoutManager() {
			super(0);

			motherFields = new SingleRelativeLayout("Mother");
			
			fatherFields = new SingleRelativeLayout("Father");
			
			optionalRelatives = new MultiRelativeLayout[OPTIONAL_RELATIVES.length];
			
			for ( int i=0; i<OPTIONAL_RELATIVES.length; i++ ) {
				optionalRelatives[i] = new MultiRelativeLayout(OPTIONAL_RELATIVES[i]);
			}
			
			add(motherFields);
			add(fatherFields);
			
			for ( int i=0; i<OPTIONAL_RELATIVES.length; i++ ) {
				add(optionalRelatives[i]); 
			}
		}

		protected void sublayout(int width, int height) {
			int y = 0;
			
			layoutChild(motherFields, width, 30);
			setPositionChild(motherFields, 0, y);
		
			y += motherFields.getExtent().height;
			
			layoutChild(fatherFields, width, 30);
			setPositionChild(fatherFields, 0, y);
			
			y += fatherFields.getExtent().height;
			
			for ( int i=0; i<OPTIONAL_RELATIVES.length; i++ ) {
				layoutChild(optionalRelatives[i], width, 30);
				
				setPositionChild(optionalRelatives[i], 0, y);
				
				y += optionalRelatives[i].getExtent().height;
			}
		
			y += 10;
			
			setExtent(width, y);
		}
	}
	
	private class SingleRelativeLayout extends Manager {
		private Font defaultFont;
		LabelField header;
		RelativeLayout fields;
		
		public SingleRelativeLayout(String headerText) {
			super(0);
			
			defaultFont = Styles.getDefaultFont();
			
			header = new LabelField(headerText);
			header.setFont(defaultFont);
			
			fields = new RelativeLayout();
			
			add(header);
			add(fields);
		}
		
		protected void sublayout(int width, int height) {
			int y = 0;
			
			layoutChild(header, width, 30);
			setPositionChild(header, 10, y);

			y += 10;
			
			layoutChild(fields, width, 30);
			setPositionChild(fields, 0, y);	
			
			y += fields.getExtent().height;
			
			setExtent(width, y);
		}
	}

	
	private class MultiRelativeLayout extends Manager {
		private Font defaultFont;
		LabelField header;
		Button button;
		
		public MultiRelativeLayout(String headerText) {
			super(0);
			
			defaultFont = Styles.getDefaultFont();
			
			header = new LabelField(headerText);
			header.setFont(defaultFont);
			
			button = new Button("Add", 40);
			button.setFont(defaultFont);
			
			add(header);
			add(button);
		}
		
		protected void sublayout(int width, int height) {
			int y = 0;
			
			layoutChild(header, width, 30);
			setPositionChild(header, 10, y);

			y += 15;
		
			layoutChild(button, width, 30);
			setPositionChild(button, 40, y);
			
			y += 35;
			
			setExtent(width, y);
		}
	}
	
	private class RelativeLayout extends Manager {
		BorderedEditField nameField;
		CheckboxField isAlive;
		CheckboxField reunite;
		private Font defaultFont;
		
		public RelativeLayout() {
			super(0);
			
			defaultFont = Styles.getDefaultFont();
			
			nameField = new BorderedEditField("", "", defaultFont, 15);
			isAlive = new CheckboxField("Alive", false);
			isAlive.setFont(Styles.getSecondaryFont());
			reunite = new CheckboxField("Reunite", false);
			reunite.setFont(Styles.getSecondaryFont());
			
			add(nameField);
			add(isAlive);
			add(reunite);
		}

		protected void sublayout(int width, int height) {
			layoutChild(nameField, width, 30);
			setPositionChild(nameField, 20, 5);
			layoutChild(isAlive, width, 30);
			setPositionChild(isAlive, 190, 5);
			layoutChild(reunite, width, 30);
			setPositionChild(reunite, 240, 5);
			
			setExtent(width, 30);
		}
	}
}
