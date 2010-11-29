package com.rapidftr.utilities;


public class Settings {

	private static final String LAST_USED_HOST_KEY = "last.used.host";
	
	private static final String DEFAULT_URL = "https://dev.rapidftr.com";

	private static final String AUTHORISATION_TOKEN = "authorisation.token";
	private static final String AUTHORISATION_TOKEN_FOR_OFFLINE_LOGIN = "temp.authorisation.token";
	private static final String CURRENT_USER = "current.user";

	
	private Store store;

	public Settings(Store store) {
		this.store = store;
	}

	public static String getDefaultHost() {
		return DEFAULT_URL;
	}

	public String getLastUsedLoginHost() {
		return store.getString(LAST_USED_HOST_KEY, DEFAULT_URL);
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
		return !"".equals(getCurrentlyLoggedIn());
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
	
	private boolean isNotEmpty(String value) {
		return (!"".equals(value) && value != null && value.trim().length() != 0);
	}

	public void clearAuthenticationInfo() {
		store.remove(CURRENT_USER);
        setAuthorisationTokenForOfflineLogin(AUTHORISATION_TOKEN);
        store.remove(AUTHORISATION_TOKEN);
	}

	public void clear() {
		store.clear();
	}

    public boolean hasBeenAuthorised() {
        return getAuthorisationTokenForOfflineLogin()!= null;
    }

    public String getLastUserLoginName() {
        return store.getString("last.used.username");
    }

    public String getLastUsedPassword() {
        return store.getString("last.used.password");
    }

    public void setOfflineMode(boolean mode) {
        store.setString("offline.mode", String.valueOf(mode));
    }

    public void setLastUsedUserName(String userName) {
        store.setString("last.used.username", userName);
    }

    public void setLastUsedPassword(String pass) {
        store.setString("last.used.password", pass);
    }

    public String getAuthorisationTokenForOfflineLogin() {
        return store.getString(AUTHORISATION_TOKEN_FOR_OFFLINE_LOGIN);
    }

    public void setAuthorisationTokenForOfflineLogin(String token) {
        store.setString(AUTHORISATION_TOKEN_FOR_OFFLINE_LOGIN, token);
    }
}
