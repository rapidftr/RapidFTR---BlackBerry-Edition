package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class Caregiver implements Persistable {	
	private String name;
	private String profession;
	private String relationshipToChild;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getRelationshipToChild() {
		return relationshipToChild;
	}
	public void setRelationshipToChild(String relationshipToChild) {
		this.relationshipToChild = relationshipToChild;
	}
	
	
}
