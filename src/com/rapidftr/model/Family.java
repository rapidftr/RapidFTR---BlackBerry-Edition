package com.rapidftr.model;

public class Family {
	private Relative mother;
	private Relative father;
	private Relative[] siblings;
	private Relative[] uncles;
	private Relative[] aunts;
	private Relative[] cousins;
	private Relative[] neighbors;
	private Relative[] others;
	private boolean isMarried;
	private String partnerName;
	private int numberChildren;
	
	public Relative getMother() {
		return mother;
	}
	public void setMother(Relative mother) {
		this.mother = mother;
	}
	public Relative getFather() {
		return father;
	}
	public void setFather(Relative father) {
		this.father = father;
	}
	public Relative[] getSiblings() {
		return siblings;
	}
	public void setSiblings(Relative[] siblings) {
		this.siblings = siblings;
	}
	public Relative[] getUncles() {
		return uncles;
	}
	public void setUncles(Relative[] uncles) {
		this.uncles = uncles;
	}
	public Relative[] getAunts() {
		return aunts;
	}
	public void setAunts(Relative[] aunts) {
		this.aunts = aunts;
	}
	public Relative[] getCousins() {
		return cousins;
	}
	public void setCousins(Relative[] cousins) {
		this.cousins = cousins;
	}
	public Relative[] getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(Relative[] neighbors) {
		this.neighbors = neighbors;
	}
	public Relative[] getOthers() {
		return others;
	}
	public void setOthers(Relative[] others) {
		this.others = others;
	}
	public boolean isMarried() {
		return isMarried;
	}
	public void setMarried(boolean isMarried) {
		this.isMarried = isMarried;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public int getNumberChildren() {
		return numberChildren;
	}
	public void setNumberChildren(int numberChildren) {
		this.numberChildren = numberChildren;
	}
	
	
}
