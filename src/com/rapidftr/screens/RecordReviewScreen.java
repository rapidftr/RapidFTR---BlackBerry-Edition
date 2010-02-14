package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.ScreenManager;
import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.Identification;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordReviewScreen extends MainScreen implements Page {
	private ScreenManager screenManager;
	private ChildRecord record;
	private String recordId;
	
	public RecordReviewScreen() {
		super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
	}
	
	public void setUserInfo(Object userInfo) {
		this.record = (ChildRecord)userInfo;
		this.recordId = record.getRecordId();
		
		add(new HeaderLayoutManager("Review Record", recordId));

		Font defaultFont = Styles.getDefaultFont();
		
		int limit = 140;

		LabelField fields[] = new LabelField[32]; 
		
		fields[0] = new LabelField("IDENTIFICATION DETAILS");

		fields[6] = new LabelField("Date of Separation: 2 - 4 weeks ago");
		fields[7] = new LabelField("FAMILY DETAILS");
		fields[8] = new LabelField("Mother's Name: Martha Doe Alive: Yes Reunite: Yes");
		fields[9] = new LabelField("Father's Name: John Doe Alive: No Reunite: -");
		fields[10] = new LabelField("Siblings: Bob, Lisa (2) Reunite: Yes");
		fields[11] = new LabelField("Uncles: - Reunite: -");
		fields[12] = new LabelField("Aunts: - Reunite: -");
		fields[13] = new LabelField("Cousins: - Reunite: -");
		fields[14] = new LabelField("Neighbors: - Reunite: -");
		fields[15] = new LabelField("Others: - Reunite: -");
		fields[16] = new LabelField("Married: No");
		fields[17] = new LabelField("Partner/Spouse Name: -");
		fields[18] = new LabelField("Children: -");
		fields[19] = new LabelField("OTHER DETAILS");
		fields[20] = new LabelField("Caregiver Details:");
		fields[21] = new LabelField("Name: Susan Goode");
		fields[22] = new LabelField("Profession: Aid Worker");
		fields[23] = new LabelField("R'ship to Child: ...");
		fields[24] = new LabelField("Protection Concerns:");
		fields[25] = new LabelField("   UNACCOMPANIED");
		fields[26] = new LabelField("   REFUGEE");
		fields[27] = new LabelField("   IN INTERIM CARE");
		fields[28] = new LabelField("   SICK/INJURED");
		fields[29] = new LabelField("OPTIONS");
		fields[30] = new LabelField("   REUNIFICATION");
		fields[31] = new LabelField("   FOLLOW-UP");
	
		
		Button editButton = new Button("Edit Record", limit);
		Button submitButton = new Button("Submit Record", limit);

		add( new LayoutManager() );
		
		for ( int i=6; i<13; i++ ) {
			fields[i].setFont(defaultFont);
			
			add( fields[i]);
			
			if ( ( i == 6) || (i == 8) || (i == 10) ) {
				add( new SeparatorField());
			}
		}
		
		HorizontalFieldManager buttonManager = new HorizontalFieldManager();
		
		buttonManager.add(editButton);
		buttonManager.add(submitButton);

		add(buttonManager);
		
		editButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onEdit();
			}
		});
		
		submitButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSubmit();
			}
		});
	}
	
	public void addScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}
	
	private void onEdit() {
		this.getUiEngine().popScreen(this);
	}
	
	private void onSubmit() {
		this.getUiEngine().popScreen(this);
		screenManager.closeScreen(ScreenManager.STATUS_SAVE, recordId);
	}
	
	private class LayoutManager extends Manager {
		private LabelField idFields[];
		private BitmapField imageField;

		public LayoutManager() {
			super(0);

			Identification identification = record.getIdentification();

			imageField = new BitmapField(Utilities.getScaledBitmapFromBytes(
					record.getPhoto(), 80));

			idFields = new LabelField[6];

			idFields[0] = new LabelField("Name: " + record.getName());

			String sex = identification.isSex() ? "Male" : "Female";

			idFields[1] = new LabelField("Sex: " + sex);

			String approxOrExact = identification.isExactAge() ? ""
					: " (approx.)";

			idFields[2] = new LabelField("Age: " + identification.getAge()
					+ approxOrExact);
			idFields[3] = new LabelField("Origin: "
					+ identification.getOrigin());
			idFields[4] = new LabelField("Last Known Location: "
					+ identification.getLastKnownLocation());

			idFields[5] = new LabelField("Date of Separation: "
					+ Identification.getFormattedSeparationDate(identification
							.getDateOfSeparation()));

			Font defaultFont = Styles.getDefaultFont();

			add(imageField);

			for (int i = 0; i < idFields.length; i++) {
				idFields[i].setFont(defaultFont);

				add(idFields[i]);
			}
		}

		protected void sublayout(int width, int height) {
			layoutChild(imageField, width/2, 80);
			setPositionChild(imageField, (width - 85), 5);
			
			for (int i = 0; i < idFields.length; i++) {
				layoutChild(idFields[i], width, 25);

				setPositionChild(idFields[i], 10, 5 + (i * 15));
			}

			setExtent(width, 130);
		}
	}
}
