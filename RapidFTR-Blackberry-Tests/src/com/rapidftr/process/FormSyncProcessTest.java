package com.rapidftr.process;

import static org.mockito.Mockito.verify;
import junit.framework.Assert;

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

	public void name() throws Exception {
		Assert.assertEquals("Form Sync", formSyncProcess.name());
	}

	@Test
	public void isBackGround() throws Exception {
		Assert.assertFalse(formSyncProcess.isNotBackGround());
	}
	
	@Test
	public void stopProcessShouldStopSyncChild() {
		formSyncProcess.stopProcess();
		verify(service).cancelRequest();
	}

}
