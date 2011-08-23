package com.rapidftr.model;

public class ChildHistoryChangeEntry {
	private String oldValue;
	private String newValue;
	private String changedFieldName;

	public ChildHistoryChangeEntry(String changedFieldName, String oldValue,
			String newValue) {
				this.changedFieldName = changedFieldName;
				this.oldValue = oldValue;
				this.newValue = newValue;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((changedFieldName == null) ? 0 : changedFieldName.hashCode());
		result = prime * result
				+ ((newValue == null) ? 0 : newValue.hashCode());
		result = prime * result
				+ ((oldValue == null) ? 0 : oldValue.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildHistoryChangeEntry other = (ChildHistoryChangeEntry) obj;
		if (changedFieldName == null) {
			if (other.changedFieldName != null)
				return false;
		} else if (!changedFieldName.equals(other.changedFieldName))
			return false;
		if (newValue == null) {
			if (other.newValue != null)
				return false;
		} else if (!newValue.equals(other.newValue))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		return true;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getChangedFieldName() {
		return changedFieldName;
	}

	public void setChangedFieldName(String changedFieldName) {
		this.changedFieldName = changedFieldName;
	}

	public String getChangeDescription() {
		String description;
		if (getOldValue().equals("")) {
	        description = getChangedFieldName() + " initialized to "
	                + getNewValue();
	    } else {
	        description = getChangedFieldName() + " changed from "
	                + getOldValue() + " to " + getNewValue();
	    }
		return description;
	}
}