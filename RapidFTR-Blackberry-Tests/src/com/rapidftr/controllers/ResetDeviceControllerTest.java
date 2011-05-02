package com.rapidftr.controllers;

import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.LoginService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResetDeviceControllerTest {

    private FormService formService;
    private ChildSyncService childSyncService;
    private LoginService loginService;
    private ResetDeviceController resetDeviceController;

    @Before
    public void setup() {
        formService = mock(FormService.class);
        childSyncService = mock(ChildSyncService.class);
        loginService = mock(LoginService.class);
        resetDeviceController = new ResetDeviceController(formService, childSyncService, loginService);
    }

    @Test
    public void shouldClearFormStateWhenResettingDevice() {
        resetDeviceController.resetDevice();
        verify(formService).clearState();
    }

    @Test
    public void shouldClearLoginStateWhenResettingDevice() {
        resetDeviceController.resetDevice();
        verify(loginService).clearLoginState();
    }

    @Test
    public void shouldClearChildSynchronizationStateWhenResettingDevice() {
        resetDeviceController.resetDevice();
        verify(childSyncService).clearState();
    }
}
