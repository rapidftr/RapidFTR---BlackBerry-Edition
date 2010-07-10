package com.rapidftr.screens;

import com.rapidftr.model.Child;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

public class ViewChildScreen extends MainScreen {

    public ViewChildScreen() {
    }

    public void setChild(Child child) {
        clearFields();
        add(new LabelField("Child: " + child.getName()));
    }

    private void clearFields() {
        int fieldCount = this.getFieldCount();
        if (fieldCount > 0)
                this.deleteRange(0, fieldCount);
    }
}
