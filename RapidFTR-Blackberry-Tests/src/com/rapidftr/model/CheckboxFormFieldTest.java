package com.rapidftr.model;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class CheckboxFormFieldTest {

	@Test
	public void shouldCheckForEquals() {
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		CheckboxFormField checkboxFormField1 = (CheckboxFormField) formFieldFactory.createFormField("is_disabled", "check_box", null);
		CheckboxFormField checkboxFormField2 = (CheckboxFormField) formFieldFactory.createFormField("is_disabled", "check_box", null);
		assertEquals(checkboxFormField1,checkboxFormField2);
	}

}
