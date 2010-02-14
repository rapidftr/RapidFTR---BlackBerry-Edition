package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class ChildRecord extends ChildRecordItem implements Persistable {	
	private Identification identification;
	private Family family;
	private Caregiver careGiver;
	private ProtectionConcerns protectionConcerns;

	public ChildRecord() {}
	
	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		this.identification = identification;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public Caregiver getCareGiver() {
		return careGiver;
	}

	public void setCareGiver(Caregiver careGiver) {
		this.careGiver = careGiver;
	}

	public ProtectionConcerns getProtectionConcerns() {
		return protectionConcerns;
	}

	public void setProtectionConcerns(ProtectionConcerns protectionConcerns) {
		this.protectionConcerns = protectionConcerns;
	}
	
	
}
