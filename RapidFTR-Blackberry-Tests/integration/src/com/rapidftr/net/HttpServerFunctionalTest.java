package com.rapidftr.net;

import com.rapidftr.datastore.MockStore;
import com.rapidftr.utilities.HttpSettings;
import com.rapidftr.utilities.Settings;
import com.sun.me.web.request.Response;
import org.junit.Test;

import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static junit.framework.Assert.assertEquals;

public class HttpServerFunctionalTest {

    @Test
    public void shouldGetHttpResponse() throws IOException {
        Settings settings = new Settings(new MockStore());
        HttpSettings httpSettings = new HttpSettings(settings);
        httpSettings.setHost("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=hello%20world");

        HttpServer server = new HttpServer(httpSettings, new HttpGateway(getConnectionFactory()));

        final Response response = server.getFromServer("", null, null);
        assertEquals(200, response.getCode());
    }

    private ConnectionFactory getConnectionFactory() {
        return new ConnectionFactory() {
            public HttpConnection openConnection(String uri) throws IOException {
                // strip options ;deviceside=true;ConnectionTimeout=10000
                String cleanUri = uri.substring(0, uri.indexOf(";deviceside=true"));
                URL url = new URL(cleanUri);
                return new HttpConnectionWrapper((HttpURLConnection) url.openConnection());
            }
            public boolean isNotConnected() { return false; }
            protected boolean isWIFIAvailable() { return true; }
        };
    }
}
