package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;

public class Tabs implements Observable, FocusChangeListener{

	private final Child child;
	private Vector tabs = new Vector();
	private Vector observers = new Vector();

	public Tabs(Child child) {
		this.child = child;
	}

	public void addForm(Form form) {
		Tab tab = new Tab(form.toString(), form, child);
		tabs.addElement(tab);
		tab.addFocusChangeObserver(this);
	}

	public Tab getDefaultTab(){
		return (tabs.isEmpty())? null : (Tab)tabs.elementAt(0);
	}

	public void addFocusChangeObserver(FocusChangeListener observer) {
		observers.addElement(observer);
	}

	public void focusChanged(Field field, int eventType) {
		notifyObservers(field, eventType);
	}

	private void notifyObservers(Field field, int eventType) {
		for(int i = 0;i < observers.size(); i++){
			((FocusChangeListener)observers.elementAt(i)).focusChanged(field, eventType);
		}
	}

	public void forEachTab(TabAction tabAction) {
		for(int i = 0;i < tabs.size(); i++){
			tabAction.execute(((Tab)tabs.elementAt(i)));
		}
	}

}
