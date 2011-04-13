package com.rapidftr.model;

import net.rim.device.api.ui.text.NumericTextFilter;
import net.rim.device.api.i18n.ResourceBundle;

import com.rapidftr.screens.ManageChildScreen;
//import com.rapidftr.model.NumericTextboxResource;


public class NumericTextboxFormField extends TextboxFormField  {
	
    public static String AGELABELSTRING = ModelLabelText.getAgeLabelString();
	
	
	public NumericTextboxFormField(String name, String displayName, String type) {
		super(name, displayName, type);
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		super.initializeLayout(newChildScreen);

		field.setFilter(new NumericTextFilter());
		
		if (AGELABELSTRING.equals(name)){
			field.setMaxSize(2);
		}
	}
}
