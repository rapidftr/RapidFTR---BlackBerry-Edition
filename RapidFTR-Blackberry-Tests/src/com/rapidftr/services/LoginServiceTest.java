package com.rapidftr.services;

import com.rapidftr.datastore.MockStore;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.Settings;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LoginServiceTest {
    private LoginSettings settings;
    private HttpService service;
    private MockStore store;

    @Before
    public void setUpStore(){
        service = mock(HttpService.class);
        store = new MockStore();
        settings = new LoginSettings(new Settings(store));
        settings.setLastUsedUserName("test");
        settings.setLastUsedPassword("pass");
        settings.setAuthorisationTokenForOfflineLogin("token");
    }

    @After
    public void clearData(){
        settings.clear();
    }

    @Test
    public void shouldAuthenticateOfflineUserWithLastUsedCredentials() {
        LoginService login = new LoginService(service, settings);
        Assert.assertTrue(login.offlineLogin("test", "pass"));
        String user = store.getString("current.user");
        Assert.assertNotNull(user);
        Assert.assertEquals("test", user);
    }
    
    @Test
    public void shouldNotAuthenticateOfflineUserCredentialsAreInValid() {
        LoginService login = new LoginService(service, settings);
        Assert.assertFalse(login.offlineLogin("test2", "pass"));
        String user = store.getString("current.user");
        Assert.assertNull(user);
    }
}
