package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controls.Button;
import com.rapidftr.model.SearchChildFilter;
import com.rapidftr.screens.internal.CustomScreen;

public class SearchChildScreen extends CustomScreen implements
		FieldChangeListener {
	private static final int MAX_SIZE = 200;

	private final BasicEditField searchTextField = new BasicEditField("", "",
			MAX_SIZE, USE_ALL_WIDTH | TextField.NO_NEWLINE);

	private Button searchButton;
	private Button resetButton;
	private Manager buttonManager;
	private SearchChildFilter searchChildFilter = new SearchChildFilter();
	// Use screen paramters instead if hardcore values
	private XYEdges border = new XYEdges(2, 2, 2, 2);

	public SearchChildScreen() {
		super();
		layoutScreen();
		searchTextField.setFocus();
	}

	private void layoutScreen() {

		add(new LabelField("Search Child"));
		add(new SeparatorField());
		add(new LabelField(""));
		searchTextField.setBorder(BorderFactory.createSimpleBorder(border));
		add(searchTextField);
		add(new LabelField(""));
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

		searchTextField.setText("");

	}

	private void onSearchButtonClicked() {
		searchChildFilter.setName(searchTextField.getText());
		if (!"".equals(searchTextField.getText())) {
			((ChildController) controller)
					.searchAndDispalyChildren(searchChildFilter);
		} else {
			Dialog.alert("Please enter either Name or Id");
		}

	}

	protected boolean onSavePrompt() {
		return true;
	}

}
