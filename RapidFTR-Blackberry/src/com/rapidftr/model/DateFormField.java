package com.rapidftr.model;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.screens.ManageChildScreen;

import java.util.Date;

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
        if (dateField.getDate() == Long.MIN_VALUE) {
            return "";
        }
        return new SimpleDateFormat("d MMM yyyy").format(new Date(dateField.getDate()));
	}

    public void initializeLayout(ManageChildScreen newChildScreen) {
		
		manager=new VerticalFieldManager();
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
		dateField=new DateField(label, Long.MIN_VALUE, dateFormat, DateField.DATE);
        dateField.setDate(null);
	    manager.add(dateField);
	}

	public void setValue(String value) {
        dateField.setDate(Long.MIN_VALUE);
        if (value == null || value.length() == 0) {
            return;
        }
        long httpDate = HttpDateParser.parse(value);
        if (httpDate == 0) {
            return;
        }
        dateField.setDate(httpDate);
    }
}