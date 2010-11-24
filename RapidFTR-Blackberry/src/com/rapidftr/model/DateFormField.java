package com.rapidftr.model;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.screens.ManageChildScreen;

public class DateFormField extends FormField
{
	  VerticalFieldManager manager ;
	  String label;
	  DateField dateField;
	
	protected DateFormField(String name, String displayName, String type) 
	{
		super(name, displayName, type);
		label= displayName;
	}

	public Manager getLayout() {
		return manager;
	}

	public String getValue() {
		return dateField.toString();
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		
		manager=new VerticalFieldManager();
		dateField=new DateField(label, System.currentTimeMillis(), DateField.DATE);
        dateField.setDate(null);
	    manager.add(dateField);
	}

	public void setValue(String value) {
		
		try
		{
			long dateValue = Long.parseLong(value);
			dateField.setDate(dateValue);
		}
		catch (NumberFormatException nfe)
		{
			dateField.setDate(System.currentTimeMillis());
		}
		
	}
}