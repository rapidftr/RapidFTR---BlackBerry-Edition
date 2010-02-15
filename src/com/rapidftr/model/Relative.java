package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class Relative implements Persistable {	

	public static final int MOTHER = 0;
	public static final int FATHER = 1;
	public static final int SIBLING = 2;
	public static final int UNCLE = 3;
	public static final int AUNT = 4;
	public static final int COUSIN = 5;
	public static final int NEIGHBOR = 6;
	public static final int OTHER = 7;

	private static final String relationships[] = {
		"Mother",
		"Father",
		"Sibling",
		"Uncle",
		"Aunt",
		"Cousin",
		"Neighbor",
		"Other"
	};
	
	private int type;
	private String name;
	private boolean isAlive;
	private boolean shouldReunite;

	public Relative(int type, String name, boolean isAlive,
			boolean shouldReunite) {
		this.type = type;
		this.name = name;
		this.isAlive = isAlive;
		this.shouldReunite = shouldReunite;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public boolean isShouldReunite() {
		return shouldReunite;
	}

	public void setShouldReunite(boolean shouldReunite) {
		this.shouldReunite = shouldReunite;
	}
	
	public String getRelationship() {
		return relationships[getType()];
	}
}
