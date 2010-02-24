package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class ChildRecordItem implements Persistable {
	// temporary?
	private static final String RECORD_ID_PREFIX = "fix_me_to_return_session_user_name";

	protected String id;
	protected String recordId;
	protected String name;
	protected byte[] photo;

	public ChildRecordItem() {
	}

	public ChildRecordItem(String recordId, String name, byte[] photo) {
		this.recordId = recordId;
		this.name = name;
		this.photo = photo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecordId() {
		return (recordId.indexOf(RECORD_ID_PREFIX) != -1) ? recordId
				.substring(RECORD_ID_PREFIX.length()) : recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
}
