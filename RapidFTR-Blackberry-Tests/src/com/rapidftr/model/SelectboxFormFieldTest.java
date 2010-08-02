package com.rapidftr.model;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Ignore;
import org.junit.Test;

public class SelectboxFormFieldTest {

	@Test
	public void shouldCheckForEqulas() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		Vector optionStrings1 = new Vector();
		optionStrings1.add("option1");
		optionStrings1.add("option2");

		FormField field1 = formFieldFactory.createFormField("name1",
				"select_box", optionStrings1);

		Vector optionStrings2 = new Vector();
		optionStrings2.add("option1");
		optionStrings2.add("option2");

		FormField field2 = formFieldFactory.createFormField("name1",
				"select_box", optionStrings2);

		assertEquals(field1, field2);

	}
}
