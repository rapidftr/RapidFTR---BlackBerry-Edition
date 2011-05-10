package com.rapidftr.net;

import com.sun.me.web.request.Request;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RequestExecutorTest {
    private HttpGateway httpGateway;
    private RequestExecutor requestExecutor;

    @Before
    public void setup() {
        httpGateway = mock(HttpGateway.class);
        requestExecutor = new RequestExecutor(httpGateway);
    }

    @Test
    public void shouldPerformRequestViaGateway() throws IOException {
        Request request = Request.createGetRequest("http://www.google.com/");
        Response response = new Response();
        when(httpGateway.perform(request)).thenReturn(response);

        requestExecutor.execute(request);
        verify(httpGateway).perform(request);
    }

    @Test
    public void shouldInvokeListenerWithResponseAndContext() throws Exception {
        RequestListener listener = mock(RequestListener.class);
        Response response = new Response();
        response.setResponseCode(200);
        String context = "context";
        Request request = Request.createGetRequest("http://www.google.com/someurl;deviceside=true;ConnectionTimeout=10000", null, null, listener, context);
        when(httpGateway.perform(request)).thenReturn(response);

        requestExecutor.execute(request);
        verify(listener).done(context, response);
    }

    @Test
    public void shouldSetExceptionOnResponseOnError() throws IOException {
        TstRequestListener listener = new TstRequestListener();
        RuntimeException someException = new RuntimeException("exception thrown for testing");
        Request request = Request.createGetRequest("http://www.google.com/someurl;deviceside=true;ConnectionTimeout=10000", null, null, listener, null);
        when(httpGateway.perform(request)).thenThrow(someException);

        requestExecutor.execute(request);

        Response response = listener.getResponse();
        assertEquals(someException, response.getException());
    }


}
