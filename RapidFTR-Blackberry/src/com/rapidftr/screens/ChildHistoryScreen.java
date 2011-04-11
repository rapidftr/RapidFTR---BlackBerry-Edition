package com.rapidftr.screens;

import com.rapidftr.model.Child;
import com.rapidftr.model.ChildHistoryItem;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.DateFormatter;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;

import java.util.Enumeration;
import java.util.Vector;

public class ChildHistoryScreen extends CustomScreen {
	
	private Child child;
    private DateFormatter dateFormatter;

    public ChildHistoryScreen(){
		super();
	}

    public ChildHistoryScreen(DateFormatter dateFormatter){
        this();
        this.dateFormatter = dateFormatter;
    }
	
	public void setChild(Child child){
		clearFields();
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
				add(new RichTextField(getDescription((ChildHistoryItem)logs.nextElement())));

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

    private String getDescription(ChildHistoryItem childHistoryItem) {
        String description = childHistoryItem.getFieldChangeDescription();
        long changeTime = HttpDateParser.parse(childHistoryItem.getChangeDateTime());
        return dateFormatter.format(changeTime) + " " + description;
    }

}
