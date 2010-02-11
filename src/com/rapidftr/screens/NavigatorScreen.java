package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.ScreenManager;
import com.rapidftr.controls.Button;
import com.rapidftr.layouts.HeaderLayoutManager;

public class NavigatorScreen extends MainScreen {
	private String id;
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
		"(1) Add/Edit Identification Details",
		"(2) Add/Edit Family Details", "(3) Add/Edit Other Details",
		"(4) Set Options", "Save", "Discard" };

	private static final String editButtonNames[] = {
		"(1) Edit Identification Details",
		"(2) Edit Family Details", "(3) Edit Other Details",
		"(4) Set Options", "Save", "Discard" };
	
	public NavigatorScreen(String id, int type) {
		this.id = id;
		this.type = type;

		String headerText = (type == TYPE_NEW) ? "Add New Info" : "Edit Info";
		
		add(new HeaderLayoutManager(headerText, id));

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

	public void addScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
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
		screenManager.closeScreen(ScreenManager.STATUS_CLOSE, id);
	
		return true;
	}

	private void onIdentification() {
		this.getUiEngine().pushScreen(new IdentificationScreen(id));
	}
	
	private void onSaveRecord() {
		RecordReviewScreen reviewScreen = new RecordReviewScreen(id);
		
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

		public LayoutManager() {
			super(0);

			final FontFamily fontFamily[] = FontFamily.getFontFamilies();

			defaultFont = fontFamily[0].getFont(FontFamily.TRUE_TYPE_FONT, 14);

			String names[] = (type == TYPE_NEW) ? buttonNames : editButtonNames;
			
			buttons = new Button[names.length];

			int limit = 250;

			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new Button(names[i], limit);

				buttons[i].setFont(defaultFont);
	     
				add(buttons[i]);
			}
		}

		protected void sublayout(int width, int height) {

			for (int i = 0; i < buttons.length; i++) {
				layoutChild(buttons[i], width, 30);

				setPositionChild(buttons[i], 20, 20 + (i * 30));
			}

			setExtent(width, 200);
		}

	}
}
