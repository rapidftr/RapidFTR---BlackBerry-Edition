package com.rapidftr.controls;

import java.util.Vector;

import com.rapidftr.form.Form;
import com.rapidftr.form.FormAction;
import com.rapidftr.form.Forms;
import com.rapidftr.model.Child;

public class UIForms {

	private final Forms forms;
	private Vector uiForms = new Vector();

	public UIForms(Forms forms, final FormFieldFactory formFieldFactory,
			final Child child) {
		this.forms = forms;
		forms.forEachForm(new FormAction() {
			public void execute(Form form) {
				uiForms.addElement(new UIForm(form, formFieldFactory, child));
			}
		});
	}

	public UIForm getDefaultForm() {
		return formAt(0);
	}

	public Object[] getFormNames() {
		final Vector names = new Vector();
		forms.forEachForm(new FormAction() {

			public void execute(Form form) {
				names.addElement(form.getName());
			}
		});
		String[] array = new String[names.size()];
		names.copyInto(array);
		return array;
	}

	public UIForm formAt(int selectedIndex) {
		return (UIForm) uiForms.elementAt(selectedIndex);
	}

	public int getIndexByName(String selectedTab) {
		int selectedIndex = 0;
		for (int i = 0; i < uiForms.size(); i++) {
			UIForm uiForm = (UIForm) uiForms.elementAt(i);
			if (uiForm.getName().equals(selectedTab)) {
				selectedIndex = i;
				break;
			}
		}
		return selectedIndex;
	}
}
