package com.rapidftr.model;


import org.junit.Test;
import com.rapidftr.form.FormField;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FormFieldFactoryTest {

	@Test
	public void shouldReturnTextboxFormField() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNotNull(field);
		assertTrue(field instanceof TextboxFormField);
	}

	@Test
	public void shouldReturnSelecttboxFormField() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNotNull(field);
		assertTrue(field instanceof SelectboxFormField);
	}

	@Test
	public void shouldReturnRadiobuttonFormField() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNotNull(field);
		assertTrue(field instanceof RadioButtonFormField);
	}

	@Test
	public void shouldReturnCheckboxFormField() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNotNull(field);
		assertTrue(field instanceof CheckboxFormField);
	}

	@Test
	public void shouldReturnPhotoUploadFormField() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNotNull(field);
		assertTrue(field instanceof PhotoUploadFormField);
	}

	@Test
	public void shouldReturnNumericTextboxFormField() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNotNull(field);
		assertTrue(field instanceof NumericTextboxFormField);
	}

	@Test
	public void shouldReturnNullWhenInvalidInputTypeIsGiven() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		net.rim.device.api.ui.Field field = formFieldFactory.createFrom(mock(FormField.class));
		assertNull(field);
	}

}
