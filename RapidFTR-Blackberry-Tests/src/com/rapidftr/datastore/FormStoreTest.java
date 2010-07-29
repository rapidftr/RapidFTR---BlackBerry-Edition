package com.rapidftr.datastore;

import static org.junit.Assert.*;

import java.util.Vector;

import net.rim.device.api.system.ControlledAccessException;

import org.junit.Test;

import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.model.FormFieldFactory;
import com.rapidftr.model.TextBoxField;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FormStoreTest {

	@Test
	public void shouldReturnVectorOfForms() throws ResultException {

		FormStore formStore = new FormStore() {
			@Override
			void initializePersistentStore() {
				persistentStore = mock(PersistentStore.class);
				String sucessfulJsonResponse = stubSuccessfulResponse();
				when(persistentStore.getContents()).thenReturn(
						sucessfulJsonResponse);
			}
		};

		Vector forms = new Vector();

		Vector fieldList = new Vector();

		FormFieldFactory formFieldFactory = new FormFieldFactory();
		FormField textFormField = formFieldFactory .createFormField("age","text_box",null);
		fieldList.add(textFormField);

		Vector optionString = new Vector();
		optionString.add("Approximate");
		optionString.add("Exact");

		FormField selectBoxFormField = formFieldFactory.createFormField("age_is", "select_box",
				optionString);
		fieldList.add(selectBoxFormField);

		Form form = new Form("Basic_details", "basic_details", fieldList);

		forms.add(form);

		assertEquals(formStore.getForms(), forms);

	}
	
	

	private String stubSuccessfulResponse() {
		String jsonFormString = String
				.format("[{\"name\":\"Basic_details\",\"unique_id\":\"basic_details\",\"fields\":[{\"name\":\"age\",\"type\":\"text_box\"},{\"name\":\"age_is\",\"type\":\"select_box\",\"option_strings\":"
						+

						"[\"Approximate\",\"Exact\"]}]}]");
		return jsonFormString;
	}
}
