package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;

import com.rapidftr.datastore.MockStore;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.DefaultStore;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.Settings;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginServiceTest {

    @Test
    public void shouldAuthenticateOfflineUser(){
        HttpService service = mock(HttpService.class);
        MockStore store = new MockStore("test");
        store.setString("offline","true");
        store.setString("last.used.username","test");
        store.setString("last.used.password","pass");
        LoginService login = new LoginService(service, new Settings(store));
        login.login("test","pass");
        store.getString("user");
    }
}
