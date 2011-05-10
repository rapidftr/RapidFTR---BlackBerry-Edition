package com.rapidftr.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormFieldFactoryTest {

	@Test
	public void shouldReturnTextboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "text_field", null, "");
		assertNotNull(field);
		assertTrue(field instanceof TextboxFormField);
	}
	
	@Test
	public void shouldReturnSelecttboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "select_box", null, "");
		assertNotNull(field);
		assertTrue(field instanceof SelectboxFormField);
	}
	
	@Test
	public void shouldReturnRadiobuttonFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "radio_button", null, "");
		assertNotNull(field);
		assertTrue(field instanceof RadioButtonFormField);
	}
	
	
	@Test
	public void shouldReturnCheckboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "check_box", null, "");
		assertNotNull(field);
		assertTrue(field instanceof CheckboxFormField);
	}
	
	@Test
	public void shouldReturnPhotoUploadFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "photo_upload_box", null, "");
		assertNotNull(field);
		assertTrue(field instanceof PhotoUploadFormField);
	}
	
	@Test
	public void shouldReturnNumericTextboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "numeric_field", null, "");
		assertNotNull(field);
		assertTrue(field instanceof NumericTextboxFormField);
	}
	
	@Test
	public void shouldReturnNullWhenInvalidInputTypeIsGiven()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "Name", "xxxxx", null, "");
		assertNull(field);
	}
	
}
