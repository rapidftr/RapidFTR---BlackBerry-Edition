package com.rapidftr.screens;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.model.Child;

public class ViewChildrenScreen extends CustomScreen {
  
    private ObjectListField childList;

    public ViewChildrenScreen() {
        super();
        layoutScreen();
    }

    private void layoutScreen() {
        add(new LabelField("All children"));
        add(new SeparatorField());
        childList = new ObjectListField() {
            protected boolean navigationClick(int i, int i1) {
                Child child = (Child) this.get(this, this.getSelectedIndex());
                ((ViewChildrenController) controller).showChild(child);
                return super.navigationClick(i, i1);   
            }
        };
        add(childList);
    }

    public void setChildren(Child[] children) {
        childList.set(children);
    }

	public void setUp() {
		// TODO Auto-generated method stub
		
	}

	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}
}
