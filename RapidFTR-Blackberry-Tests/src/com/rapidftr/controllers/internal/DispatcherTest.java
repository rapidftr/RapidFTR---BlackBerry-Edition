package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.*;
import com.rapidftr.datastore.Children;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.process.Process;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

public class DispatcherTest {

    @Test
    public void shouldShowHomeScreen() {
        HomeController homeScreenController = mock(HomeController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.homeScreenControllerWith(anyDispatcher())).thenReturn(homeScreenController);
        new Dispatcher(controllerFactory).homeScreen();
        verify(homeScreenController).show();
    }

    @Test
    public void shouldShowViewAllChildrenScreen() {
        ViewChildrenController viewChildrenController = mock(ViewChildrenController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.viewChildrenControllerWith(anyDispatcher())).thenReturn(viewChildrenController);
        new Dispatcher(controllerFactory).viewChildren();
        verify(viewChildrenController).viewAllChildren();
    }

    @Test
    public void shouldShowNewChildScreen() {
        ManageChildController manageChildController = mock(ManageChildController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.manageChildControllerWith(anyDispatcher())).thenReturn(manageChildController);
        new Dispatcher(controllerFactory).newChild();
        verify(manageChildController).newChild();
    }

    @Test
    public void shouldSynchronizeForms() {
        SyncController syncController = mock(SyncController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.syncControllerWith(anyDispatcher())).thenReturn(syncController);
        new Dispatcher(controllerFactory).synchronizeForms();
        verify(syncController).synchronizeForms();
    }

    @Test
    public void shouldSynchronizeAllProcesses() {
        SyncController syncController = mock(SyncController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.syncControllerWith(anyDispatcher())).thenReturn(syncController);
        new Dispatcher(controllerFactory).synchronize();
        verify(syncController).synchronize();
    }

    @Test
    public void shouldSynchronizeChildRecord() {
        SyncController syncController = mock(SyncController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Child child = mock(Child.class);

        when(controllerFactory.syncControllerWith(anyDispatcher())).thenReturn(syncController);
        new Dispatcher(controllerFactory).syncChild(child);
        verify(syncController).syncChildRecord(child);
    }

    @Test
    public void shouldShowSearchChildScreen() {
        SearchChildController searchChildController = mock(SearchChildController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.searchChildControllerWith(anyDispatcher())).thenReturn(searchChildController);
        new Dispatcher(controllerFactory).searchChild();
        verify(searchChildController).show();
    }

    @Test
    public void shouldResetDevice() {
        ResetDeviceController resetDeviceController = mock(ResetDeviceController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.resetDeviceController()).thenReturn(resetDeviceController);
        new Dispatcher(controllerFactory).resetDevice();
        verify(resetDeviceController).resetDevice();
    }

    @Test
    public void shouldShowLoginScreen() {
        LoginController loginController = mock(LoginController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Process process = mock(Process.class);

        when(controllerFactory.loginControllerWith(anyDispatcher())).thenReturn(loginController);

        new Dispatcher(controllerFactory).login(process);
        verify(loginController).showLoginScreen(process);
    }

    @Test
    public void shouldShowContactInformationScreen() {
        ContactInformationController contactScreenController = mock(ContactInformationController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);

        when(controllerFactory.contactScreenControllerWith(anyDispatcher())).thenReturn(contactScreenController);

        new Dispatcher(controllerFactory).showcontact();
        verify(contactScreenController).show();
    }

    @Test
    public void shouldShowEditChildScreenForGivenChild() {
        ManageChildController manageChildController = mock(ManageChildController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Child child = mock(Child.class);

        when(controllerFactory.manageChildControllerWith(anyDispatcher())).thenReturn(manageChildController);
        new Dispatcher(controllerFactory).editChild(child,"tab");
        verify(manageChildController).editChild(child, "tab");
    }

    @Test
    public void shouldShowChildHistoryScreenForGivenChild() {
        ChildHistoryController childHistoryController = mock(ChildHistoryController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Child child = mock(Child.class);

        when(controllerFactory.childHistoryControllerWith(anyDispatcher())).thenReturn(childHistoryController);
        new Dispatcher(controllerFactory).showHistory(child);
        verify(childHistoryController).showHistory(child);
    }

    @Test
    public void shouldShowViewChildScreenForGivenChild() {
        ViewChildController viewChildController = mock(ViewChildController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Child child = mock(Child.class);

        when(controllerFactory.viewChildControllerWith(anyDispatcher())).thenReturn(viewChildController);
        new Dispatcher(controllerFactory).viewChild(child);
        verify(viewChildController).viewChild(child);
    }

    @Test
    public void shouldShowViewChildrenScreenForGivenChildren() {
        ViewChildrenController viewChildrenController = mock(ViewChildrenController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Children children = mock(Children.class);

        when(controllerFactory.viewChildrenControllerWith(anyDispatcher())).thenReturn(viewChildrenController);
        new Dispatcher(controllerFactory).viewChildren(children);
        verify(viewChildrenController).viewChildren(children);
    }

    @Test
    public void shouldShowChildPhotoScreenForGivenChild() {
        ViewChildPhotoController viewChildPhotoController = mock(ViewChildPhotoController.class);
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        Child child = mock(Child.class);

        when(controllerFactory.viewChildPhotoControllerWith(anyDispatcher())).thenReturn(viewChildPhotoController);
        new Dispatcher(controllerFactory).viewChildPhoto(child);
        verify(viewChildPhotoController).viewChildPhoto(child);
    }

    @Test
    public void shouldShowFlagInformationScreenOnFlagRecord() {
        ControllerFactory controllerFactory = mock(ControllerFactory.class);
        FlagChildController flagController = mock(FlagChildController.class);
        when(controllerFactory.flagChildControllerWith(anyDispatcher())).thenReturn(flagController);

        Child child = ChildFactory.newChild();
        new Dispatcher(controllerFactory).flagRecord(child);
        verify(flagController).flagRecord(child);
    }

    private Dispatcher anyDispatcher() {
        return Matchers.any(Dispatcher.class);
    }
}
