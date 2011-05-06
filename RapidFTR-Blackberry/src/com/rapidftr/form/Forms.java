package com.rapidftr.form;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;

public class Forms {

	private Vector forms = new Vector();

	public Forms(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				forms.addElement(new Form(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
			}
		}
	}

	public void forEachForm(FormAction newFormAction) {
		for (int i = 0; i < forms.size(); i++) {
			Form form = (Form) forms.elementAt(i);
			if (form.isEnabled()) {
				newFormAction.execute(form);
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

	private boolean isEmpty() {
		if (forms.isEmpty()) {
			return true;
		}
		final boolean[] result = { true };
		forEachForm(new FormAction() {
			public void execute(com.rapidftr.form.Form form) {
				if (form.isNotEmpty()) {
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
