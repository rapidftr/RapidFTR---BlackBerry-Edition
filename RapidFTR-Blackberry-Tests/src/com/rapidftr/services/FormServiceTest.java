package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.microedition.io.HttpConnection;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.datastore.FormStore;
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
	private Object context;
	private FormStore formStore;

	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		formStore = mock(FormStore.class);
		formService = new FormService(httpService,formStore);
		context = mock(Object.class);
	}

	@Test
	public void shouldSendGetRequestToSever() {
		formService.downloadForms();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		verify(httpService).get("published_form_sections", null, httpArgs,
				formService.requestHandler);
	}


	@Test
	public void shouldSendFormsToFormStoreOnDownloadComplete()
			throws Exception {
		Response response = mock(Response.class);
		Result mockResult = mock(Result.class);
		when(response.getResult()).thenReturn(mockResult);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_OK);
		String json = "json response";
		when(mockResult.toString()).thenReturn(json);
		formService.requestHandler.setRequestInProgress();
		formService.requestHandler.done(context, response);
		verify(formStore).storeForms(response.getResult().toString());
	}

}
