package com.rapidftr.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormFieldFactoryTest {

	@Test
	public void shouldReturnTextboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "text_field", null);
		assertNotNull(field);
		assertTrue(field instanceof TextboxFormField);
	}
	
	@Test
	public void shouldReturnSelecttboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "select_box", null);
		assertNotNull(field);
		assertTrue(field instanceof SelectboxFormField);
	}
	
	@Test
	public void shouldReturnRadiobuttonFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "radio_button", null);
		assertNotNull(field);
		assertTrue(field instanceof RadioButtonFormField);
	}
	
	
	@Test
	public void shouldReturnCheckboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "check_box", null);
		assertNotNull(field);
		assertTrue(field instanceof CheckboxFormField);
	}
	
	@Test
	public void shouldReturnPhotoUploadFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "photo_upload_box", null);
		assertNotNull(field);
		assertTrue(field instanceof PhotoUploadFormField);
	}
	
	@Test
	public void shouldReturnNumericTextboxFormField()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "numeric_text_field", null);
		assertNotNull(field);
		assertTrue(field instanceof NumericTextboxFormField);
	}
	
	@Test
	public void shouldReturnNullWhenInvalidInputTypeIsGiven()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field = formFieldFactory.createFormField("name", "xxxxx", null);
		assertNull(field);
	}
	
}
