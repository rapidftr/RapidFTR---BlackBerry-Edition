package com.rapidftr.model;

import net.rim.device.api.i18n.ResourceBundle;

public class ModelLabelText implements ModelLabelResource{
	private static ResourceBundle resB = ResourceBundle.getBundle(ModelLabelResource.BUNDLE_ID, ModelLabelResource.BUNDLE_NAME);
	
	private static final String AGELABELSTRING = resB.getString(ModelLabelResource.I18NAGEFIELDLABEL);
	
	public static String getAgeLabelString () {
		return AGELABELSTRING;
	}


}
