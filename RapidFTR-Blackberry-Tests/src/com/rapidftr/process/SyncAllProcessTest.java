package com.rapidftr.process;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.model.Child;
import com.rapidftr.services.ChildSyncService;

public class SyncAllProcessTest {

	@Mock
	private SyncAllProcess syncAllProcess;
	@Mock
	private ChildSyncService service;
	

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		syncAllProcess = new SyncAllProcess(service);

	}

	@Test
	public void startProcessShouldSyncAll() {
		syncAllProcess.startProcess();
		verify(service).syncAllChildRecords();
	}

	@Test
	public void stopProcessShouldStopSyncAll() {
		syncAllProcess.stopProcess();
		verify(service).cancelRequest();
	}

}
