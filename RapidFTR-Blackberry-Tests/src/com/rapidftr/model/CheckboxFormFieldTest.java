package com.rapidftr.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CheckboxFormFieldTest {

	@Test
	public void shouldCheckForEquals() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		CheckboxFormField checkboxFormField1 = (CheckboxFormField) formFieldFactory.createFormField("is_disabled", "Is disabled?", "check_box", null);
		CheckboxFormField checkboxFormField2 = (CheckboxFormField) formFieldFactory.createFormField("is_disabled", "Is disabled?", "check_box", null);
		assertEquals(checkboxFormField1,checkboxFormField2);
	}

}
