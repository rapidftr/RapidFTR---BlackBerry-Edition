package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.controllers.LoginController;
import com.rapidftr.controls.Button;
import com.rapidftr.net.ScreenCallBack;
import com.rapidftr.utilities.SettingsStore;

public class LoginScreen extends CustomScreen implements FieldChangeListener,
		ScreenCallBack {

	private static final int MAX_SIZE = 200;

	private final BasicEditField usernameField = new BasicEditField(
			"Username:", "", MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);
	private final PasswordEditField passwordField = new PasswordEditField(
			"Password:", "", MAX_SIZE, USE_ALL_WIDTH);
	private final BasicEditField hostField = new BasicEditField("Host:", "",
			MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);

	private final SettingsStore store;
	private Manager progressMsgFieldmanager;
	private LabelField progressMsg;
	private Button loginButton;
	private Manager buttonManager;
	private Button cancelButton;

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
		createProgressMsg();
	}

	private void createProgressMsg() {

		progressMsg = new LabelField();
		progressMsgFieldmanager = new HorizontalFieldManager(FIELD_HCENTER);
		progressMsgFieldmanager.setPadding(PADDING);
		progressMsg.setPadding(PADDING);
		progressMsgFieldmanager.add(progressMsg);

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

	private void addButtons() {
		loginButton = new Button("Login");
		loginButton.setChangeListener(this);

		cancelButton = new Button("Cancel");
		cancelButton.setChangeListener(this);
		buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.setPadding(PADDING);
		add(buttonManager);
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

	public void setProgressMsg(String msg) {

		progressMsg.setText(msg);

		try {
			add(progressMsgFieldmanager);
		} catch (IllegalStateException ex) {

		}

	}

	public boolean isDirty() {
		return false;
	}

	public void removeProgressMsgIfExist() {

		try {
			delete(progressMsgFieldmanager);
		} catch (IllegalArgumentException ex) {

		}

	}

	public void fieldChanged(Field field, int context) {

		if (field.equals(loginButton)) {
			onLoginButtonClicked();

		}
		if (field.equals(cancelButton)) {
			onCancelButtonClicked();
		}
	}

	private void onLoginButtonClicked() {

		((LoginController) controller).login(usernameField.getText(),
				passwordField.getText());
		setCacelButton();
	}

	private void onCancelButtonClicked() {
		cleanUp();
	}

	private void setCacelButton() {
		buttonManager.deleteAll();
		buttonManager.add(cancelButton);
	}

	public void setUp() {
		setLoginButton();
		removeProgressMsgIfExist();
	}

	private void setLoginButton() {
		buttonManager.deleteAll();
		buttonManager.add(loginButton);
	}

	public boolean onClose() {

		cleanUp();
		return super.onClose();

	}

	public void cleanUp() {

		removeProgressMsgIfExist();
		setLoginButton();
		((LoginController) controller).loginCancelled();
	}

	public void handleAuthenticationFailure() {
     onProcessFail("Authentication Failed");
	}


	public void handleConnectionProblem() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				setProgressMsg("Connection Problem");
				setLoginButton();
			}
		});
	}

	public void updateRequestProgress(int progress) {
		// TODO Auto-generated method stub
		
	}

	public void onProcessComplete() {
//		controller.popScreen();
		
	}

	public void onProcessFail(String failureMessage) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				setProgressMsg("Login Failed");
				setLoginButton();
			}
		});		
	}

	public void setProgressMessage(String message) {
		setProgressMsg(message);		
	}

}
