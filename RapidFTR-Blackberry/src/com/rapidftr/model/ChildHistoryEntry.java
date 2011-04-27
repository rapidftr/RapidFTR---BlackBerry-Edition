package com.rapidftr.model;

public class ChildHistoryEntry {
	private String oldValue;
	private String newValue;
	private String changedFieldName;

	public ChildHistoryEntry(String oldValue, String newValue, String changedFieldName) {
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.changedFieldName = changedFieldName;
	}

	public String getFieldChangeDescription() {
		String description = "";
		if (oldValue == null || "".equals(oldValue)) {
			description = changedFieldName + " initialized to " + newValue;
		} else {
			description = changedFieldName + " changed from " + oldValue
					+ " to " + newValue;
		}
		return description;
	}
}