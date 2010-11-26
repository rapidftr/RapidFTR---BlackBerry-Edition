package com.rapidftr.services;

import com.rapidftr.utilities.Settings;

public class LoginSettings {
    private Settings settings;

    public LoginSettings(Settings settings) {
        this.settings = settings;
    }

    public boolean allowOfflineLogin(String user, String password) {
        return settings.hasBeenAuthorised()
                && user.equals(settings.getLastUserLoginName())
                && password.equals(settings.getLastUsedPassword());
    }

    public void authenticate(String user, String token) {
        settings.setCurrentlyLoggedIn(user);
        settings.setAuthorisationToken(token);
    }

    public void clear() {
        settings.clear();
    }

    public void setOfflineMode(boolean offline) {
        settings.setOfflineMode(offline);
    }

    public void setLastUsedUserName(String userName) {
        settings.setLastUsedUserName(userName);
    }

    public void setLastUsedPassword(String pass) {
        settings.setLastUsedPassword(pass);
    }

    public void setAuthorisationToken(String token) {
        settings.setAuthorisationToken(token);        
    }

    public String getAuthorizationTokenForOfflineLogin() {
        return settings.getAuthorisationTokenForOfflineLogin();
    }

    public void setAuthorisationTokenForOfflineLogin(String token) {
        settings.setAuthorisationTokenForOfflineLogin(token);
    }
}
