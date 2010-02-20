package com.rapidftr.screens;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

import com.rapidftr.Main;
import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.controls.BorderedPasswordField;
import com.rapidftr.controls.Button;
import com.rapidftr.services.ServiceException;
import com.rapidftr.services.ServiceManager;
import com.rapidftr.utilities.Properties;
import com.rapidftr.utilities.Styles;

public class LoginScreen extends DisplayPage {
	public static final int HOME_SCREEN_ACTION = 1;
	
	private LayoutManager layoutManager;

	public LoginScreen() {
		super();
			
		int limit = 50;
	
		Button okButton = new Button("OK", limit);
		Button closeButton = new Button("Close", limit);

		layoutManager = new LayoutManager(okButton, closeButton);

		add(layoutManager);

		FieldChangeListener closeListener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onClose();
			}
		};

		closeButton.setChangeListener(closeListener);

		FieldChangeListener okListener = new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onLaunch();
			}
		};

		okButton.setChangeListener(okListener);
	}
	
	private MenuItem _next = new MenuItem("Next", 110, 10) {
		public void run() {
			onLaunch();
		}
	};

	private MenuItem _close = new MenuItem("Close " + Main.APPLICATION_NAME,
			110, 10) {
		public void run() {
			onClose();
		}
	};
	
	protected void makeMenu(Menu menu, int instance) {
		menu.add(_next);
		menu.add(_close);
	}
	
	private void onLaunch() {
		String userName = layoutManager.usernameField.getText();
		String password = layoutManager.passwordField.getText();

		String hostName = layoutManager.hostField.getText();
		
		if ( hostName != null ) {
			Properties.getInstance().setHostName(hostName);
		}
		
		try {
			boolean loginResult = ServiceManager.getLoginService().login(
					userName, password);

			if (loginResult) {
				pushScreen(HOME_SCREEN_ACTION, null);	
			} else {
				Dialog.alert("Invalid login.\nTry again.");
			}
		} catch (ServiceException se) {
			System.out.println("Service Exception " + se);
		}
	}

	public boolean onClose() {
		Dialog.alert("Closing " + Main.APPLICATION_NAME);
		System.exit(0);
		return true;
	}

	/**
	 * Layout Manager
	 * 
	 */

	private class LayoutManager extends Manager {
		public BorderedEditField usernameField;
		public BorderedPasswordField passwordField;
		public BorderedEditField hostField;
		
		private LabelField header;
		private Button okButton;
		private Button closeButton;
		private BitmapField imageField;

		public LayoutManager(Button okButton, Button closeButton) {
			super(0);

			this.okButton = okButton;
			this.closeButton = closeButton;
	
			final Font titleFont = Styles.getTitleFont();

			Font defaultFont = Styles.getDefaultFont();

			header = new LabelField("Login") {
				public void paint(Graphics graphics) {
					graphics.setColor(0x00008800);
					super.paint(graphics);
				}
			};

			header.setFont(titleFont);

			Bitmap logoImage = Bitmap.getBitmapResource("img/logo.jpg");

			imageField = new BitmapField(logoImage);

			usernameField = new BorderedEditField("User Name: ", "",
					defaultFont, 19);

			usernameField.setFont(defaultFont);

			passwordField = new BorderedPasswordField("Password: ", "");
			passwordField.setFont(defaultFont);

			hostField = new BorderedEditField("Host: ", "");
			hostField.setFont(defaultFont);
			
			add(imageField);
			add(header);
			add(usernameField);
			add(passwordField);
			add(hostField);
			add(okButton);
			add(closeButton);
		}

		protected void sublayout(int width, int height) {
			layoutChild(imageField, width, 50);
			layoutChild(header, width, 25);
			layoutChild(usernameField, width, 25);
			layoutChild(passwordField, width, 25);
			layoutChild(hostField, width, 25);

			layoutChild(okButton, width / 4, 25);
			layoutChild(closeButton, width / 4, 25);

			setPositionChild(imageField, (width - imageField.getWidth()) / 2,
					10);
			setPositionChild(header, (width - header.getWidth()) / 2, 60);
			setPositionChild(usernameField, 10 + (width - usernameField
					.getWidth()) / 2, 100);
			setPositionChild(passwordField, 10 + (width - passwordField
					.getWidth()) / 2, 125);
			setPositionChild(hostField, 10 + (width - hostField
					.getWidth()) / 2, 150);
			
			setPositionChild(okButton, 60, 180);
			setPositionChild(closeButton, 160, 180);

			int actualHeight = 220;

			setExtent(width, actualHeight);
		}
	}
}
