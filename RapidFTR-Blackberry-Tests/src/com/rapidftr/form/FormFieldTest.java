package com.rapidftr.form;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

public class FormFieldTest {

	@Test
	public void getOptions() throws Exception {
		FormField field = new FormField(new JSONObject(
				"{option_strings:['one', 'two']}"));
		final boolean[] hasOption = { false };
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
	public void getOptionsArray() throws Exception {
		FormField field = new FormField(new JSONObject(
				"{option_strings:['one', 'two']}"));
		String[] options = field.getOptionsArray();
		assertEquals("one", options[0]);
		assertEquals("two", options[1]);
	}

	@Test
	public void ignoreInvalidOptionStrings() throws Exception {
		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		Mockito.when(jsonObject.getString("option_strings")).thenThrow(
				new JSONException(""));
		FormField formField = new FormField(jsonObject);
		formField.forEachOption(new OptionAction() {

			@Override
			public void execute(String option) {
				fail("Should ignore invalid options");
			}
		});
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
