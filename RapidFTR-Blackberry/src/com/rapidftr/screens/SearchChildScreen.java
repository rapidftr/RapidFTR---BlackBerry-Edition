package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.controllers.SearchChildController;
import com.rapidftr.controls.Button;
import com.rapidftr.model.SearchChildFilter;

public class SearchChildScreen extends CustomScreen implements FieldChangeListener{
	private static final int MAX_SIZE = 200;
	
	private final BasicEditField nameField = new BasicEditField(
			"Name:", "", MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);
	private final BasicEditField idField = new BasicEditField(
			"id:", "", MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);
	
	private Button searchButton;
	private Button resetButton;
	private Manager buttonManager;
	private SearchChildFilter searchChildFilter = new SearchChildFilter();
	public SearchChildScreen()
	{
		super();
		layoutScreen();
		nameField.setFocus();
	}
	
	private void layoutScreen() {
		
		add(new LabelField("Search Child"));
		add(new SeparatorField());
		add(new LabelField(""));
		
		nameField.setPadding(PADDING);
		add(nameField);
		idField.setPadding(PADDING);
		add(idField);
		add(new SeparatorField());
		addButtons();
	}
	
	private void addButtons() {
		
		searchButton = new Button("Search");
		searchButton.setChangeListener(this);

		resetButton = new Button("Reset");
		resetButton.setChangeListener(this);
		
		buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.setPadding(PADDING);
		buttonManager.add(searchButton);
		buttonManager.add(resetButton);
		add(buttonManager);
	}

	public void cleanUp() {	
	}

	public void setUp() {			
	}

	public void fieldChanged(Field field, int context) {

		if (field.equals(searchButton)) {
			onSearchButtonClicked();

		}
		if (field.equals(resetButton)) {
			onResetButtonClicked();
		}
	}

	private void onResetButtonClicked() {
		
		nameField.setText("");
		idField.setText("");
		
	}

	private void onSearchButtonClicked() {	
		searchChildFilter.setName(nameField.getText());
		searchChildFilter.setId(idField.getText());
		((SearchChildController) controller).search(searchChildFilter);
		
	}
	

}
