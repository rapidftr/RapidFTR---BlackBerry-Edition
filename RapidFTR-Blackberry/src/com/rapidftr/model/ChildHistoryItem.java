package com.rapidftr.model;

import java.util.Vector;

public class ChildHistoryItem {
    private String username;
    private Vector data;
	private String changeDateTime;

	public ChildHistoryItem(String username, String changeDateTime, 
                            Vector data) {
        this.username = username;
        this.changeDateTime = changeDateTime;
        this.data = data;
    }

	public String toString() {
        return description();
    }

    private String description() {
        return changeDateTime + getFieldChangesDescription();
    }

    public String getUsername() {
        return username;
    }

    public String getFieldChangesDescription() {
    	String description = "";
    	for(int i=0; i< getData().size(); i++){
    		ChildHistoryChangeEntry childHistoryChangeEntry = ((ChildHistoryChangeEntry) getData().elementAt(i)); 
    		description = description + "  " + childHistoryChangeEntry.getChangeDescription() + "\n";
    	}
    	return description + "By " + username;
    }

	public String getChangeDateTime() {
		return changeDateTime;
	}

	public Vector getData() {
		return data;
	}
}