package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.Identification;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordEditScreen extends DisplayPage {
	private static final String DEFAULT_IMAGE_NAME = "img/head.png";
	
	private ChildRecord record;
	private String recordId;
	private LabelField fields[];

	public RecordEditScreen() {
		super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
	}

	public void initializePage(Object userInfo) {
		this.record = (ChildRecord) userInfo;

		this.recordId = record.getRecordId();

		add(new HeaderLayoutManager("Review Record", recordId));

		Font defaultFont = Styles.getDefaultFont();

		int limit = 140;

		fields = new LabelField[32];

		fields[0] = new LabelField("IDENTIFICATION DETAILS");

		fields[6] = new LabelField("FAMILY DETAILS");
		fields[7] = new LabelField(
				"Mother's Name: Martha Doe Alive: Yes Reunite: Yes");
		fields[8] = new LabelField(
				"Father's Name: John Doe Alive: No Reunite: -");
		fields[9] = new LabelField("Siblings: Bob, Lisa (2) Reunite: Yes");
		fields[10] = new LabelField("Uncles: - Reunite: -");
		fields[11] = new LabelField("Aunts: - Reunite: -");
		fields[12] = new LabelField("Cousins: - Reunite: -");
		fields[13] = new LabelField("Neighbors: - Reunite: -");
		fields[14] = new LabelField("Others: - Reunite: -");
		fields[15] = new LabelField("Married: No");
		fields[16] = new LabelField("Partner/Spouse Name: -");
		fields[17] = new LabelField("Children: -");
		fields[18] = new LabelField("OTHER DETAILS");
		fields[19] = new LabelField("Caregiver Details:");
		fields[20] = new LabelField("Name: Susan Goode");
		fields[21] = new LabelField("Profession: Aid Worker");
		fields[22] = new LabelField("R'ship to Child: ...");
		fields[23] = new LabelField("Protection Concerns:");
		fields[24] = new LabelField("   UNACCOMPANIED");
		fields[25] = new LabelField("   REFUGEE");
		fields[26] = new LabelField("   IN INTERIM CARE");
		fields[27] = new LabelField("   SICK/INJURED");
		fields[28] = new LabelField("OPTIONS");
		fields[29] = new LabelField("   REUNIFICATION");
		fields[30] = new LabelField("   FOLLOW-UP");

		Button editButton = new Button("Edit Record", limit);

		fields[0].setFont(defaultFont);

		add(fields[0]);

		add(new LayoutManager());

		add(new SeparatorField());

		HorizontalFieldManager buttonManager = new HorizontalFieldManager();

		buttonManager.add(editButton);

		add(buttonManager);

		editButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onEdit();
			}
		});
	}

	private void onEdit() {
		Hashtable userInfo = new Hashtable();

		userInfo.put("record", record);
		userInfo.put("type", String.valueOf(NavigatorScreen.TYPE_EDIT));

		pushScreen(1, userInfo);
	}

	private class LayoutManager extends Manager {
		private LabelField idFields[];
		private BitmapField imageField;

		public LayoutManager() {
			super(0);

			Identification identification = record.getIdentification();

			Bitmap bitmap;
			int imageHeight = 80;

			if (record.getPhoto() != null) {
				EncodedImage encodedImage = Utilities
						.getEncodedImageFromBytes(record.getPhoto());

				bitmap = Utilities.getScaledBitmap(encodedImage, imageHeight);
			} else {
				bitmap = Utilities.getScaledBitmap(DEFAULT_IMAGE_NAME,
						imageHeight);
			}
			
			imageField = new BitmapField(bitmap);

			idFields = new LabelField[6];

			idFields[0] = new LabelField("Name: " + record.getName());

			String sex = identification.isMale() ? "Male" : "Female";

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
					+ identification.getFormattedSeparationDate());

			Font defaultFont = Styles.getDefaultFont();

			add(imageField);

			for (int i = 0; i < idFields.length; i++) {
				idFields[i].setFont(defaultFont);

				add(idFields[i]);
			}
		}

		protected void sublayout(int width, int height) {
			layoutChild(imageField, width / 2, 80);
			setPositionChild(imageField, (width - 85), 5);

			for (int i = 0; i < idFields.length; i++) {
				layoutChild(idFields[i], width, 25);

				setPositionChild(idFields[i], 10, 5 + (i * 15));
			}

			setExtent(width, 130);
		}
	}
}