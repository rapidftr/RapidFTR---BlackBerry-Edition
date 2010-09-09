package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.microedition.io.HttpConnection;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.HttpService;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class FormServiceTest {

	private HttpService httpService;
	private FormService formService;
	private  HttpRequestHandler listener;
	private Object context;
	private RequestCallBack requestCallback;

	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		formService = new FormService(httpService);
		requestCallback = mock(RequestCallBackImpl.class);
		listener = new HttpRequestHandler(requestCallback);
		formService.setListener(listener);
		context = mock(Object.class);
	}

	@Test
	public void shouldSendGetRequestToSever() {
		formService.downloadForms();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		verify(httpService).get("published_form_sections", null, httpArgs,
				listener);
	}

	@Test
	public void shouldSendDownloadStatusToServiceListener() {
		int received = 10;
		int total = 100;
		listener.setRequestInProgress();
		listener.readProgress(context, received, total);
		verify(requestCallback).updateRequestProgress(10);
	}

	@Test
	public void shouldSendFormsToSynchronizeServiceListenerOnDownloadComplete()
			throws Exception {
		Response response = mock(Response.class);
		Result mockResult = mock(Result.class);
		when(response.getResult()).thenReturn(mockResult);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_OK);
		String json = "json response";
		when(mockResult.toString()).thenReturn(json);
		listener.setRequestInProgress();
		listener.done(context, response);
		verify(requestCallback).onSuccess(context, response);
	}

	@Test
	public void shouldSendAuthenticationFailureMesssageToServiceListener()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_UNAUTHORIZED);
		listener.setRequestInProgress();
		listener.done(context, response);
		verify(requestCallback).handleUnauthorized();
	}

	@Test
	public void shoudlSendDownloadFailedErrorMessageToServiceListenerOnFailure()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_CLIENT_TIMEOUT);
		listener.setRequestInProgress();
		listener.done(context, response);
		verify(requestCallback).handleConnectionProblem();
	}

	@Test
	public void shouldSendDownloadFailedErrorMessageToServiceListenerOnAnyException()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getException()).thenReturn(new Exception());
		listener.setRequestInProgress();
		listener.done(context, response);
		verify(requestCallback).handleException(response.getException());

	}

}
