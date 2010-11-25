package com.rapidftr.model;

import net.rim.device.api.io.http.HttpDateParser;
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

        String formattedDateStr = value + " 00:00:00 GMT";
        long httpDate = HttpDateParser.parse(formattedDateStr);
        if (httpDate == -1) {
            dateField.setDate(null);
            return;
        }
        dateField.setDate(httpDate);
    }
}