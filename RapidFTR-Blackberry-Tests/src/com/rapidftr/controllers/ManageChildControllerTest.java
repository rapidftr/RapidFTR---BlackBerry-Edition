package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.ChildStoreService;

public class ManageChildControllerTest {

	private FormStore formStore;
	private UiStack uiStack;
	private ManageChildScreen newChildScreen;
	private ManageChildController newChildController;
	private Vector forms;
	private Dispatcher dispatcher;
	private ChildStoreService childStoreService;

	@Before
	public void setUp() {
		formStore = mock(FormStore.class);
		uiStack = mock(UiStack.class);
		forms = mock(Vector.class);
		childStoreService = mock(ChildStoreService.class);
		when(formStore.getForms()).thenReturn(forms);
		newChildScreen = mock(ManageChildScreen.class);
		newChildController = new ManageChildController(newChildScreen, uiStack,
				formStore,childStoreService);
		dispatcher = mock(Dispatcher.class);
		newChildController.setDispatcher(dispatcher);
	}
	
	@Test
	public void shouldDelegateSynchronizeFormActionToDispatcher()
	{
		newChildController.synchronizeForms();
		verify(dispatcher).synchronizeForms();
	}
	
	@Test
	public void shouldDelegateShowEditScreenForChildActionToChildScreen()
	{
		Child child = mock(Child.class);
		newChildController.showEditScreenForChild(child);
		verify(newChildScreen).setEditForms(formStore.getForms(),child);
	}
	
	
	@Test
	public void shouldSaveTheChildToTheRecordStore()
	{
		
	Child child = mock(Child.class);
		newChildController.saveChild(child);
	}


}
