package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class ChildRecordItem implements Persistable {	
	protected String recordId;
	protected String name;
	protected byte[] photo;
	
	public ChildRecordItem() {}
	
	public ChildRecordItem(String id, String name, byte[] photo) {
		this.recordId = id;
		this.name = name;
		this.photo = photo;
	}
	
	public String getRecordId() {
		return recordId;
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
