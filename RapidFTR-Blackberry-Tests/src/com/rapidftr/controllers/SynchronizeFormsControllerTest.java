package com.rapidftr.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Hashtable;

import org.json.me.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.FormService;
import com.rapidftr.services.RequestCallBackImpl;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;

public class SynchronizeFormsControllerTest {

	private FormService formService;
	private UiStack uiStack;
	private SynchronizeFormsScreen synchronizeFormsScreen;
	private FormStore formStore;
	private SynchronizeFormsController synchronizeFormsController;
	private  HttpRequestHandler listener;
	private RequestCallBackImpl requestCallback;
	private Response response;
	private Result result;
	@Before
	public void setUp() {
		formService = mock(FormService.class);
		uiStack = mock(UiStack.class);
		synchronizeFormsScreen = mock(SynchronizeFormsScreen.class);
		formStore = mock(FormStore.class);
		synchronizeFormsController = new SynchronizeFormsController(
				formService, formStore, uiStack, synchronizeFormsScreen);
		requestCallback = new RequestCallBackImpl(synchronizeFormsScreen,synchronizeFormsController);
		listener = new HttpRequestHandler(requestCallback);
		formService.setListener(listener);
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
		verify(formService).cancelDownloadOfForms();
	}

	@Test
	public void shouldShowSynchronizeFormScreen() {
		synchronizeFormsController.synchronizeForms();
		verify(uiStack).pushScreen(synchronizeFormsScreen);
	}

	@Test
	public void shouldUpdateProgressBarValueOnScreen() {
		synchronizeFormsController.synchronizeForms();
		listener.updateRequestProgress(10, 100);
		verify(synchronizeFormsScreen).updateRequestProgress(10);
	}

	@Test
	public void shouldSendDownloadCompletedMessageToSynchronizeFormsScreenOnDownloadComplete() {
		synchronizeFormsController.synchronizeForms();
		synchronizeFormsController.onRequestSuccess(new Object(), response);
		verify(synchronizeFormsScreen).onProcessComplete();
		}

	@Test
	public void shouldSendDownloadFaileddMessageToSynchronizeFormsScreenOnJsonExceptionFormStore()
			throws JSONException {
		synchronizeFormsController.synchronizeForms();
		String jsonResult = new String("json");
		doThrow(new JSONException("Json Exception")).when(formStore)
				.storeForms(jsonResult);
		synchronizeFormsController.onRequestSuccess(new Object(), response);
		verify(synchronizeFormsScreen, never()).onProcessComplete();
		//verify(synchronizeFormsScreen).onProcessFail();

	}

	@Test
	public void shouldSendDownloadFailedErrorMessageToScreen() {
		synchronizeFormsController.synchronizeForms();
		requestCallback.handleConnectionProblem();
		verify(synchronizeFormsScreen).handleConnectionProblem();
	}

}
