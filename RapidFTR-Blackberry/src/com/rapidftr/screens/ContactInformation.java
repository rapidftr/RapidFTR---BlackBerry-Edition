package com.rapidftr.screens;

import com.rapidftr.utilities.Store;

public class ContactInformation {
	private final Store store;
	public ContactInformation(Store store) {
		this.store = store;
	}
	public String getName(){
		return store.getString("contact.name");
	}
	public String getPosition(){
		return store.getString("contact.position");
	}
	public String getOrganization(){
		return store.getString("contact.organization");
	}
	public String getEmail(){
		return store.getString("contact.email");
	}
	public String getPhone(){
		return store.getString("contact.phone");
	}
	public String getLocation(){
		return store.getString("contact.location");
	}
	public String getOther(){
		return store.getString("contact.other");
	}
	public void setName(String value){
		store.setString("contact.name", value);
	}
	public void setPosition(String value){
		store.setString("contact.position", value);
	}
	public void setOrganization(String value){
		store.setString("contact.organization", value);
	}
	public void setEmail(String value){
		store.setString("contact.email", value);
	}
	public void setPhone(String value){
		store.setString("contact.phone", value);
	}
	public void setLocation(String value){
		store.setString("contact.location", value);
	}
	public void setOther(String value){
		store.setString("contact.other", value);
	}
}
