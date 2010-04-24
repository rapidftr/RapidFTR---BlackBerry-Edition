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
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.Identification;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordEditScreen extends DisplayPage {
	private static final int LIMIT = 140;

	private static final String DEFAULT_IMAGE_NAME = "img/head.png";

	private ChildRecord record;

	public RecordEditScreen() {
		super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
	}

	public void initializePage(Object userInfo) {
		this.record = (ChildRecord) userInfo;

		add(new HeaderLayoutManager("Review Record", record.getRecordId()));
		add(createButtonManager());

		add(new LabelField("IDENTIFICATION DETAILS"));
		add(new SeparatorField());
		addFields();
	}

	private HorizontalFieldManager createButtonManager() {
		HorizontalFieldManager buttonManager = new HorizontalFieldManager();
		Button editButton = new Button("Edit Record", LIMIT);
		buttonManager.add(editButton);
		editButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onEdit();
			}
		});
		return buttonManager;
	}

	private void onEdit() {
		Hashtable userInfo = new Hashtable();

		userInfo.put("record", record);
		userInfo.put("type", String.valueOf(NavigatorScreen.TYPE_EDIT));

		//pushScreen(1, userInfo);
	}

	private void addFields() {

		Identification identification = record.getIdentification();

		Bitmap bitmap;
		int imageHeight = 80;

		if (record.getPhoto() != null) {
			EncodedImage encodedImage = Utilities
					.getEncodedImageFromBytes(record.getPhoto());

			bitmap = Utilities.getScaledBitmap(encodedImage, imageHeight);
		} else {
			bitmap = Utilities.getScaledBitmap(DEFAULT_IMAGE_NAME, imageHeight);
		}

		BitmapField imageField = new BitmapField(bitmap);

		addLabel("Name", record.getName());

		String sex = identification.isMale() ? "Male" : "Female";

		addLabel("Sex", sex);

		String approxOrExact = identification.isExactAge() ? "" : " (approx.)";

		addLabel("Age", identification.getAge() + approxOrExact);
		addLabel("Origin", identification.getOrigin());
		addLabel("Last Known Location", identification.getLastKnownLocation());
		
		addLabel("Date of Separation", identification.getFormattedSeparationDate());

		add(imageField);
	}

	private void addLabel(String labelName, String value) {
		HorizontalFieldManager manager = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		manager.add(new LabelField(labelName + " :", 0));
		TextField textField = new TextField(0);
		textField.setText(value);
		manager.add(textField);
		add(manager);
	}
}