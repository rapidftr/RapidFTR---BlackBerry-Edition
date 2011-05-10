package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.ContactInformationController;
import com.rapidftr.screens.ContactInformationScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ContactInformationSyncService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContactInformationControllerTest {

    private ContactInformationScreen screen;
    private UiStack uiStack;
    private ContactInformationSyncService service;
    private ContactInformationController controller;
    private Dispatcher dispatcher;

    @Before
    public void setup() {
        screen = mock(ContactInformationScreen.class);
        uiStack = mock(UiStack.class);
        service = mock(ContactInformationSyncService.class);
        dispatcher = mock(Dispatcher.class);
        controller = new ContactInformationController(screen, uiStack, service, dispatcher);
    }

    @Test
    public void shouldDownloadContactInformation() {
        controller.fetchContactInformation();
        verify(service).downloadContactInformation();
    }
}
