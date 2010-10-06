package com.rapidftr.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ChildPhotoScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildStoreService;

public class ChildControllerTest {

	@Mock
	private FormStore formStore;
	@Mock
	private UiStack uiStack;
	@Mock
	private ManageChildScreen newChildScreen;
	@Mock
	private ViewChildScreen viewChildScreen;
	@Mock
	private SearchChildScreen searchChildScreen;
	@Mock
	private ViewChildrenScreen viewChildrenScreen;

	@Mock
	private ChildPhotoScreen childPhotoScreen;

	private ChildController childController;
	@Mock
	private Vector forms;
	@Mock
	private Dispatcher dispatcher;
	@Mock
	private ChildStoreService childStoreService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(formStore.getForms()).thenReturn(forms);
		childController = new ChildController(newChildScreen, viewChildScreen,
				searchChildScreen, viewChildrenScreen, uiStack, formStore,
				childStoreService, childPhotoScreen);
		childController.setDispatcher(dispatcher);
	}

	@Test
	public void shouldDelegateSynchronizeFormActionToDispatcher() {
		childController.synchronizeForms();
		verify(dispatcher).synchronizeForms();
	}

	@Test
	public void shouldDelegateShowEditScreenForChildActionToChildScreen() {
		Child child = mock(Child.class);
		childController.editChild(child);
		verify(newChildScreen).setEditForms(formStore.getForms(), child);
	}

	@Test
	public void shouldSaveTheChildToTheRecordStore() {
		Child child = mock(Child.class);
		childController.saveChild(child);
	}

}
