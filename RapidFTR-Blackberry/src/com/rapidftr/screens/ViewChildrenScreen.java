package com.rapidftr.screens;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.model.Child;

public class ViewChildrenScreen extends MainScreen {
    private ViewChildrenController controller;
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
                controller.showChild(child);
                return super.navigationClick(i, i1);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        add(childList);
    }

    public void setViewChildrenController(ViewChildrenController controller) {
        this.controller = controller;
    }

    public void setChildren(Child[] children) {
        childList.set(children);
    }
}
