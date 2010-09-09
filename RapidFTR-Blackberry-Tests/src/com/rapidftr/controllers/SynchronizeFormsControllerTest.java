package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.me.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.FormService;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;

public class SynchronizeFormsControllerTest {

	private FormService formService;
	private UiStack uiStack;
	private SynchronizeFormsScreen synchronizeFormsScreen;
	private SynchronizeFormsController synchronizeFormsController;
	private Response response;
	private Result result;
	@Before
	public void setUp() {
		formService = mock(FormService.class);
		uiStack = mock(UiStack.class);
		synchronizeFormsScreen = mock(SynchronizeFormsScreen.class);
		synchronizeFormsController = new SynchronizeFormsController(
				formService,  uiStack, synchronizeFormsScreen);
		response = mock(Response.class);
		result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.toString()).thenReturn("json");
	}


	@Test
	public void shouldDownloadFormsFromFormService() throws IOException {
		synchronizeFormsController.synchronizeForms();
		verify(formService).downloadForms();
	}

	@Test
	public void shouldCancelDownloadingOfForms() {
		synchronizeFormsController.stopSynchronizingForms();
		verify(formService).cancelRequest();
	}

	@Test
	public void shouldShowSynchronizeFormScreen() {
		synchronizeFormsController.synchronizeForms();
		verify(uiStack).pushScreen(synchronizeFormsScreen);
	}

	@Test
	public void shouldUpdateProgressBarValueOnScreen() {
		synchronizeFormsController.synchronizeForms();
		synchronizeFormsController.screenCallBack.updateRequestProgress(10);
		verify(synchronizeFormsScreen).updateRequestProgress(10);
	}

	@Test
	public void shouldSendDownloadFailedErrorMessageToScreen() {
		synchronizeFormsController.synchronizeForms();
		synchronizeFormsController.screenCallBack.handleConnectionProblem();
		verify(synchronizeFormsScreen).handleConnectionProblem();
	}

}
