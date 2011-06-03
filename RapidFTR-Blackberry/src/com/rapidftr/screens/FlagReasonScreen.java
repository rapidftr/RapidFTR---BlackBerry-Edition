package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controllers.FlagRecordController;
import com.rapidftr.controls.Button;
import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;


public class FlagReasonScreen extends CustomScreen{
	
	private final BasicEditField flagReasonField = new BasicEditField(
			"Enter reason:", "");
	private Button flagButton;
	private Child child;
	protected UiStack uiStack;
	
	public FlagReasonScreen() {
	}
	
	public void setUp() {
		
		clearFields();
		renderTitle();
		renderFlagReasonField();
		renderFlagButton();
	}
	
	public void setChild(Child child) {
		this.child = child;
	}
	
	private void renderFlagButton() {
		flagButton = new Button("Flag");
		flagButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				child.flagRecord(flagReasonField.getText());
				((FlagRecordController)controller).popScreen();
			}
		});
		HorizontalFieldManager buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.add(flagButton);
		this.add(buttonManager);
	}
	
	private void renderFlagReasonField() {
		flagReasonField.setPadding(PADDING);
		add(flagReasonField);
		flagReasonField.setFocus();
	}
	
	private void renderTitle() {
		HorizontalFieldManager titleManager = new HorizontalFieldManager(FIELD_HCENTER);
		titleManager.setPadding(new XYEdges(2, 2, 2, 0));
		titleManager.add(new LabelField("Flag Reason"));
		this.add(titleManager);
		this.add(new SeparatorField());
	}
	
}