package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.Main;
import com.rapidftr.controls.Button;
import com.rapidftr.utilities.FtrStore;
import com.rapidftr.utilities.HttpServer;

public class LoginScreen extends MainScreen {

	private static final int BUTTON_WIDTH = 50;
	private static final int MAX_SIZE = 200;
	private static final XYEdges PADDING = new XYEdges(4, 4, 4, 4);

	private final BasicEditField usernameField = new BasicEditField(
			"Username:", "", MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);
	private final PasswordEditField passwordField = new PasswordEditField(
			"Password:", "", MAX_SIZE, USE_ALL_WIDTH);
	private final BasicEditField hostField = new BasicEditField("Host:", "",
			MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);

	private final FtrStore store;
	private final FtrController controller;

	public LoginScreen(FtrStore store, FtrController controller) {
		super();
		this.store = store;
		this.controller = controller;
		addLogo();
		add(new SeparatorField());
		usernameField.setPadding(PADDING);
		usernameField.setText(store.getLastUsedLoginUsername());
		add(usernameField);
		passwordField.setPadding(PADDING);
		add(passwordField);
		add(new SeparatorField());
		addButtons();
		passwordField.setFocus();
	}

	private void addHostField(FtrStore store) {
		if (hostField.getManager() != null) {
			return;
		}
		hostField.setPadding(PADDING);
		hostField.setText(store.getLastUsedLoginHost());
		int hostPosition = getHostPosition();
		if (hostPosition == -1) {
			add(hostField);
		} else {
			insert(hostField, hostPosition);
		}
	}

	private int getHostPosition() {
		int position = getFieldCount() - 2;
		if (position >= 0) {
			return position;
		} else {
			return -1;
		}
	}

	private void saveValuesToStore() {
		store.setLastUsedUsername(usernameField.getText());
		store.setLastUsedHost(hostField.getText());
	}

	private void addLogo() {
		Bitmap bitmap = Bitmap.getBitmapResource("img/logo.jpg");
		if (bitmap == null) {
			return;
		}

		BitmapField field = new BitmapField(bitmap, FIELD_HCENTER);
		field.setPadding(PADDING);
		add(field);
	}

	private void addButtons() {
		Button okButton = new Button("Login", BUTTON_WIDTH);
		Button closeButton = new Button("Close", BUTTON_WIDTH);

		closeButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onClose();
			}
		});

		okButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onLaunch();
			}
		});

		Manager manager = new HorizontalFieldManager(FIELD_HCENTER);
		manager.setPadding(PADDING);
		okButton.setPadding(PADDING);
		manager.add(okButton);
		closeButton.setPadding(PADDING);
		manager.add(closeButton);
		add(manager);
	}

	protected void onChangeHost() {
		addHostField(store);
	}

	private void onLaunch() {
		saveValuesToStore();
		
		HttpServer server = new HttpServer();
		try {
			Dialog.inform("about to hit google");
			Hashtable foo = server.getAsHtmlFromServer("www.google.com");
			Dialog.inform("It worked maybe");
		} catch (Exception e) {
			Dialog.inform("broked");
			Dialog.inform(e.getClass().toString());
			Dialog.inform(e.getMessage());
			e.printStackTrace();
		}
		
	}

	protected void makeMenu(Menu menu, int instance) {
		MenuItem changeHostMenuItem = new MenuItem("Change Host", 1, 1) {
			public void run() {
				onChangeHost();
			}
		};
		menu.add(changeHostMenuItem);
	}

	protected boolean navigationClick(int status, int time) {
		return true;
	}

	public boolean onClose() {
		Dialog.alert("Closing " + Main.APPLICATION_NAME);
		System.exit(0);
		return true;
	}
}
