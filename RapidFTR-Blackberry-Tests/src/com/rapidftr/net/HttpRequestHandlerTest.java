package com.rapidftr.net;

import com.rapidftr.datastore.MockStore;
import com.rapidftr.utilities.HttpSettings;
import com.rapidftr.utilities.Settings;
import com.rapidftr.utilities.Store;
import com.sun.me.web.request.*;
import org.junit.Before;
import org.junit.Test;

import javax.microedition.io.HttpConnection;
import java.io.IOException;

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
    public void shouldInvokeUpdateProcessedRequestsWithSuccessfulPostRequest() throws IOException {
        requestHandler.startNewProcess();
        Part apart = new Part("hello".getBytes(), null);
        Part[] parts = new Part[]{apart};
        PostData postData = new PostData(parts, "");
        Request request = Request.createPostRequest("http://www.rapidftr.com/relativeurl;deviceside=true;ConnectionTimeout=10000",
                null, getAuthToken(), requestHandler, postData, context);
        Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_OK);
        when(httpGateway.perform(request)).thenReturn(response);

        requestHandler.post("relativeurl", null, new Arg[] {}, postData, context);

        verify(requestCallBack).onRequestSuccess(context, response);
    }

	@Test
	public void shouldInvokeAuthenticationFailureOnRequestCallback()
			throws Exception {
		Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_UNAUTHORIZED);

		requestHandler.done(context, response);
        
		verify(requestCallBack).onAuthenticationFailure();
	}

	@Test
	public void shouldInvokeConnectionProblemOnRequestCallback()
			throws Exception {
		Response response = new Response();
        response.setResponseCode(HttpConnection.HTTP_CLIENT_TIMEOUT);

    	requestHandler.done(context, response);

		verify(requestCallBack).onConnectionProblem();
	}

	@Test
	public void shouldInvokeRequestFailureOnRequestCallbackOnAnyException()
			throws Exception {
		Response response = new Response();
        response.setException(new Exception());

		requestHandler.done(context, response);
        
		verify(requestCallBack).onRequestFailure(context,response.getException());

	}

    private Arg[] getAuthToken() {
		Arg[] newArgs = new Arg[1];
		newArgs[0] = new Arg(Arg.AUTHORIZATION,
				"RFTR_Token " + settings.getAuthorizationToken());
		return newArgs;
	}


}
