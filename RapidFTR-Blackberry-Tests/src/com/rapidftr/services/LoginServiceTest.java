package com.rapidftr.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rapidftr.Key;
import com.rapidftr.datastore.MockStore;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.Settings;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;

public class LoginServiceTest {
    private LoginSettings settings;
    private HttpService httpService;
    private MockStore store;
    LoginService loginService;

    @Before
    public void setUpStore(){
        httpService = mock(HttpService.class);
        store = new MockStore(new Key("mock"));
        settings = new LoginSettings(new Settings(store));
        settings.setLastUsedUserName("test");
        settings.setLastUsedPassword("pass");
        settings.setAuthorisationTokenForOfflineLogin("token");
        loginService = new LoginService(httpService, settings);
    }

    @After
    public void clearData(){
        settings.clear();
    }

    @Test
    public void shouldAuthenticateOfflineUserWithLastUsedCredentials() {
       
        Assert.assertTrue(loginService.offlineLogin("test", "pass"));
        String user = store.getString("current.user");
        Assert.assertNotNull(user);
        Assert.assertEquals("test", user);
    }
    
    @Test
    public void shouldNotAuthenticateOfflineUserCredentialsAreInValid() {
        Assert.assertFalse(loginService.offlineLogin("test2", "pass"));
        String user = store.getString("current.user");
        Assert.assertNull(user);
    }
    
	@Test
	public void shouldRequestForPasswordRecovery(){
        String username = "username";
        String imei = "000000000000000";
		Arg[] postArgs = new Arg[]{new Arg("password_recovery_request[user_name]", username),
                new Arg("password_recovery_request[imei]", imei)};
		loginService.recoverPassword(username, imei);
		verify(httpService).post(eq("password_recovery_requests"),
				eq(postArgs),eq(new Arg[]{HttpUtility.HEADER_ACCEPT_JSON}),
				(com.sun.me.web.request.RequestListener)any(), eq((PostData)null),any());
	}
}
