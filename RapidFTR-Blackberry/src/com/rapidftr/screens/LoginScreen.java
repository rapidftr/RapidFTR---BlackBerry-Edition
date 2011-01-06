package com.rapidftr.screens;

import net.rim.device.api.system.Characters;
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
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.services.ScreenCallBack;
import com.rapidftr.utilities.HttpSettings;

public class LoginScreen extends CustomScreen implements ScreenCallBack {

	private static final int MAX_SIZE = 200;

	private final PasswordEditField passwordField = new PasswordEditField(
			"Password:", "", MAX_SIZE, USE_ALL_WIDTH);
	private final BasicEditField usernameField = basicField("Username:");
	private final BasicEditField urlField = basicField("Url:");

	private final HttpSettings httpSettings;
	private Manager progressMsgFieldmanager;
	private LabelField progressMsg;
	private Button loginButton;
	private Manager buttonManager;
	private Button cancelButton;

	public LoginScreen(HttpSettings httpSettings) {
		super();
		this.httpSettings = httpSettings;
		layoutScreen();
		usernameField.setFocus();
	}

	private void layoutScreen() {
		usernameField.setPadding(PADDING);
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
		add(progressMsgFieldmanager);

	}

	private BasicEditField basicField(String field) {
		return new BasicEditField(field, "", MAX_SIZE, USE_ALL_WIDTH
				| TextField.NO_NEWLINE);
	}

	private void addField(BasicEditField field, String defaultValue) {
		if (field.getManager() != null) {
			return;
		}
		field.setPadding(PADDING);
		field.setText(defaultValue);
		insert(field, getFieldCount() - 2);
	}

	private void addButtons() {
		loginButton = new Button("Login");
		loginButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onLoginButtonClicked();
			}
		});

		cancelButton = new Button("Cancel");
		cancelButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onCancelButtonClicked();
			}
		});
		
		buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.setPadding(PADDING);
		add(buttonManager);
	}

	protected void makeMenu(Menu menu, int instance) {
		menu.add(new MenuItem("Change Url", 1, 1) {
			public void run() {
				addField(urlField, httpSettings.getHost());
			}
		});

		super.makeMenu(menu, instance);
	}

    public boolean isDirty() {
		return false;
	}

	private void clearProgressMessage() {
		progressMsg.setText("");
	}

	private void onLoginButtonClicked() {
		httpSettings.setHost(urlField.getText());
		
		usernameField.setFocus();
		getController().login(usernameField.getText(),
				passwordField.getText());
		showCancelButton();
	}

    private LoginController getController() {
        return ((LoginController) controller);
    }

    private void onCancelButtonClicked() {
    	showLoginButton();
	}

	private void showCancelButton() {
		buttonManager.deleteAll();
		buttonManager.add(cancelButton);
	}

	public void setUp() {
		showLoginButton();
		clearProgressMessage();
	}

	private void showLoginButton() {
		buttonManager.deleteAll();
		buttonManager.add(loginButton);
	}


	public void onAuthenticationFailure() {
		onProcessFail("Authentication Failure ");
	}

	public void onConnectionProblem() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				setProgressMessage("Connection Problem ");
				showLoginButton();
			}
		});
	}

	public void updateProgress(int progress) {

	}

	public void onProcessSuccess() {
		resetCredentials(true);
        getController().synchronizeForms();
	}

    public void onProcessFail(final String message) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
                if (!(message == null || "".equals(message.trim())))
                    setProgressMessage(message);
                showLoginButton();
                resetCredentials(false);
				passwordField.setFocus();
			}
		});
	}

    public void setProgressMessage(String message) {
        progressMsg.setText(message);
    }

	public boolean keyDown(int keycode, int time) {
		if (keycode == Characters.ESCAPE) {
			controller.homeScreen();
		}
		return super.keyDown(keycode, time);
	}

	public boolean keyChar(char code, int time, int arg2) {
		if (code == Characters.ESCAPE) {
			controller.homeScreen();
			return true;
		} else {
			return super.keyChar(code, time, arg2);
		}
	}

	public void resetCredentials(boolean resetUser) {
		if(resetUser)
			usernameField.setText("");
		passwordField.setText("");
	}
}
