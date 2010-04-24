package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;

import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.Caregiver;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.Family;
import com.rapidftr.model.Identification;
import com.rapidftr.model.Options;
import com.rapidftr.model.ProtectionConcerns;
import com.rapidftr.model.Relative;
import com.rapidftr.utilities.Utilities;

public class NavigatorScreen extends DisplayPage {
	public static final int SAVE_ACTION = 1;
	public static final int ID_SCREEN_ACTION = 3;
	public static final int FAMILY_SCREEN_ACTION = 4;
	public static final int OTHER_SCREEN_ACTION = 5;
	public static final int OPTIONS_SCREEN_ACTION = 6;

	private int type;

	private LayoutManager manager;

	public static int TYPE_NEW = 1;
	public static int TYPE_EDIT = 2;

	public static final int IDENTIFICATION = 0;
	public static final int FAMILY = 1;
	public static final int OTHER = 2;
	public static final int OPTIONS = 3;
	public static final int SAVE = 4;
	public static final int DISCARD = 5;

	private static final String buttonNames[] = { "(1) Add/Edit Id. Details",
			"(2) Add/Edit Family Details", "(3) Add/Edit Other Details",
			"(4) Set Options", "Save", "Discard" };

	private static final String editButtonNames[] = { "(1) Edit Id. Details",
			"(2) Edit Family Details", "(3) Edit Other Details",
			"(4) Set Options", "Save", "Discard" };

	private ChildRecord record;

