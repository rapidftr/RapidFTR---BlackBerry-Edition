package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.SyncChildScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.RequestCallBackImpl;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;

public class UploadChildrenRecordsControllerTests {

	private SyncChildScreen uploadChildRecordsScreen;
	private UiStack uiStack;
	private ChildSyncService childSyncService;
	private SyncChildController uploadChildRecordsController;
	private Response response;
	private Result result;

	@Before
	public void setUp() {
		uploadChildRecordsScreen = mock(SyncChildScreen.class);
		uiStack = mock(UiStack.class);
		childSyncService = mock(ChildSyncService.class);
		uploadChildRecordsController = new SyncChildController(
				uploadChildRecordsScreen, uiStack, childSyncService);
		response = mock(Response.class);
		result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.toString()).thenReturn("json");
	}

	@Test
	public void shouldUploadChildRecords() {
		uploadChildRecordsController.syncAllChildRecords();
		verify(childSyncService).syncAllChildRecords();
	}

	@Test
	public void shouldCancelUploadChildRecords() {
		uploadChildRecordsController.cancelUpload();
		verify(childSyncService).cancelRequest();
	}

	@Test
	public void shouldUpdateStatusOfUploadOperation() {
		uploadChildRecordsController.syncAllChildRecords();
		uploadChildRecordsController.getScreenCallBack().updateRequestProgress(10);
		verify(uploadChildRecordsScreen).updateRequestProgress(10);
	}

	@Test
	public void shouldDelegateLoginActionToDispatcher() {
		Dispatcher dispatcher = mock(Dispatcher.class);
		uploadChildRecordsController.setDispatcher(dispatcher);
		uploadChildRecordsController.login();
		verify(dispatcher).login();
	}


}
