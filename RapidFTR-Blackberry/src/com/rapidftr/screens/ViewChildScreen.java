package com.rapidftr.screens;

import com.rapidftr.model.Child;
import net.rim.device.api.ui.component.LabelField;

import java.util.Enumeration;

public class ViewChildScreen extends CustomScreen {

    public ViewChildScreen() {
    }

    public void setChild(Child child) {
        clearFields();
        renderChildFields(child);
    }

    private void renderChildFields(Child child) {

//                Enumeration fieldNames = child.getFieldNames();
//                while (fieldNames.hasMoreElements()) {
//                    String fieldName = (String)fieldNames.nextElement();
//                    String fieldValue = child.getField(fieldName);
//                    add(new LabelField(fieldName + ": " + fieldValue));
//                }

    }

    private void clearFields() {
        int fieldCount = this.getFieldCount();
        if (fieldCount > 0)
                this.deleteRange(0, fieldCount);
    }

	public void setUp() {
		// TODO Auto-generated method stub
		
	}

	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}
}
