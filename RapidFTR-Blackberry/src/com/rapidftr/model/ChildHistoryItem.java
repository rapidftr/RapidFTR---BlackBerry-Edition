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

        return changeDateTime + " " + getFieldChangeDescription();
    }

    public String getUsername() {
        return username;
    }

    public String getFieldChangeDescription() {
       String description = "";
        if (oldValue.equals("")) {
            description = changedFieldName + " intialized to "
                    + newValue + " By "
                    + username;
        } else {
            description = changedFieldName + " changed from "
                    + oldValue + " to " + newValue + " By "
                    + username;
        }
        return description;
    }
}
