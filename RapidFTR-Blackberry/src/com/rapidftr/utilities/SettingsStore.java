package com.rapidftr.utilities;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import java.util.Hashtable;

public class SettingsStore {

	private static final String DEFAULT_HOST = "defaulthost.foo.org";

	private static final String DEFAULT_USERNAME = "default.user";

	private static final long KEY = "com.rapidftr.utilities.ftrstore"
			.hashCode();

	private static final String KEY_LAST_USED_USERNAME = "Last_Used_Username";
	private static final String KEY_LAST_USED_HOST = "Last_Used_Host";

	private final PersistentObject persistentObject = PersistentStore
			.getPersistentObject(KEY);

	private Hashtable contents;
	private static final String AUTHORISATION_TOKEN = "Authorisation_Token";
	private static final String CURRENT_USER = "Current_User";

	public SettingsStore() {
		loadContentsHashtable();
	}

	private void loadContentsHashtable() {
		if (persistentObject.getContents() == null) {
			persistentObject.setContents(new Hashtable());
		}

		Object contentsObject = (Hashtable) persistentObject.getContents();
		if (!(contentsObject instanceof Hashtable)) {
			persistentObject.setContents(new Hashtable());
		}
		contents = (Hashtable) contentsObject;
	}

	public String getLastUsedLoginUsername() {
		return getString(KEY_LAST_USED_USERNAME, DEFAULT_USERNAME);
	}

	public String getLastUsedLoginHost() {
		return getString(KEY_LAST_USED_HOST, DEFAULT_HOST);
	}

	public void setLastUsedUsername(String value) {
		setString(KEY_LAST_USED_USERNAME, value);
	}

	private String getString(String key) {
		return getString(key, "");
	}

	private String getString(String key, String def) {
		if (contents.containsKey(key)) {
			return "" + contents.get(key);
		} else {
			return def;
		}
	}

	private void setString(String key, String value) {
		if (value == null) {
			value = "";
		}
		contents.put(key, value);
		persistentObject.commit();
	}

	public void setAuthorisationToken(String authorisationToken) {
		setString(AUTHORISATION_TOKEN, authorisationToken);
	}

	public void setCurrentlyLoggedIn(String userName) {
		setString(CURRENT_USER, userName);
	}

	public String getCurrentlyLoggedIn() {
		return getString(CURRENT_USER);
	}

	public String getAuthorizationToken() {

		return getString(AUTHORISATION_TOKEN);
	}

	public boolean isUserLoggedIn() {

		return !getAuthorizationToken().equals("");

	}

	public void clearState() {
		persistentObject.setContents(new Hashtable());
		contents = new Hashtable();
		persistentObject.commit();
	}

	public boolean isVerifiedUser() {
		throw new UnsupportedOperationException();
	}
}
