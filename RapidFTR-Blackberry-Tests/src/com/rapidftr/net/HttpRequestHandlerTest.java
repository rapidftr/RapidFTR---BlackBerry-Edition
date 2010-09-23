package com.rapidftr.net;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.microedition.io.HttpConnection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.me.web.request.Response;

public class HttpRequestHandlerTest {

	
	private HttpBatchRequestHandler requestHandler;
	@Mock
	private RequestCallBack requestCallBack;
	@Mock
	private HttpService httpService;
	@Mock
	private Object context;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		requestHandler = new HttpBatchRequestHandler(httpService);
		requestHandler.setRequestCallBack(requestCallBack);
	}

//
//	@Test
//	public void shouldSendProgressStatusToRequestCallback() {
//		int received = 10;
//		int total = 100;
//		requestHandler.setRequestInProgress();
//		requestHandler.readProgress(context, received, total);
//		verify(requestCallBack).updateRequestProgress(10);
//	}


	@Test
	public void shouldSendAuthenticationFailureMesssageToRequestCallback()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_UNAUTHORIZED);
		requestHandler.done(context, response);
		verify(requestCallBack).onAuthenticationFailure();
	}

	@Test
	public void shoudlSendProcessFailedErrorMessageToRequestCallbackOnFailure()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_CLIENT_TIMEOUT);
    	requestHandler.done(context, response);
		verify(requestCallBack).onConnectionProblem();
	}

	@Test
	public void shouldSendProcessFailedErrorMessageToRequestCallbackOnAnyException()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getException()).thenReturn(new Exception());
		requestHandler.done(context, response);
		verify(requestCallBack).onRequestFailure(context,response.getException());

	}


}
