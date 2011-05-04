package com.rapidftr.screens;

import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.form.Form;
import com.rapidftr.form.FormFieldAction;
import com.rapidftr.model.FormFieldFactory;

public class UIForm extends VerticalFieldManager {

	private final Form form;
	private final FormFieldFactory factory;

	public UIForm(Form form, FormFieldFactory factory) {
		this.form = form;
		this.factory = factory;
	}
	
	protected void onDisplay() {
		form.forEachField(new FormFieldAction() {
			public void execute(com.rapidftr.form.FormField field) {
				add(factory.createFrom(field));
				add(new BlankSeparatorField(10));
			}
		});
	}

}
