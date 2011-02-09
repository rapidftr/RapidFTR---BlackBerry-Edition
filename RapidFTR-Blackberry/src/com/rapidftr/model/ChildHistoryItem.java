package com.rapidftr.model;

public class ChildHistoryItem {
    private String username;
    private String oldValue;
    private String newValue;
    private String changedFieldName;
    private String changeDateTime;

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getChangedFieldName() {
        return changedFieldName;
    }

    public String getChangeDateTime() {
        return changeDateTime;
    }

    public ChildHistoryItem(String username, String changeDateTime, 
                            String changedFieldName,
                            String oldValue, String newValue) {
        this.username = username;
        this.changedFieldName = changedFieldName;
        this.changeDateTime = changeDateTime;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String toString() {
        return description();
    }

    private String description() {
        String description = "";
        if (oldValue.equals("")) {
            description = changeDateTime + " "
                    + changedFieldName + " intialized to "
                    + newValue + " By "
                    + username;
        } else {
            description = changeDateTime + " "
                    + changedFieldName + " changed from "
                    + oldValue + " to " + newValue + " By "
                    + username;
        }
        return description;
    }

    public String getUsername() {
        return username;
    }
}
