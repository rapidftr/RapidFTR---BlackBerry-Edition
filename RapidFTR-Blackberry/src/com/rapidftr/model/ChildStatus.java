package com.rapidftr.model;

import net.rim.device.api.ui.Color;
import net.rim.device.api.util.Persistable;

public class ChildStatus implements Persistable {
	
	private static final String STATUS_STRING_NEW = "New";
	private static final String STATUS_STRING_UPDATED = "Updated";
	private static final String STATUS_STRING_SYNCED = "Synced";
	private static final String STATUS_STRING_SYNC_FAILED = "Failed";

	public static final ChildStatus NEW = new ChildStatus(Color.BLUE, STATUS_STRING_NEW);
	public static final ChildStatus UPDATED = new ChildStatus(Color.CYAN, STATUS_STRING_UPDATED);
	public static final ChildStatus SYNCED = new ChildStatus(Color.GREEN, STATUS_STRING_SYNCED);
	public static final ChildStatus SYNC_FAILED = new ChildStatus(Color.DARKRED, STATUS_STRING_SYNC_FAILED);


	private String syncError;

	private int colorCode;
	private String statusString;

	private ChildStatus(int colorCode, String statusString) {
		this.colorCode = colorCode;
		this.statusString = statusString;
	}
	
	public String getStatusString(){
		return statusString;
	}

	public int getStatusColor() {
		return colorCode;
	}

	public String getSyncError() {
		return syncError;
	}

	public void setSyncError(String syncError) {
		this.syncError = syncError;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colorCode;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildStatus other = (ChildStatus) obj;
		if (colorCode != other.getStatusColor())
			return false;
		return true;
	}

}
