package com.rapidftr.utilities;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;

public interface LocalStore {

	void persist(ChildRecord[] records);
	
	void persist(ChildRecord record);
	
	ChildRecordItem[] retrieveAll();
	
	int countStoredRecords();
	
	ChildRecord retrieve(String recordId);
}
