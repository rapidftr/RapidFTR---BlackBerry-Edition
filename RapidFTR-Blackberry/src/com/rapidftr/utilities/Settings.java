package com.rapidftr.utilities;


public class Settings {

	private static final String LAST_USED_PORT_KEY = "last.used.port";
	private static final String LAST_USED_HOST_KEY = "last.used.host";
	
	private static final String DEFAULT_HOST = "dev.rapidftr.com";
	private static final String DEFAULT_PORT = "80";

	private static final String AUTHORISATION_TOKEN = "authorisation.token";
	private static final String CURRENT_USER = "current.user";

	
	private Store store;

	public Settings(Store store) {
		this.store = store;
	}

	public static String getDefaultHost() {
		return DEFAULT_HOST;
	}

	public static String getDefaultPort() {
		return DEFAULT_PORT;
	}

	public String getLastUsedLoginHost() {
		return store.getString(LAST_USED_HOST_KEY, DEFAULT_HOST);
	}

	public String getLastUsedLoginPort() {
		return store.getString(LAST_USED_PORT_KEY, DEFAULT_PORT);
	}

	public void setAuthorisationToken(String authorisationToken) {
		store.setString(AUTHORISATION_TOKEN, authorisationToken);
	}

	public void setCurrentlyLoggedIn(String userName) {
		store.setString(CURRENT_USER, userName);
	}

	public String getCurrentlyLoggedIn() {
		return store.getString(CURRENT_USER);
	}

	public String getAuthorizationToken() {
		return store.getString(AUTHORISATION_TOKEN);
	}

	public boolean isUserLoggedIn() {
		return !getAuthorizationToken().equals("");
	}

	public boolean isVerifiedUser() {
		throw new UnsupportedOperationException();
	}

	public int getTimeOut() {
		return 10000;
	}

	public void setLastUsedLoginHost(String host) {
		if (isNotEmpty(host))
			store.setString(LAST_USED_HOST_KEY, host);
		else
			store.setString(LAST_USED_HOST_KEY, getDefaultHost());
	}
	
	public void setLastUsedLoginPort(String port) {
		if (isNotEmpty(port))
			store.setString(LAST_USED_PORT_KEY, port);
		else
			store.setString(LAST_USED_PORT_KEY, getDefaultPort());
	}

	private boolean isNotEmpty(String value) {
		return (!"".equals(value) && value != null && value.trim().length() != 0);
	}

	public void clearAuthenticationInfo() {
		store.remove(AUTHORISATION_TOKEN);
	}

	public void clear() {
		store.clear();
		
	}

    public boolean isOfflineLogin() {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    public boolean isAuthorised() {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    public String getLastUserLoginName() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public String getLastUsedPassword() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