	public void initializePage(Object userInfo) {
		Hashtable data = (Hashtable) userInfo;

		this.record = (ChildRecord) (data.get("record"));

		this.type = Integer.parseInt((String) data.get("type"));

		String headerText = (type == TYPE_NEW) ? "Add New Info" : "Edit Info";

		add(new HeaderLayoutManager(headerText, record.getRecordId()));

		manager = new LayoutManager();

		add(manager);

		manager.buttons[IDENTIFICATION]
				.setChangeListener(new FieldChangeListener() {
					public void fieldChanged(Field field, int context) {
						onIdentification();
					}
				});

		manager.buttons[FAMILY].setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onAddFamily();
			}
		});

		manager.buttons[OTHER].setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onAddOther();
			}
		});

		manager.buttons[OPTIONS].setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSetOptions();
			}
		});

		manager.buttons[SAVE].setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSaveRecord();
			}
		});

		manager.buttons[DISCARD].setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onClose();
			}
		});
	}

	public void updatePage(Object userInfo, DisplayPage source) {
		if (source instanceof RecordReviewScreen) {
			handleEdit(userInfo, source);
		} else {
			handleSave(userInfo, source);
		}
	}

	public void handleEdit(Object data, DisplayPage source) {
		record = (data == null) ? record : (ChildRecord) data;
	}

	public void handleSave(Object data, DisplayPage source) {
		if (source instanceof IdentificationScreen) {
			Identification identification = (Identification) data;

			record.setName(identification.getName());

			record.setIdentification(identification);

			manager.ticks[IDENTIFICATION].setBitmap(Utilities.getScaledBitmap(
					"img/checkmark.gif", 10));

			Dialog.alert(getAlertMessage(IDENTIFICATION));
		} else if (source instanceof OtherDetailsScreen) {
			Hashtable userInfo = (Hashtable) data;

			ProtectionConcerns protectionConcerns = (ProtectionConcerns) (userInfo
					.get("protectionConcerns"));

			record.setProtectionConcerns(protectionConcerns);

			Caregiver careGiver = (Caregiver) (userInfo.get("caregiverDetails"));

			record.setCareGiver(careGiver);

			manager.ticks[OTHER].setBitmap(Utilities.getScaledBitmap(
					"img/checkmark.gif", 10));

			Dialog.alert(getAlertMessage(OTHER));
		} else if (source instanceof SetOptionsScreen) {
			Options options = (Options) data;

			record.setOptions(options);

			manager.ticks[OPTIONS].setBitmap(Utilities.getScaledBitmap(
					"img/checkmark.gif", 10));

			Dialog.alert(getAlertMessage(OPTIONS));
		}

		// test family data
		record.setFamily(getFamilyData());

	}

	public boolean onClose() {
		if (Dialog.ask(Dialog.D_YES_NO, "Are you sure?") == Dialog.YES) {
			//popScreen(RETURN_HOME_ACTION, record.getRecordId());
		}

		return true;
	}

	private String getAlertMessage(int action) {
		String message = null;
		String prefix = (type == TYPE_NEW) ? "Added" : "Updated";

		switch (action) {
		case IDENTIFICATION:
			message = prefix + " Identification Data";
			break;
		case FAMILY:
			message = prefix + " Family Data";
			break;
		case OTHER:
			message = prefix + " Protection Concerns";
			break;
		case OPTIONS:
			message = prefix + " Options";
			break;
		}

		return message;
	}

	private void onIdentification() {
		//pushScreen(ID_SCREEN_ACTION, record);
	}

	private void onAddFamily() {
		//pushScreen(FAMILY_SCREEN_ACTION, record.getRecordId());
	}

	private void onAddOther() {
		//pushScreen(OTHER_SCREEN_ACTION, record.getRecordId());
	}

	private void onSetOptions() {
		//pushScreen(OPTIONS_SCREEN_ACTION, record.getRecordId());
	}

	private void onSaveRecord() {
		if (record.getName() != null) {
			Hashtable userInfo = new Hashtable();

			userInfo.put("record", record);
			userInfo.put("type", String.valueOf(TYPE_NEW));

			//pushScreen(SAVE_ACTION, userInfo);
		} else {
			Dialog.alert("Child name cannot be blank");
		}
	}

	private Family getFamilyData() {
		Family family = new Family();

		Relative mother = new Relative(Relative.MOTHER, "Martha Doe", true,
				true);

		Relative father = new Relative(Relative.FATHER, "John Doe", false,
				false);

		Relative[] siblings = new Relative[2];

		siblings[0] = new Relative(Relative.SIBLING, "Wendy", true, true);
		siblings[1] = new Relative(Relative.SIBLING, "Lisa", true, true);

		family.setMother(mother);
		family.setFather(father);
		family.setSiblings(siblings);

		family.setMarried(false);

		return family;
	}

	/**
	 * Layout Manager
	 */
	private class LayoutManager extends Manager {
		private Font defaultFont;

		public Button buttons[];
		public BitmapField ticks[];
		private BitmapField imageField;

		public LayoutManager() {
			super(0);

			final FontFamily fontFamily[] = FontFamily.getFontFamilies();

			defaultFont = fontFamily[0].getFont(FontFamily.TRUE_TYPE_FONT, 14);

			String names[] = (type == TYPE_NEW) ? buttonNames : editButtonNames;

			buttons = new Button[names.length];

			ticks = new BitmapField[names.length - 2];

			int limit = 180;

			imageField = new BitmapField(Utilities.getScaledBitmapFromBytes(
					record.getPhoto(), 80));
			/*
			if (record.getPhoto() != null && record.getPhoto().length != 0) {
				imageField.setBitmap(bitmap)
			}*/

			add(imageField);

			for (int i = 0; i < buttons.length; i++) {
				limit = (i >= (buttons.length - 2)) ? 100 : limit;

				buttons[i] = new Button(names[i], limit);

				buttons[i].setFont(defaultFont);

				add(buttons[i]);

				if (i < (buttons.length - 2)) {
					ticks[i] = new BitmapField();

					add(ticks[i]);
				}
			}
		}

		protected void sublayout(int width, int height) {
			for (int i = 0; i < buttons.length - 2; i++) {
				layoutChild(buttons[i], width, 30);

				layoutChild(ticks[i], 40, 40);

				setPositionChild(buttons[i], 30, 10 + (i * 35));
				setPositionChild(ticks[i], 10, 20 + (i * 35));
			}

			layoutChild(buttons[buttons.length - 2], width / 2, 30);
			layoutChild(buttons[buttons.length - 1], width / 2, 30);

			int y = 10 + ((buttons.length - 1) * 30);

			setPositionChild(buttons[buttons.length - 2], 20, y);
			setPositionChild(buttons[buttons.length - 1], (width - 130), y);

			layoutChild(imageField, width / 2, 70);

			setPositionChild(imageField, (width - 80), 10);

			setExtent(width, 200);
		}

	}
}
