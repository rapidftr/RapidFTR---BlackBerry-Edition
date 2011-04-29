package com.rapidftr.model;

import java.util.Vector;

import com.rapidftr.screens.ManageChildScreen;

public class Forms {

	private Vector forms;

	public Forms(Vector forms) {
		this.forms = forms;
	}

	public Forms() {
		forms = new Vector();
	}

	public void initializeLayout(ManageChildScreen screen, Child childToEdit) {
		for (int i = 0; i < forms.size(); i++) {
			Form form = (Form) forms.elementAt(i);
			form.initializeLayout(screen, childToEdit);
		}
	}

	public Form[] toArray() {
		final Form[] formArray = new Form[forms.size()];
		forms.copyInto(formArray);
		return formArray;
	}

	public boolean isEmpty() {
		for (int i = 0; i < forms.size(); i++) {
			Form form = (Form) forms.elementAt(i);
			if (!form.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isNotEmpty(){
		return !isEmpty();
	}
	
	public void forEachField(FieldAction action) {
		for (int i = 0; i < forms.size(); i++) {
			Form form = (Form) forms.elementAt(i);
			form.forEachField(action);
		}
	}

	public void forEachForm(FormAction formAction) {
		for (int i = 0; i < forms.size(); i++) {
			Form form = (Form) forms.elementAt(i);
			formAction.execute(form);
		}
	}
	
}
