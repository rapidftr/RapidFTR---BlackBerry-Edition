package com.rapidftr.screens;

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
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controls.Button;
import com.rapidftr.utilities.SettingsStore;

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

	private final SettingsStore store;

	private LoginController loginController;

	public LoginScreen(SettingsStore store) {
		super();
		this.store = store;
		layoutScreen();
		passwordField.setFocus();
	}

	private void layoutScreen() {
		addLogo();
		add(new SeparatorField());
		usernameField.setPadding(PADDING);
		usernameField.setText(store.getLastUsedLoginUsername());
		add(usernameField);
		passwordField.setPadding(PADDING);
		add(passwordField);
		add(new SeparatorField());
		addButtons();
	}

	private void addHostField(SettingsStore store) {
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
                loginController.login(usernameField.getText(), passwordField.getText());
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

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}
	
	
}
