package com.rapidftr.screens;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.util.Arrays;

import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.Caregiver;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.Identification;
import com.rapidftr.model.ProtectionConcerns;
import com.rapidftr.model.Relative;
import com.rapidftr.model.ProtectionConcerns.ProtectionConcern;
import com.rapidftr.services.ServiceException;
import com.rapidftr.services.ServiceManager;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordReviewScreen extends DisplayPage {
	// private ScreenManager screenManager;
	private ChildRecord record;
	private String recordId;

	public RecordReviewScreen() {
		super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
	}

	public void initializePage(Object userInfo) {
		this.record = (ChildRecord) userInfo;
		this.recordId = record.getRecordId();

		add(new HeaderLayoutManager("Review Record", recordId));

		add(new IdentificationLayoutManager());

		add(new FamilyLayoutManager());

		add(new OtherDetailsLayoutManager());

		add(new OptionsLayoutManager());

		Font defaultFont = Styles.getDefaultFont();

		int limit = 60;

		Button editButton = new Button("Edit", limit);
		editButton.setFont(defaultFont);
		Button submitButton = new Button("Submit", limit);
		submitButton.setFont(defaultFont);

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

	private void onEdit() {
		popScreen(3, null);
	}

	private void onSubmit() {
		try {
			ServiceManager.getRecordService().save(record);
		} catch (ServiceException se) {
			Dialog.alert("Failed to save record " + recordId + ": " + se);
		}

		popScreen(2, recordId);
	}

	private class IdentificationLayoutManager extends Manager {
		private RichTextField items[];
		private BitmapField imageField;
		private LabelField header;

		public IdentificationLayoutManager() {
			super(0);

			Identification identification = record.getIdentification();

			header = new LabelField("IDENTIFICATION DETAILS");

			imageField = new BitmapField(Utilities.getScaledBitmapFromBytes(
					record.getPhoto(), 80));

			items = new RichTextField[6];

			items[0] = new RichTextField("Name: " + record.getName(),
					Field.READONLY);

			String sex = identification.isMale() ? "Male" : "Female";

			items[1] = new RichTextField("Sex: " + sex, Field.READONLY);

			String approxOrExact = identification.isExactAge() ? ""
					: " (approx.)";

			items[2] = new RichTextField("Age: " + identification.getAge()
					+ approxOrExact, Field.READONLY);
			items[3] = new RichTextField("Origin: "
					+ identification.getOrigin(), Field.READONLY);
			items[4] = new RichTextField("Last Known Location: "
					+ identification.getLastKnownLocation(), Field.READONLY);

			items[5] = new RichTextField("Date of Separation: "
					+ Identification.getFormattedSeparationDate(identification
							.getDateOfSeparation()), Field.READONLY);

			Font defaultFont = Styles.getDefaultFont();

			add(header);
			header.setFont(Styles.getHeaderFont());

			add(imageField);

			for (int i = 0; i < items.length; i++) {
				items[i].setFont(defaultFont);

				add(items[i]);
			}
		}

		protected void sublayout(int width, int height) {
			layoutChild(header, width, 20);
			setPositionChild(header, 5, 5);

			layoutChild(imageField, width / 2, 100);
			setPositionChild(imageField, (width - 85), 25);

			for (int i = 0; i < items.length; i++) {
				layoutChild(items[i], width, 45);

				setPositionChild(items[i], 10, 25 + (i * 15));
			}

			// setExtent(width, 140);
			setExtent(width, (items.length * 25) + 5);
		}
	}

	private class FamilyLayoutManager extends Manager {
		private LabelField header;
		private RichTextField items[];
		private LabelField tableHeaders[];
		private RichTextField relName[];
		private LabelField relRship[];
		private LabelField relIsAlive[];
		private LabelField relReunite[];

		public FamilyLayoutManager() {
			super(0);

			items = new RichTextField[3];

			header = new LabelField("FAMILY DETAILS", Field.READONLY);
			header.setFont(Styles.getHeaderFont());

			tableHeaders = new LabelField[4];

			String headerTexts[] = { "Name", "R'ship", "Is Alive", "Reunite" };

			for (int i = 0; i < headerTexts.length; i++) {
				tableHeaders[i] = new LabelField(headerTexts[i]);

				tableHeaders[i].setFont(Styles.getSecondaryFont());
			}

			Vector relatives = record.getFamily().getAllRelatives();

			relName = new RichTextField[relatives.size()];
			relRship = new LabelField[relatives.size()];
			relIsAlive = new LabelField[relatives.size()];
			relReunite = new LabelField[relatives.size()];

			Font defaultFont = Styles.getDefaultFont();

			for (int i = 0; i < relatives.size(); i++) {
				Relative relative = (Relative) relatives.elementAt(i);

				relName[i] = new RichTextField(relative.getName(),
						Field.READONLY);
				relName[i].setFont(defaultFont);

				relRship[i] = new LabelField(relative.getRelationship(),
						Field.READONLY);
				relRship[i].setFont(defaultFont);

				relIsAlive[i] = new LabelField(String.valueOf(relative
						.isAlive()), Field.READONLY);
				relIsAlive[i].setFont(defaultFont);

				relReunite[i] = new LabelField(String.valueOf(relative
						.isShouldReunite()), Field.READONLY);
				relReunite[i].setFont(defaultFont);
			}

			String married = "no";
			String partnerName = "-";
			int children = record.getFamily().getNumberChildren();

			if (record.getFamily().isMarried()) {
				married = "yes";

				partnerName = record.getFamily().getPartnerName();
			}

			items[0] = new RichTextField("Married: " + married, Field.READONLY);
			items[1] = new RichTextField("Partner/Spouse Name: " + partnerName,
					Field.READONLY);
			items[2] = new RichTextField("Children: " + children,
					Field.READONLY);

			add(header);

			for (int i = 0; i < relatives.size(); i++) {
				add(relName[i]);
				add(relRship[i]);
				add(relIsAlive[i]);
				add(relReunite[i]);
			}

			for (int i = 0; i < tableHeaders.length; i++) {
				add(tableHeaders[i]);
			}

			for (int i = 0; i < items.length; i++) {
				items[i].setFont(defaultFont);

				add(items[i]);
			}
		}

		protected void sublayout(int width, int height) {
			layoutChild(header, width, 20);
			setPositionChild(header, 5, 5);

			int x[] = new int[] { 5, 125, 175, 225 };

			for (int i = 0; i < tableHeaders.length; i++) {
				layoutChild(tableHeaders[i], width / 3, 20);
				setPositionChild(tableHeaders[i], x[i], 25);
			}

			Vector relatives = record.getFamily().getAllRelatives();

			for (int i = 0; i < relatives.size(); i++) {
				layoutChild(relName[i], width / 4, 20);
				layoutChild(relRship[i], width / 4, 20);
				layoutChild(relIsAlive[i], width / 4, 20);
				layoutChild(relReunite[i], width / 4, 20);

				setPositionChild(relName[i], x[0], 40 + (20 * i));
				setPositionChild(relRship[i], x[1], 40 + (20 * i));
				setPositionChild(relIsAlive[i], x[2], 40 + (20 * i));
				setPositionChild(relReunite[i], x[3], 40 + (20 * i));
			}

			int offset = (relatives.size() * 20) + 40;

			for (int i = 0; i < items.length; i++) {
				layoutChild(items[i], width, 20);

				setPositionChild(items[i], 10, offset + (i * 15));
			}

			setExtent(width, 180);
		}

	}

	private class OtherDetailsLayoutManager extends Manager {
		private LabelField header;
		private RichTextField items[];
		private RichTextField fields[];

		public OtherDetailsLayoutManager() {
			super(0);

			items = new RichTextField[5];

			header = new LabelField("OTHER DETAILS");
			header.setFont(Styles.getHeaderFont());

			Caregiver caregiver = record.getCareGiver();
			
			items[0] = new RichTextField("Caregiver Details:", Field.READONLY);
			
			String name = (caregiver == null) ? "-" : caregiver.getName();
			name = (name == null) ? "-" : name;
			
			String profession = (caregiver == null) ? "-" : caregiver.getProfession();
			profession = (profession == null) ? "-" : profession;
			
			String relationship = (caregiver == null) ? "-" : caregiver.getRelationshipToChild();
			relationship = (relationship == null) ? "-" : relationship;
			
			items[1] = new RichTextField("Name: " + name, Field.READONLY);
			items[2] = new RichTextField("Profession: " + profession,
					Field.READONLY);
			items[3] = new RichTextField("R'ship to Child: " + relationship, Field.READONLY);
			items[4] = new RichTextField("Protection Concerns:", Field.READONLY);


			Font defaultFont = Styles.getDefaultFont();

			add(header);

			for (int i = 0; i < items.length; i++) {
				items[i].setFont(defaultFont);

				add(items[i]);
			}

			ProtectionConcerns protectionConcerns = record
					.getProtectionConcerns();

			if (protectionConcerns != null) {
				ProtectionConcern concerns[] = protectionConcerns.getConcerns();
				
				fields = new RichTextField[0];

				for (int i = 0; i < concerns.length; i++) {
					if (concerns[i].isStatus()) {
						RichTextField field = new RichTextField("   "
								+ concerns[i].getName(), Field.READONLY);
						
						field.setFont(Styles.getSecondaryFont());
						
						Arrays.add(fields, field);
						
						add(field);
					}
				}
			}

		}

		protected void sublayout(int width, int height) {
			layoutChild(header, width, 20);
			setPositionChild(header, 5, 5);

			for (int i = 0; i < items.length; i++) {
				layoutChild(items[i], width, 20);

				setPositionChild(items[i], 10, 25 + (i * 15));
			}

			int fieldsOffset = 0;
			
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					layoutChild(fields[i], width, 20);

					setPositionChild(fields[i], 10, 100 + (i * 15));
				}
				
				fieldsOffset = (fields.length * 25) + 5;
			}

			setExtent(width, 80 + fieldsOffset);
		}

	}

	private class OptionsLayoutManager extends Manager {
		private LabelField header;

		public OptionsLayoutManager() {
			super(0);

			header = new LabelField("OPTIONS");
			header.setFont(Styles.getHeaderFont());

			add(header);
		}

		protected void sublayout(int width, int height) {
			layoutChild(header, width, 20);
			setPositionChild(header, 5, 5);

			setExtent(width, 30);
		}
	}
}
