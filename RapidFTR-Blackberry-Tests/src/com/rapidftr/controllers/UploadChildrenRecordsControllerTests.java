package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.SyncAllScreen;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.RequestCallBackImpl;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;

public class UploadChildrenRecordsControllerTests {

	private SyncAllScreen uploadChildRecordsScreen;
	private UiStack uiStack;
	private ChildSyncService childSyncService;
	private SyncChildController uploadChildRecordsController;
	private  HttpRequestHandler listener;
	private RequestCallBackImpl requestCallback;
	private Response response;
	private Result result;

	@Before
	public void setUp() {
		uploadChildRecordsScreen = mock(SyncAllScreen.class);
		uiStack = mock(UiStack.class);
		childSyncService = mock(ChildSyncService.class);
		uploadChildRecordsController = new SyncChildController(
				uploadChildRecordsScreen, uiStack, childSyncService);
		requestCallback = new RequestCallBackImpl(uploadChildRecordsScreen,uploadChildRecordsController);
		listener = new HttpRequestHandler(requestCallback);
		childSyncService.setListener(listener);
		response = mock(Response.class);
		result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.toString()).thenReturn("json");
	}

	@Test
	public void shouldUploadChildRecords() {
		uploadChildRecordsController.uploadChildRecords();
		verify(childSyncService).uploadChildRecords();
	}

	@Test
	public void shouldCancelUploadChildRecords() {
		uploadChildRecordsController.cancelUpload();
		verify(childSyncService).cancelUploadOfChildRecords();
	}

	@Test
	public void shouldUpdateScreenOnConnectionProblem() {
		uploadChildRecordsController.uploadChildRecords();
		requestCallback.handleConnectionProblem();
		verify(uploadChildRecordsScreen).handleConnectionProblem();
	}

	@Test
	public void shouldUpdateStatusOfUploadOperation() {
		uploadChildRecordsController.uploadChildRecords();
		int bytes = 2;
		int total = 3;
		listener.updateRequestProgress(bytes, total);
		verify(uploadChildRecordsScreen).updateRequestProgress(
				(int) ((((double) bytes) / total) * 100));
	}

	@Test
	public void shouldDelegateLoginActionToDispatcher() {
		Dispatcher dispatcher = mock(Dispatcher.class);
		uploadChildRecordsController.setDispatcher(dispatcher);
		uploadChildRecordsController.login();
		verify(dispatcher).login();
	}

	@Test
	public void shouldAlertUserOnAuthenticationFailure() {
		uploadChildRecordsController.uploadChildRecords();
		requestCallback.handleUnauthorized();
		verify(uploadChildRecordsScreen).handleAuthenticationFailure();
	}

	@Test
	public void shouldAlertUserOnUploadComplete() {
		uploadChildRecordsController.uploadChildRecords();
		requestCallback.onSuccess(new Object(), response);
		verify(uploadChildRecordsScreen).onProcessComplete();
	}

	@Test
	public void shouldAlertUserOnUploadFailed() {
		uploadChildRecordsController.uploadChildRecords();
		Exception exception = mock(Exception.class);
		requestCallback.handleException(exception);
		verify(uploadChildRecordsScreen).onProcessFail();
	}

}
