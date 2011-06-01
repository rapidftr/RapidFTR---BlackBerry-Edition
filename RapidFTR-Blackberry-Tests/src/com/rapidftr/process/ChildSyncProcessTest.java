package com.rapidftr.process;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.model.Child;
import com.rapidftr.services.ChildSyncService;

public class ChildSyncProcessTest {

	@Mock
	private ChildSyncProcess childSyncProcess;
	@Mock
	private ChildSyncService service;
	@Mock
	private Child child;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		childSyncProcess = new ChildSyncProcess(service);
		childSyncProcess.setChild(child);

	}

	@Test
	public void name() throws Exception {
		assertEquals("Child Sync", childSyncProcess.name());
	}

	@Test
	public void isBackGround() throws Exception {
		assertTrue(childSyncProcess.isNotBackGround());
	}

	@Test
	public void startProcessShouldSyncChild() {
		childSyncProcess.startProcess();
		verify(service).syncChildRecord(child);
	}

	@Test
	public void stopProcessShouldStopSyncChild() {
		childSyncProcess.stopProcess();
		verify(service).cancelRequest();
	}

}
