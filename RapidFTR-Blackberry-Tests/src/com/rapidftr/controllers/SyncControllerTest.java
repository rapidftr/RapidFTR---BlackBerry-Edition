//package com.rapidftr.controllers;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//
//import org.json.me.JSONException;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//
//import com.rapidftr.screens.SyncScreen;
//import com.rapidftr.screens.internal.UiStack;
//import com.rapidftr.services.ChildSyncService;
//import com.rapidftr.services.FormService;
//import com.sun.me.web.path.Result;
//import com.sun.me.web.request.Response;
//
//public class SyncControllerTest {
//
//	@Mock
//	private FormService formService;
//	@Mock
//	private ChildSyncService childSyncService;
//	@Mock
//	private UiStack uiStack;
//	@Mock
//	private SyncScreen syncScreen;
//	@Mock
//	private Response response;
//	@Mock
//	private Result result;
//
//	private SyncController syncController;
//
//	@Before
//	public void setUp() {
//		syncController = new SyncController(syncScreen, uiStack,
//				childSyncService, formService);
//		when(response.getResult()).thenReturn(result);
//		when(result.toString()).thenReturn("json");
//	}
//
//	@Test
//	public void shouldDownloadFormsFromFormService() throws IOException {
//		synchronizeFormsController.synchronizeForms();
//		verify(formService).downloadForms();
//	}
//
//	@Test
//	public void shouldCancelDownloadingOfForms() {
//		synchronizeFormsController.stopSynchronizingForms();
//		verify(formService).cancelRequest();
//	}
//
//	@Test
//	public void shouldShowSynchronizeFormScreen() {
//		synchronizeFormsController.synchronizeForms();
//		verify(uiStack).pushScreen(synchronizeFormsScreen);
//	}
//
//	@Test
//	public void shouldUpdateProgressBarValueOnScreen() {
//		synchronizeFormsController.synchronizeForms();
//		synchronizeFormsController.getScreenCallBack()
//				.updateRequestProgress(10);
//		verify(synchronizeFormsScreen).updateRequestProgress(10);
//	}
//
//	@Test
//	public void shouldSendDownloadFailedErrorMessageToScreen() {
//		synchronizeFormsController.synchronizeForms();
//		synchronizeFormsController.getScreenCallBack()
//				.handleConnectionProblem();
//		verify(synchronizeFormsScreen).handleConnectionProblem();
//	}
//
//	@Test
//	public void shouldUploadChildRecords() {
//		uploadChildRecordsController.syncAllChildRecords();
//		verify(childSyncService).syncAllChildRecords();
//	}
//
//	@Test
//	public void shouldCancelUploadChildRecords() {
//		uploadChildRecordsController.cancelUpload();
//		verify(childSyncService).cancelRequest();
//	}
//
//	@Test
//	public void shouldUpdateStatusOfUploadOperation() {
//		uploadChildRecordsController.syncAllChildRecords();
//		uploadChildRecordsController.getScreenCallBack().updateRequestProgress(
//				10);
//		verify(uploadChildRecordsScreen).updateRequestProgress(10);
//	}
//
//	@Test
//	public void shouldDelegateLoginActionToDispatcher() {
//		Dispatcher dispatcher = mock(Dispatcher.class);
//		uploadChildRecordsController.setDispatcher(dispatcher);
//		uploadChildRecordsController.login();
//		verify(dispatcher).login();
//	}
//
//}
