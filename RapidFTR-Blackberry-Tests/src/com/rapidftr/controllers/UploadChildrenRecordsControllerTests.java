package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.UploadChildrenRecordsScreen;
import com.rapidftr.services.UploadChildrenRecordsService;

public class UploadChildrenRecordsControllerTests {

	private UploadChildrenRecordsScreen uploadChildRecordsScreen;
	private UiStack uiStack;
	private UploadChildrenRecordsService uploadChildRecordsService;
	private UploadChildrenRecordsController uploadChildRecordsController;

	@Before
	public void setUp() {
		uploadChildRecordsScreen = mock(UploadChildrenRecordsScreen.class);
		uiStack = mock(UiStack.class);
		uploadChildRecordsService = mock(UploadChildrenRecordsService.class);
		uploadChildRecordsController = new UploadChildrenRecordsController(
				uploadChildRecordsScreen, uiStack, uploadChildRecordsService);
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
		uploadChildRecordsController.onConnectionProblem();
		verify(uploadChildRecordsScreen).connectionProblem();
	}

	@Test
	public void shouldUpdateStatusOfUploadOperation() {
		uploadChildRecordsController.uploadChildRecords();
		int bytes = 2;
		int total = 3;
		uploadChildRecordsController.updateUploadStatus(bytes, total);
		verify(uploadChildRecordsScreen).updateUploadProgessBar(
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
		uploadChildRecordsController.onAuthenticationFailure();
		verify(uploadChildRecordsScreen).authenticationFailure();
	}

	@Test
	public void shouldAlertUserOnUploadComplete() {
		uploadChildRecordsController.uploadChildRecords();
		uploadChildRecordsController.onUploadComplete();
		verify(uploadChildRecordsScreen).uploadCompleted();
	}

	@Test
	public void shouldAlertUserOnUploadFailed() {
		uploadChildRecordsController.uploadChildRecords();
		uploadChildRecordsController.onUploadFailed();
		verify(uploadChildRecordsScreen).uploadFailed();
	}

}
