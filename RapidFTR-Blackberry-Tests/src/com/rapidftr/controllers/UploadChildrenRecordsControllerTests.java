package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.UploadChildrenRecordsScreen;
import com.rapidftr.services.RequestCallBackImpl;
import com.rapidftr.services.UploadChildrenRecordsService;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;

public class UploadChildrenRecordsControllerTests {

	private UploadChildrenRecordsScreen uploadChildRecordsScreen;
	private UiStack uiStack;
	private UploadChildrenRecordsService uploadChildRecordsService;
	private UploadChildrenRecordsController uploadChildRecordsController;
	private  HttpRequestHandler listener;
	private RequestCallBackImpl requestCallback;
	private Response response;
	private Result result;

	@Before
	public void setUp() {
		uploadChildRecordsScreen = mock(UploadChildrenRecordsScreen.class);
		uiStack = mock(UiStack.class);
		uploadChildRecordsService = mock(UploadChildrenRecordsService.class);
		uploadChildRecordsController = new UploadChildrenRecordsController(
				uploadChildRecordsScreen, uiStack, uploadChildRecordsService);
		requestCallback = new RequestCallBackImpl(uploadChildRecordsScreen,uploadChildRecordsController);
		listener = new HttpRequestHandler(requestCallback);
		uploadChildRecordsService.setListener(listener);
		response = mock(Response.class);
		result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.toString()).thenReturn("json");
	}

	@Test
	public void shouldUploadChildRecords() {
		uploadChildRecordsController.uploadChildRecords();
		verify(uploadChildRecordsService).uploadChildRecords();
	}

	@Test
	public void shouldCancelUploadChildRecords() {
		uploadChildRecordsController.cancelUpload();
		verify(uploadChildRecordsService).cancelUploadOfChildRecords();
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
		verify(uploadChildRecordsScreen).uploadCompleted();
	}

	@Test
	public void shouldAlertUserOnUploadFailed() {
		uploadChildRecordsController.uploadChildRecords();
		Exception exception = mock(Exception.class);
		requestCallback.handleException(exception);
		verify(uploadChildRecordsScreen).uploadFailed();
	}

}
