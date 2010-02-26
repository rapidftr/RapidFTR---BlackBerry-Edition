package com.rapidftr.services;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;

public interface RecordService {
	ChildRecordItem[] getMatches(String searchCriteria) throws ServiceException;
	
	ChildRecord getRecord(ChildRecordItem item) throws ServiceException;
	
	void save(ChildRecord record) throws ServiceException;
}
