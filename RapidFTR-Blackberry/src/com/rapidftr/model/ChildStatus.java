package com.rapidftr.model;

import net.rim.device.api.ui.Color;
import net.rim.device.api.util.Persistable;

public class ChildStatus implements Persistable {

	public static final ChildStatus NEW = new ChildStatus(Color.WHITE);
	public static final ChildStatus UPDATED = new ChildStatus(Color.BLUE);
	public static final ChildStatus SYNCED = new ChildStatus(Color.GREEN);
	public static final ChildStatus SYNC_FAILED = new ChildStatus(Color.DARKRED);

	private String syncError;

	private int colorCode;

	private ChildStatus(int colorCode) {
		this.colorCode = colorCode;
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
		if (colorCode != other.colorCode)
			return false;
		return true;
	}

}
