package com.rapidftr.services;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;

public interface RecordService {
	String getRecordId() throws ServiceException;

	ChildRecordItem[] getMatches(String searchCriteria) throws ServiceException;
	
	ChildRecord getRecord(String recordId) throws ServiceException;
	
	void save(ChildRecord record) throws ServiceException;
}
