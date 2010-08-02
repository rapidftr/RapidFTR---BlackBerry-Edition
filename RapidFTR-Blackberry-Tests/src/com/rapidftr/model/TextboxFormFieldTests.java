package com.rapidftr.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextboxFormFieldTests {

	@Test
	public void shouldTestForEquals() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField field1 = (TextboxFormField) formFieldFactory
				.createFormField("name1", "text_field", null);
		FormField field2 = formFieldFactory.createFormField("name1",
				"text_field", null);
		assertTrue(field1.equals(field2));
	}
	

}
