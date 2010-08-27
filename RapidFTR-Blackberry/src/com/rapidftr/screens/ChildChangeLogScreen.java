package com.rapidftr.screens;

import java.util.Enumeration;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.LabelField;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.rapidftr.model.Child;

public class ChildChangeLogScreen extends CustomScreen {
	
	JSONArray histories;
	
	public ChildChangeLogScreen(Child child)
	{
		try 
		{
			histories = new JSONArray(child.getField("histories").toString());
		}
		catch (JSONException e) {
			Dialog.alert("unable to parse child history");
		}	
		if(histories==null)
		{
			histories = new JSONArray();
		}
		
		layoutScreen();		
	}

	private void layoutScreen() {
		addLogo();
		add(new SeparatorField());
		for(int i =0 ; i < histories.length();i++)
		{
			try {
				JSONObject history = histories.getJSONObject(i);
				Enumeration keys =history.keys();
				while (keys.hasMoreElements())
				{
					add(new LabelField(history.get((String) keys.nextElement())));
				}
				
			} catch (JSONException e) {
				Dialog.alert("Unable to parse child history");
				break;
			}
			
		}
		
	}

	public void cleanUp() {

	}

	public void setUp() {

	}

}
