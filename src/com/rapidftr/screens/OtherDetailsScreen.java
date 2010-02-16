package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.Caregiver;
import com.rapidftr.model.ProtectionConcerns;
import com.rapidftr.utilities.Styles;

public class OtherDetailsScreen extends DisplayPage {
	public static final int CLOSE_ACTION = 1;

	private LayoutManager layoutManager;

	public void initializePage(Object userInfo) {
		String id = (String) userInfo;

		add(new HeaderLayoutManager("3. Other Details", id));
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
		ProtectionConcerns protectionConcerns = new ProtectionConcerns();

		for (int i = 0; i < ProtectionConcerns.NAMES.length; i++) {
			CheckboxField field = layoutManager.items[i];

			protectionConcerns.setConcern(i, field.getChecked());
		}

		Caregiver caregiverDetails = new Caregiver();
		
		caregiverDetails.setName(layoutManager.caregiverName.getText());
		caregiverDetails.setProfession(layoutManager.caregiverProfession.getText());
		caregiverDetails.setRelationshipToChild(layoutManager.caregiverRship.getText());

		Hashtable userInfo = new Hashtable();
		
		userInfo.put("protectionConcerns", protectionConcerns);
		userInfo.put("caregiverDetails", caregiverDetails);
		
		popScreen(CLOSE_ACTION, userInfo);
		
		return true;
	}

	public boolean onClose() {
		popScreen(CLOSE_ACTION, null);

		return true;
	}

	private class LayoutManager extends Manager {
		private Font defaultFont;

		LabelField header1;
		LabelField header2;
		BorderedEditField caregiverName;
		BorderedEditField caregiverProfession;
		BorderedEditField caregiverRship;
		CheckboxField items[];

		public LayoutManager() {
			super(0);

			defaultFont = Styles.getDefaultFont();

			header1 = new LabelField("Caregiver Details");
			
			caregiverName = new BorderedEditField("Name: ", "", defaultFont);
			
			caregiverProfession = new BorderedEditField("Profession: ", "", defaultFont);
			
			caregiverRship = new BorderedEditField("Relationship to Child: ", "", defaultFont, 14);
	
			add(caregiverName);
			add(caregiverProfession);
			add(caregiverRship);
			
			header2 = new LabelField("Protection Concerns");

			header1.setFont(Styles.getHeaderFont());

			add(header1);

			header2.setFont(Styles.getHeaderFont());

			add(header2);
			
			items = new CheckboxField[ProtectionConcerns.NAMES.length];

			for (int i = 0; i < ProtectionConcerns.NAMES.length; i++) {
				items[i] = new CheckboxField(ProtectionConcerns.NAMES[i], false);
				items[i].setFont(defaultFont);
				add(items[i]);
			}
		}

		protected void sublayout(int width, int height) {
			layoutChild(header1, width, 30);
			
			layoutChild(caregiverName, width, 30);
			layoutChild(caregiverProfession, width, 30);
			layoutChild(caregiverRship, width, 30);
			
			layoutChild(header2, width, 30);

			setPositionChild(header1, 10, 0);

			setPositionChild(caregiverName, 10, 25);
			setPositionChild(caregiverProfession, 10, 45);
			setPositionChild(caregiverRship, 10, 65);
						
			setPositionChild(header2, 10, 85);
			
			for (int i = 0; i < items.length; i++) {
				layoutChild(items[i], width, 30);
				setPositionChild(items[i], 10, 110 + (20 * i));
			}

			setExtent(width, 75 + (items.length * 20));
		}
	}
}
