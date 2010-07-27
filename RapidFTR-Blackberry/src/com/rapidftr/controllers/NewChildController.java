package com.rapidftr.controllers;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.screens.NewChildScreen;
import com.rapidftr.screens.Screen;
import com.rapidftr.screens.UiStack;

public class NewChildController extends Controller{

	private final FormStore formStore;

	public NewChildController(NewChildScreen screen, UiStack uiStack, FormStore formStore) {
		super(screen, uiStack);
		this.formStore = formStore;
	
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();
	}
	
	public void show()
	{
		((NewChildScreen) screen).setForms(formStore.getForms());
		super.show();
	}

    
}
