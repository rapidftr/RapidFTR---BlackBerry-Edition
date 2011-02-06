package com.rapidftr.model;

public class ChildHistoryItem {
    private String username;
    private String description;

    public ChildHistoryItem(String username, String description) {
        this.username = username;
        this.description = description;
    }

    public String toString() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}
