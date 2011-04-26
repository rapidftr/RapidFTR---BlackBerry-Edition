package com.rapidftr.model;

import net.rim.device.api.ui.Manager;

import com.rapidftr.screens.ManageChildScreen;

abstract public class FormField {

	protected String name;
    protected String displayName;
	protected String type;
    protected String helpText;

    protected FormField(String name, String displayName, String type, String helpText) {
		this.name = name;
		this.displayName = displayName;
		this.type = type;
        this.helpText = helpText;
	}

	abstract public void initializeLayout(ManageChildScreen newChildScreen);

	abstract public Manager getLayout();

	public abstract String getValue();

	public abstract void setValue(String value);
	
	public String getName() {
		return name;
	}

    public String displayLabel() {
        if (displayName != null && !displayName.equals("")) {
            return displayName;
        }
        return name;
    }

    public boolean isEmpty() {
        return true;
    }

    public String getHelpText() {
        return helpText;
    }
}
