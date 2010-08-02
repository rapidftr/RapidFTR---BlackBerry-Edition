package com.rapidftr.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Ignore;
import org.junit.Test;

public class RadioButtonFormFieldTests {

	@Test
	public void shouldTestForEquals() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		Vector optionStrings1 = new Vector();
		optionStrings1.add("male");
		optionStrings1.add("female");
		
		RadioButtonFormField field1 =  (RadioButtonFormField) formFieldFactory
				.createFormField("name1", "radio_button",optionStrings1);
		
		Vector optionStrings2 = new Vector();
		optionStrings2.add("male");
		optionStrings2.add("female");
		
		RadioButtonFormField field2 = (RadioButtonFormField) formFieldFactory.createFormField("name1",
				"radio_button",optionStrings2 );
		assertEquals(field1,field2);
	}

	
}
