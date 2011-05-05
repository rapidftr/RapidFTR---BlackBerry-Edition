package com.rapidftr.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.me.JSONObject;
import org.junit.Test;

public class FormFieldTest {

	@Test
	public void getOptions() throws Exception {
		FormField field = new FormField(new JSONObject(
				"{option_strings:['one', 'two']}"));
		final boolean[] hasOption = {false};
		field.forEachOption(new OptionAction() {
			@Override
			public void execute(String option) {
				hasOption[0] = true;
				assertNotNull(option);
			}
		});
		assertTrue(hasOption[0]);
	}
	
	@Test
	public void verifyOptions() throws Exception {
		FormField field = new FormField(new JSONObject(
				"{option_strings:['one']}"));
		field.forEachOption(new OptionAction() {
			@Override
			public void execute(String option) {
				assertEquals("one", option);
			}
		});
	}
}
