package com.rapidftr.process;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;

public class SyncAllProcessTest {

	@Mock
	private SyncAllProcess syncAllProcess;
	@Mock
	private ChildSyncService childService;
    @Mock
    private FormService formService;


    @Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		syncAllProcess = new SyncAllProcess(childService, formService);
	}

	@Test
	public void startProcessShouldSyncAll() {
		syncAllProcess.startProcess();
		verify(childService).syncAllChildRecords();
	}

	@Test
	public void stopProcessShouldStopSyncAll() {
		syncAllProcess.stopProcess();
		verify(childService).cancelRequest();
	}

}
