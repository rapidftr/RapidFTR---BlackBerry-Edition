package com.rapidftr.screens;

import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.model.Child;
import com.rapidftr.model.ChildHistories;
import com.rapidftr.model.ChildHistoryItem;
import com.rapidftr.model.HistoryAction;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.DateFormatter;

public class ChildHistoryScreen extends CustomScreen {
	private Child child;
	private DateFormatter dateFormatter;

	public ChildHistoryScreen() {
		super();
	}

	public ChildHistoryScreen(DateFormatter dateFormatter) {
		this();
		this.dateFormatter = dateFormatter;
	}

	public void setChild(Child child) {
		clearFields();
		this.child = child;
		try {
			ChildHistories histories = this.child.getHistory();
			if (histories.isNotEmpty()) {
				layoutScreen(histories);
			} else {
				add(new RichTextField("No History Logs Present"));
			}
		} catch (final Exception e) {
			showErrorDialog(e);
		}
	}

	private void layoutScreen(ChildHistories histories) {
		histories.forEachHistory(new HistoryAction() {
			public void execute(ChildHistoryItem historyItem) {
				add(new RichTextField(getDescription(historyItem)));
				add(new SeparatorField());
			}
		});
	}

	private void showErrorDialog(final Exception e) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog.alert("Error Occured while displaying change log "
						+ e.getMessage());
				controller.popScreen();
			}
		});
	}

	private String getDescription(ChildHistoryItem childHistoryItem) {
		String description = childHistoryItem.getFieldChangesDescription();
		long changeTime = HttpDateParser.parse(childHistoryItem
				.getChangeDateTime());
		return dateFormatter.format(changeTime) + "\n" + description;
	}

}
