package com.rapidftr.controls;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.form.Form;
import com.rapidftr.form.FormField;
import com.rapidftr.form.FormFieldAction;
import com.rapidftr.model.Child;

public class UIForm extends VerticalFieldManager {

	private final Form form;
	private final FormFieldFactory factory;
	private Child child;
	private boolean rendered = false;

	public UIForm(Form form, FormFieldFactory formFieldFactory, Child child) {
		this.form = form;
		this.factory = formFieldFactory;
		this.child = child;
	}

	protected void onDisplay() {
		if(!rendered){
			form.forEachField(new FormFieldAction() {
				public void execute(FormField field) {
					Field createField = createField(field);
					add(createField);
					add(new BlankSeparatorField(10));
				}

			});
			rendered = true;
		}
	}

	private Field createField(FormField field) {
		CustomField formField = factory.createFrom(field);
		if (null != this.child)
			formField.setValue(child.getField(field.getName()));
		return formField;
	}

	public String getName() {
		return form.getName();
	}
}
