package com.rapidftr.screens;

import java.util.Enumeration;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.LabelField;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import net.rim.device.api.ui.component.RichTextField;

import com.rapidftr.model.Child;

public class ChildChangeLogScreen extends CustomScreen {
	

	
	public ChildChangeLogScreen(Child child)
	{
		JSONArray histories;
		Object JSONHistory = child.getField("histories");
		if(JSONHistory == null)
		{
			add(new RichTextField("No History Logs Present"));

		}
		else
		{
			try 
			{
				histories = new JSONArray(JSONHistory.toString());
				layoutScreen(histories);
			}
			catch (JSONException e) {
				Dialog.alert("unable to parse child history");
			}	
			
		}
	}

	private void layoutScreen(JSONArray histories) {
		
		for(int i =0 ; i < histories.length();i++)
		{
			JSONObject history = null;
			Enumeration keys =null;
			Object nextElement = null;
			try {
				 
				history = histories.getJSONObject(i);
				JSONObject changes = history.getJSONObject("changes");
				Enumeration changedFields = changes.keys();
				while(changedFields.hasMoreElements())
				{
					String changedFieldName= (String) changedFields.nextElement();
					JSONObject changedFieldObject = changes.getJSONObject(changedFieldName);
					String changeDateTime = history.getString("datetime");
					String oldValue = changedFieldObject.getString("from");
					String newalue= changedFieldObject.getString("to");
					if(oldValue.equals(""))
					{
						add(new RichTextField(changeDateTime + " " +   changedFieldName + " intialized to " + newalue + " By " +history.getString("user_name")));
					}
					else
					{
						add(new RichTextField(changeDateTime + " " +   changedFieldName + " changed from "+ oldValue + " to " + newalue + " By " +history.getString("user_name")));
						
					}
					
					add(new SeparatorField());
				}
				
				
			} catch (Exception e) {
				Dialog.alert("Unable to parse child history" + history + nextElement);
				break;
			}
			
		}
		
	}

	public void cleanUp() {

	}

	public void setUp() {

	}

	public void parseChanges(JSONObject change) throws JSONException
	{
		Enumeration changedFields = change.keys();
		while(changedFields.hasMoreElements())
		{
			String changedField= (String) changedFields.nextElement();
			JSONObject changedFieldObject = change.getJSONObject(changedField);
			add(new LabelField(" from "+ changedFieldObject.getString("from")+ " to " +changedFieldObject.getString("to")));
		}
	}
}
