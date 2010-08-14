package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.microedition.io.HttpConnection;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class FormServiceTest {

	private HttpService httpService;
	private FormService formService;
	private FormServiceListener formServiceListener;
	private Object context;

	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		formService = new FormService(httpService);
		formServiceListener = mock(FormServiceListener.class);
		formService.setListener(formServiceListener);
		context = mock(Object.class);
	}

	@Test
	public void shouldSendGetRequestToSever() {
		formService.downloadForms();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		verify(httpService).get("published_form_sections", null, httpArgs,
				formService);
	}

	@Test
	public void shouldSendDownloadStatusToServiceListener() {
		int received = 10;
		int total = 100;
		((RequestListener) formService).readProgress(context, received, total);
		verify(formServiceListener).updateDownloadStatus(received, total);
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

		((RequestListener) formService).done(context, response);

		verify(formServiceListener).onDownloadComplete(json);
	}

	@Test
	public void shouldSendAuthenticationFailureMesssageToServiceListener()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_UNAUTHORIZED);
		((RequestListener) formService).done(context, response);
		verify(formServiceListener).onAuthenticationFailure();
	}

	@Test
	public void shoudlSendDownloadFailedErrorMessageToServiceListenerOnFailure()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_CLIENT_TIMEOUT);

		((RequestListener) formService).done(context, response);
		verify(formServiceListener).onConnectionProblem();
	}

	@Test
	public void shouldSendDownloadFailedErrorMessageToServiceListenerOnAnyException()
			throws Exception {
		Response response = mock(Response.class);
		when(response.getException()).thenReturn(new Exception());
		((RequestListener) formService).done(context, response);
		verify(formServiceListener).onDownloadFailed();

	}

}
