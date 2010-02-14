package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.Persistable;

import com.rapidftr.ScreenManager;
import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.Identification;
import com.rapidftr.utilities.Utilities;

public class NavigatorScreen extends MainScreen implements Controller, Page {
	private int type;
	
	private ScreenManager screenManager;
	
	
	public static int TYPE_NEW = 1;
	public static int TYPE_EDIT = 2;
	
	public static final int IDENTIFICATION = 0;
	public static final int FAMILY = 1;
	public static final int OTHER = 2;
	public static final int OPTIONS = 3;
	public static final int SAVE = 4;
	public static final int DISCARD = 5;
	
	private static final String buttonNames[] = {
		"(1) Add/Edit Id. Details",
		"(2) Add/Edit Family Details", "(3) Add/Edit Other Details",
		"(4) Set Options", "Save", "Discard" };

	private static final String editButtonNames[] = {
		"(1) Edit Id. Details",
		"(2) Edit Family Details", "(3) Edit Other Details",
		"(4) Set Options", "Save", "Discard" };
	
	private ChildRecord record;
	
	public NavigatorScreen(ChildRecord childRecord, int type) {
		this.record = childRecord;
		this.type = type;

		String headerText = (type == TYPE_NEW) ? "Add New Info" : "Edit Info";
		
		add(new HeaderLayoutManager(headerText, record.getRecordId()));

		LayoutManager manager = new LayoutManager();

		add(manager);

		manager.buttons[IDENTIFICATION]
				.setChangeListener(new FieldChangeListener() {
					public void fieldChanged(Field field, int context) {
						onIdentification();
					}
				});
		
		manager.buttons[SAVE]
						.setChangeListener(new FieldChangeListener() {
							public void fieldChanged(Field field, int context) {
								onSaveRecord();
							}
						});
		
		manager.buttons[DISCARD]
						.setChangeListener(new FieldChangeListener() {
							public void fieldChanged(Field field, int context) {
								onClose();
							}
						});
	}

	public void setUserInfo(Object userInfo) {
	}
	
	public void addScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}
	
	public void handleSave(Persistable data) {
		if ( data instanceof Identification ) {
			record.setName("Any New");
			
			Identification identification = (Identification)data;
			
			record.setIdentification(identification);
		}
		
		Dialog.alert("Added Identification Data");
	}
	
	private MenuItem _identification = new MenuItem("Add/Edit Ident. Details",
			110, 10) {
		public void run() {
			onIdentification();
		}
	};

	private MenuItem _close = new MenuItem("Close Page", 110, 10) {
		public void run() {
			onClose();
		}
	};

	protected void makeMenu(Menu menu, int instance) {
		menu.add(_identification);
		menu.add(_close);
	}

	public boolean onClose() {
		screenManager.closeScreen(ScreenManager.STATUS_CLOSE, record.getRecordId());
	
		return true;
	}

	private void onIdentification() {
		IdentificationScreen screen = new IdentificationScreen();
		
		screen.setUserInfo(record.getRecordId());
		
		screen.addController(this);
		
		this.getUiEngine().pushScreen(screen);
	}
	
	private void onSaveRecord() {
		RecordReviewScreen reviewScreen = new RecordReviewScreen();
		
		reviewScreen.setUserInfo(record);
		
		reviewScreen.addScreenManager( new ScreenManager() {

			public void closeScreen(int status, Object userInfo) {
				screenManager.closeScreen(status, userInfo);
			}
		});
		
		this.getUiEngine().pushScreen(reviewScreen);
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
			
			add(imageField);
			
			for (int i = 0; i < buttons.length; i++) {
				limit = (i >= (buttons.length -2)) ? 100 : limit;
				
				buttons[i] = new Button(names[i], limit);

				buttons[i].setFont(defaultFont);
	     
				add(buttons[i]);
				
				if ( i < (buttons.length -2)) {
					ticks[i] = new BitmapField(Utilities.getScaledBitmap("img/checkmark.gif", 10));
					
					add(ticks[i]);
				}
			}
		}

		protected void sublayout(int width, int height) {
			for (int i = 0; i < buttons.length - 2; i++) {
				layoutChild(buttons[i], width, 30);

				layoutChild(ticks[i], 40, 40);
				
				setPositionChild(buttons[i], 30, 10 + (i * 30));
				setPositionChild(ticks[i], 10, 20 + (i * 30));
			}
			
			layoutChild(buttons[buttons.length - 2], width/2, 30);
			layoutChild(buttons[buttons.length - 1], width/2, 30);

			int y = 10 + ((buttons.length - 1) * 30);
			
			setPositionChild(buttons[buttons.length - 2], 20, y);
			setPositionChild(buttons[buttons.length - 1], (width - 130), y);

			layoutChild(imageField, width/2, 70);

			setPositionChild(imageField, (width - 80), 10);
			
			setExtent(width, 200);
		}

	}
}
