package com.rapidftr.process;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.services.FormService;

public class FormSyncProcessTest {

	@Mock
	private FormSyncProcess formSyncProcess;
	@Mock
	private FormService service;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		formSyncProcess = new FormSyncProcess(service);
	}

	@Test
	public void startProcessShouldSyncChild() {
		formSyncProcess.startProcess();
		verify(service).downloadForms();
	}

	@Test
	public void stopProcessShouldStopSyncChild() {
		formSyncProcess.stopProcess();
		verify(service).cancelRequest();
	}

}
