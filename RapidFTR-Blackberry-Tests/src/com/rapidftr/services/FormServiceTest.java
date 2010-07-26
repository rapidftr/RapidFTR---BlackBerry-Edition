package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.HttpConnection;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.controllers.SynchronizeFormsController;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.net.HttpService;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
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
		verify(httpService).get("published_form_sections", null, formService);
	}

	@Test
	public void shouldSendDownloadStatusToServiceListener() {
		int received = 10;
		int total = 100;
		((RequestListener) formService).readProgress(context,
				received, total);
		verify(formServiceListener).updateDownloadStatus(received, total);
	}

	@Test
	public void shouldSendFormsToSynchronizeServiceListenerOnDownloadComplete()
			throws Exception {
		Response response = stubSuccessfulResponse();
		((RequestListener) formService).done(context, response);

		Vector forms = new Vector();

		Vector fieldList = new Vector();

		FormField textFormField = new FormField("age", "text_box");
		fieldList.add(textFormField);

		Vector optionString = new Vector();
		optionString.add("Approximate");
		optionString.add("Exact");

		FormField selectBoxFormField = new FormField("age_is", "select_box",
				optionString);
		fieldList.add(selectBoxFormField);

		Form form = new Form("Basic_details", "basic_details", fieldList);

		forms.add(form);

		verify(formServiceListener).onDownloadComplete(forms);
	}

	
	@Test
	public void shouldSendAuthenticationFailureMesssageToServiceListener() throws Exception
	{
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
		when(response.getException()).thenReturn(new Exception());
		((RequestListener) formService).done(context, response);
		verify(formServiceListener).onConnectionProblem();
	}

	private Response stubSuccessfulResponse() throws ResultException {
		Response response = mock(Response.class);
		String jsonFormString = String
				.format("[{\"name\":\"Basic_details\",\"unique_id\":\"basic_details\",\"fields\":[{\"name\":\"age\",\"type\":\"text_box\"},{\"name\":\"age_is\",\"type\":\"select_box\",\"option_strings\":"
						+

						"[\"Approximate\",\"Exact\"]}]}]");
		when(response.getResult()).thenReturn(
				Result.fromContent(jsonFormString, "application/json"));
		when(response.getCode()).thenReturn(200);
		return response;
	}

}
