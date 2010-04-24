package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class Family implements Persistable {
	private boolean isMarried;
	private String partnerName;
	private int numberChildren;

	private Vector relatives;

	public Family() {
		relatives = new Vector();
	}
	
	public Vector getAllRelatives() {
		return relatives;
	}

	public Relative getMother() {
		return (Relative)relatives.elementAt(0);
	}

	public void setMother(Relative mother) {
		relatives.insertElementAt(mother, 0);
	}

	public Relative getFather() {
		return (Relative)relatives.elementAt(1);
	}

	public void setFather(Relative father) {
		relatives.insertElementAt(father, 1);
	}

	public void setSiblings(Relative[] siblings) {
		if (siblings != null) {
			for (int i = 0; i < siblings.length; i++) {
				Relative relative = siblings[i];
				
				relatives.addElement(relative);
			}
		}
	}

	public void setUncles(Relative[] uncles) {
		if (uncles != null) {
			for (int i = 0; i < uncles.length; i++) {
				Relative relative = uncles[i];
				relatives.addElement(relative);
			}
		}
	}
	
	public void setAunts(Relative[] aunts) {
		if (aunts != null) {
			for (int i = 0; i < aunts.length; i++) {
				Relative relative = aunts[i];
				relatives.addElement(relative);
			}
		}
	}

	public void setCousins(Relative[] cousins) {
		if (cousins != null) {
			for (int i = 0; i < cousins.length; i++) {
				Relative relative = cousins[i];
				relatives.addElement(relative);
			}
		}
	}

	public void setNeighbors(Relative[] neighbors) {
		if (neighbors != null) {
			for (int i = 0; i < neighbors.length; i++) {
				Relative relative = neighbors[i];
				relatives.addElement(relative);
			}
		}
	}

	public void setOthers(Relative[] others) {
		if (others != null) {
			for (int i = 0; i < others.length; i++) {
				Relative relative = others[i];
				relatives.addElement(relative);
			}
		}
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
