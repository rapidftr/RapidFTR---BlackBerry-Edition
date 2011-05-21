package com.rapidftr.model;

import java.util.Vector;

public class ChildHistoryItem {
    private String username;
    private String changeDateTime;
	private final Vector changedEntries;

    public String getChangeDateTime() {
        return changeDateTime;
    }

    public ChildHistoryItem(String username, String changeDateTime, Vector changedEntries) {
        this.username = username;
        this.changeDateTime = changeDateTime;
		this.changedEntries = changedEntries;
    }

    public String toString() {
        return description();
    }

    private String description() {

        return changeDateTime + " " + getFieldChangeDescription();
    }

    public String getUsername() {
        return username;
    }

    public String getFieldChangeDescription() {
    	String description = "";
    	description += getUsername() + " changed:\n";
        for(int i = 0; i < changedEntries.size();i++){
        	description += "  " + ((ChildHistoryEntry)changedEntries.elementAt(i)).getFieldChangeDescription() + "\n";
        }
        return description;
    }
}
