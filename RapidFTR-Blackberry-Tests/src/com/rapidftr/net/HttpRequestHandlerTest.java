package com.rapidftr.net;

import com.rapidftr.datastore.MockStore;
import com.rapidftr.utilities.HttpSettings;
import com.rapidftr.utilities.Settings;
import com.rapidftr.utilities.Store;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Request;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import javax.microedition.io.HttpConnection;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class HttpRequestHandlerTest {

    private HttpGateway httpGateway;
    private HttpBatchRequestHandler requestHandler;
    private RequestCallBack requestCallBack;
    private Object context;
    private Settings settings;

    @Before
    public void setUp() {
        context = mock(Object.class);
        requestCallBack = mock(RequestCallBack.class);
        final Store settingsStore = new MockStore();
        settings = new Settings(settingsStore);
        HttpSettings httpSettings = new HttpSettings(settings);
        httpSettings.setHost("http://www.rapidftr.com");
        httpGateway = mock(HttpGateway.class);
        HttpServer httpServer = new HttpServer(httpSettings, httpGateway);
        HttpService httpService = new HttpService(httpServer, settings);
        requestHandler = new HttpBatchRequestHandler(httpService);
        requestHandler.setRequestCallBack(requestCallBack);
    }

    @Test
    public void shouldInvokeRequestSuccessCallbackWithSuccessfulPostRequest() throws Exception {

        PostData postData = RequestFactory.createPostData();
        Request request = RequestFactory.createPostRequest("http://www.rapidftr.com/relativeurl", settings.getAuthorizationToken(),
                postData, requestHandler, context);
        Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_OK);
        when(httpGateway.perform(request)).thenReturn(response);

        requestHandler.startNewProcess();
        requestHandler.post("relativeurl", null, new Arg[]{}, postData, context);

        waitForRequestToComplete();
        verify(requestCallBack).onRequestSuccess(context, response);
    }

    @Test
    public void shouldAccumulateErrorsFromMultipleRequests() throws Exception {

        Request firstRequest = RequestFactory.createGetRequest("http://www.rapidftr.com/firsturl", settings.getAuthorizationToken(),
                requestHandler, context);
        Request secondRequest = RequestFactory.createGetRequest("http://www.rapidftr.com/secondurl", settings.getAuthorizationToken(),
                requestHandler, context);

        Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_CLIENT_TIMEOUT);
        when(httpGateway.perform(firstRequest)).thenReturn(response);
        when(httpGateway.perform(secondRequest)).thenReturn(response);

        requestHandler.startNewProcess(2);
        requestHandler.get("firsturl", null, new Arg[] {}, context);
        requestHandler.get("secondurl", null, new Arg[] {}, context);

        waitForRequestToComplete();
        assertEquals(2, requestHandler.getErrors().size());
        verify(requestCallBack).onProcessFail(anyString());
    }

    private void waitForRequestToComplete() throws InterruptedException {
        Thread.sleep(500);
    }


}
