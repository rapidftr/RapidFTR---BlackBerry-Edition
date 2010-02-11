package com.rapidftr.screens;

import net.rim.device.api.system.Bitmap;
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
import com.rapidftr.utilities.Styles;
import com.rapidftr.utilities.Utilities;

public class RecordCreationScreen extends MainScreen {

	private String id;
	private ScreenManager screenManager;
	
	public RecordCreationScreen(Bitmap photo, String id, String user) {
		this.id = id;

		String headerText = "logged in: " + user;

		LabelField footerField = new LabelField("ID #: " + id) {
			public void paint(Graphics graphics) {
				graphics.setColor(Color.CADETBLUE);
				super.paint(graphics);
			}
		};

		final Font footerFont = Styles.getTitleFont();

		footerField.setFont(footerFont);

		BitmapField imageField = new BitmapField(photo);

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

	public RecordCreationScreen(final UiApplication application, String user) {
		this(null, null, user);
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
		final NavigatorScreen screen = new NavigatorScreen(id, NavigatorScreen.TYPE_NEW);
		
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
