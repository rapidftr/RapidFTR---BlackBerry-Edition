package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;

public class ChildHistoryScreen extends CustomScreen {
	
	private Child child;
	
	public ChildHistoryScreen(){
		super();
	}
	
	public void setChild(Child child){
		this.child = child;
		Vector history = this.child.getHistory();
		if (history==null || history.size() == 0) {
			add(new RichTextField("No History Logs Present"));

		} else {
			layoutScreen(history);

		}
	}

	private void layoutScreen(Vector history) {
		try {
			Enumeration logs = history.elements();
			while (logs.hasMoreElements()) {
				add(new RichTextField(logs.nextElement().toString()));

				add(new SeparatorField());
			}
		} catch (final Exception e) {

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("Error Occured while displaying change log "
							+ e.getMessage());
					controller.popScreen();

				}
			});
		}
	}

}
