package com.rapidftr.datastore;

import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.model.FormFieldFactory;
import com.sun.me.web.path.ResultException;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormStoreTest {

	@SuppressWarnings("serial")
	@Test
	public void shouldReturnVectorOfForms() throws ResultException {

		FormStore formStore = new FormStore() {
			
			void initializePersistentStore() {
				persistentStore = mock(PersistentStore.class);
				String sucessfulJsonResponse = stubSuccessfulResponse();
				when(persistentStore.getContents()).thenReturn(
						sucessfulJsonResponse);
			}
		};

		final FormFieldFactory formFieldFactory = new FormFieldFactory();

		final Vector<FormField> fields = new Vector<FormField>(){{
			add(formFieldFactory.createFormField("age", "Age", "text_box", null));
			add(formFieldFactory.createFormField("age_is", "Age is", "select_box", new Vector<String>() {
				{
					add("Approximate");
					add("Exact");

				}
			}));
			
		}};

		Vector<Form> forms = new Vector<Form>(){{
			add(new Form("Basic_details", "basic_details", fields));			
		}};		


		assertEquals(formStore.getForms(), forms);

	}

	private String stubSuccessfulResponse() {
		String jsonFormString = String
				.format("[" +
							"{'name':'Basic_details'" +
							",'unique_id':'basic_details'" +
							",'fields':" +
									"[" +
										"{'name':'age'" +
										",'display_name':'Age'" +
										",'enabled':'true'" +
										",'type':'text_box'}" +
										",{'name':'age_is'" +
										",'display_name':'Age is'" +
										",'enabled':'true'" +
										",'type':'select_box'" +
										",'option_strings':" +
											"['Approximate','Exact']" +
											"}" +
										",{'name':'donotshow'" +
										",'display_name':'donotshow'" +
										",'enabled':'false'" +
										",'type':'text_box'" +
										"}"+											
									  "]" +
							   "}" +
						  "]");
		return jsonFormString;
	}
}
