package com.rapidftr.controllers;

import java.io.IOException;
import java.util.Vector;

import org.json.me.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.FormService;
import com.rapidftr.services.FormServiceListener;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.stub;

import static org.mockito.Mockito.*;

public class SynchronizeFormsControllerTest {

	private FormService formService;
	private UiStack uiStack;
	private SynchronizeFormsScreen synchronizeFormsScreen;
	private FormStore formStore;
	private SynchronizeFormsController synchronizeFormsController;

	@Before
	public void setUp() {
		formService = mock(FormService.class);
		uiStack = mock(UiStack.class);
		synchronizeFormsScreen = mock(SynchronizeFormsScreen.class);
		formStore = mock(FormStore.class);
		synchronizeFormsController = new SynchronizeFormsController(
				formService, formStore, uiStack, synchronizeFormsScreen);
	}

	@Test
	public void shouldSetFormSerivceListenerOnFormService() {
		verify(formService).setListener(
				(FormServiceListener) synchronizeFormsController);
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
		synchronizeFormsController.updateDownloadStatus(10, 100);
		verify(synchronizeFormsScreen).updateDownloadProgessBar(10);
	}

	@Test
	public void shouldSendDownloadCompletedMessageToSynchronizeFormsScreenOnDownloadComplete() {
		synchronizeFormsController.synchronizeForms();
		String jsonResult = new String("json");
		synchronizeFormsController.onDownloadComplete(jsonResult);
		verify(synchronizeFormsScreen).downloadCompleted();
	}

	@Test
	public void shouldSendDownloadFaileddMessageToSynchronizeFormsScreenOnJsonExceptionFormStore()
			throws JSONException {
		synchronizeFormsController.synchronizeForms();
		String jsonResult = new String("json");
		doThrow(new JSONException("Json Exception")).when(formStore)
				.storeForms(jsonResult);
		synchronizeFormsController.onDownloadComplete(jsonResult);
		verify(synchronizeFormsScreen, never()).downloadCompleted();
		verify(synchronizeFormsScreen).downloadFailed();

	}

	@Test
	public void shouldSendDownloadFailedErrorMessageToScreen() {
		synchronizeFormsController.synchronizeForms();
		synchronizeFormsController.onConnectionProblem();
		verify(synchronizeFormsScreen).downloadFailed();
	}

}
