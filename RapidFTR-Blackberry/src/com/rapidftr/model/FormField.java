package com.rapidftr.model;

import net.rim.device.api.ui.Manager;

import com.rapidftr.screens.NewChildScreen;

abstract public class FormField {

	protected String name;
	protected String type;
	
	

	protected FormField(String name, String type) {
		this.name = name;
		this.type = type;
	}

	abstract public void initializeLayout(NewChildScreen newChildScreen);

	abstract public Manager getLayout();

	public abstract Object getValue();
	
	

}
