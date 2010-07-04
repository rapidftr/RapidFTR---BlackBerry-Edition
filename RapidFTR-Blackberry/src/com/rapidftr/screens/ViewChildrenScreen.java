package com.rapidftr.screens;

import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.model.Child;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectListField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

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
        childList = new ObjectListField();
        add(childList);
    }

    public void setViewChildrenController(ViewChildrenController controller) {
        this.controller = controller;
    }

    public void setChildren(Child[] children) {
        childList.set(children);
    }
}
