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
	public void setName(String name){
		store.setString("contact.name", name);
	}
	public void setPosition(String name){
		store.setString("contact.position", name);
	}
	public void setOrganization(String name){
		store.setString("contact.organization", name);
	}
	public void setEmail(String name){
		store.setString("contact.email", name);
	}
	public void setPhone(String name){
		store.setString("contact.phone", name);
	}
	public void setLocation(String name){
		store.setString("contact.location", name);
	}
	public void setOther(String name){
		store.setString("contact.other", name);
	}
}
