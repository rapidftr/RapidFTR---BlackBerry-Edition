package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.ScreenManager;
import com.rapidftr.layouts.BorderManager;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordCreationScreen extends MainScreen implements Page {

	private String id;
	private EncodedImage photo;
	private ScreenManager screenManager;
	
	public void setUserInfo(Object userInfo) {
		Hashtable hashTable = (Hashtable)userInfo;
		
		EncodedImage photo = (EncodedImage)hashTable.get("photo");
		String id = (String)hashTable.get("id");
		String user = (String)hashTable.get("user");
		
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

		BitmapField imageField = new BitmapField(photo.getBitmap());

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

	public void addScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}

	private MenuItem _retakePhoto = new MenuItem("Retake Photo", 110, 10) {
		public void run() {
			onRetake();
		}
	};

	private MenuItem _enterNewInfo = new MenuItem("Add Details", 110, 10) {
		public void run() {
			onEnterNewInfo();
		}
	};

	private MenuItem _searchAndEdit = new MenuItem("Search + Edit", 110, 10) {
		public void run() {
			onSearchAndEdit();
		}
	};

	private MenuItem _close = new MenuItem("Close Page", 110, 10) {
		public void run() {
			onClose();
		}
	};

	protected void makeMenu(Menu menu, int instance) {
		menu.add(_retakePhoto);
		menu.add(_enterNewInfo);
		menu.add(_searchAndEdit);
		menu.add(_close);
	}

	public boolean onClose() {
		this.getUiEngine().popScreen(this);
		return true;
	}

	private void onRetake() {
		System.out.println("_retakePhoto");

		Dialog.alert("Retake Photo");
	}

	private void onEnterNewInfo() {
		ChildRecord record = new ChildRecord();
		
		record.setRecordId(id);
		record.setPhoto( photo.getData());
		
		final NavigatorScreen screen = new NavigatorScreen(record, NavigatorScreen.TYPE_NEW);
		
		final UiEngine engine = this.getUiEngine();
		
		screen.addScreenManager( new ScreenManager() {
			public void closeScreen(int status, Object userInfo) {
				if ( (status == ScreenManager.STATUS_CLOSE) || (status == ScreenManager.STATUS_SAVE) ) { 
					Utilities.popToHomeScreen(screen); 
				}
			}	
		});
		
		engine.pushScreen(screen);
	}

	private void onSearchAndEdit() {
		System.out.println("_searchAndEdit");

		Dialog.alert("Search + Edit");
	}
}
