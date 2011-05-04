package com.rapidftr.model;

import org.junit.Test;

import com.rapidftr.form.FormField;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RadioButtonFormFieldTests {

	@Test
	public void shouldTestForEquals() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		Vector optionStrings1 = new Vector();
		optionStrings1.add("male");
		optionStrings1.add("female");
		
		RadioButtonFormField field1 =  (RadioButtonFormField) formFieldFactory
				.createFrom(mock(FormField.class));
		
		Vector optionStrings2 = new Vector();
		optionStrings2.add("male");
		optionStrings2.add("female");
		
		RadioButtonFormField field2 = (RadioButtonFormField) formFieldFactory.createFrom(mock(FormField.class));
		assertEquals(field1,field2);
	}

	
}
