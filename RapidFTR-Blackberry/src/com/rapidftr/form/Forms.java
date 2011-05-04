package com.rapidftr.form;

import org.json.me.JSONArray;
import org.json.me.JSONException;

public class Forms {

	private final JSONArray jsonArray;

	public Forms(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public void forEachForm(FormAction newFormAction) {
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				newFormAction.execute(new Form(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
			}
		}
	}

	public void forEachField(final FormFieldAction fieldAction) {
		forEachForm(new FormAction() {
			public void execute(Form form) {
				form.forEachField(fieldAction);
			}
		});
	}

	public Form[] toArray() {
		final Form[] formArray = new Form[jsonArray.length()];
		forEachForm(new FormAction() {
			int i = 0;

			public void execute(Form form) {
				formArray[i++] = form;
			}
		});
		return formArray;
	}

	public boolean isEmpty() {
		if (jsonArray.length() == 0) {
			return true;
		}
		final boolean[] result = { true };
		forEachForm(new FormAction() {
			public void execute(com.rapidftr.form.Form form) {
				if (!form.isEmpty()) {
					result[0] = false;
				}
			}
		});
		return result[0];
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}
}
