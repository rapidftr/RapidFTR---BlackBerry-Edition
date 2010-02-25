package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;

import com.rapidftr.layouts.BorderManager;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.services.PhotoServiceListener;
import com.rapidftr.services.ServiceManager;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordCreationScreen extends DisplayPage {

	private String id;
	private EncodedImage photo;
	BitmapField imageField;

	public static final int SEARCH_ACTION = 3;
	
	public void initializePage(Object userInfo) {
		Hashtable hashTable = (Hashtable) userInfo;

		EncodedImage photo = (EncodedImage) hashTable.get("photo");
		String id = (String) hashTable.get("id");
		String user = (String) hashTable.get("user");

		this.id = id;
		this.photo = photo;

		String headerText = "logged in: " + user;

		LabelField footerField = new LabelField("ID #: " + id) {
			public void paint(Graphics graphics) {
				graphics.setColor(Color.CADETBLUE);
				super.paint(graphics);
			}
		};

		final Font footerFont = Styles.getTitleFont();

		footerField.setFont(footerFont);

		imageField = new BitmapField(photo.getBitmap());

		BorderManager manager = new BorderManager(headerText, imageField,
				footerField, true);

		manager.retakeButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onRetake();
			}
		});

		manager.newInfoButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onEnterNewInfo();
			}
		});

		manager.searchAndEditButton
				.setChangeListener(new FieldChangeListener() {
					public void fieldChanged(Field field, int context) {
						onSearchAndEdit();
					}
				});

		add(manager);
	}

	public boolean onClose() {
		popScreen(POP_ACTION, null);

		return true;
	}

	private void onRetake() {
		ServiceManager.getPhotoService().startCamera( new PhotoServiceListener() {

			public void handlePhoto(EncodedImage encodedImage) {
				EncodedImage e2 = Utilities.getScaledImage(encodedImage, 100);

				imageField.setBitmap(e2.getBitmap());	
			}
			
		});
	}

	private void onEnterNewInfo() {
		ChildRecord record = new ChildRecord();

		record.setRecordId(id);
		record.setPhoto(photo.getData());

		Hashtable userInfo = new Hashtable();

		userInfo.put("record", record);
		userInfo.put("type", String.valueOf(NavigatorScreen.TYPE_NEW));

		pushScreen(1, userInfo);
	}

	private void onSearchAndEdit() {
		pushScreen(SEARCH_ACTION, null);
	}
}
