package com.rapidftr.controls;

import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.form.FormField;

public abstract class CustomField extends VerticalFieldManager {
	
	protected final FormField field;

	public CustomField(FormField field){
		this.field = field;
	}
	
	public CustomField(FormField field,long style) {
		super(style);
		this.field = field;
	}

	public  void setValue(String value){
		
	}
	
	protected void setFieldValue(String value){
		field.setValue(value);
	}
}
