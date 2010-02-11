package com.rapidftr.model;

public class Relative {

	public static final int MOTHER = 100;
	public static final int FATHER = 200;
	public static final int SIBLING = 300;
	public static final int UNCLE = 400;
	public static final int AUNT = 500;
	public static final int COUSIN = 600;
	public static final int NEIGHBOR = 700;
	public static final int OTHER = 800;

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
	
	
}
