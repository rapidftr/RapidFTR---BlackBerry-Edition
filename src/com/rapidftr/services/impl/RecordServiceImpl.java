package com.rapidftr.services.impl;

import java.util.Random;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.services.RecordService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.LocalStore;
import com.rapidftr.utilities.impl.LocalStoreImpl;

public class RecordServiceImpl implements RecordService {
	private static RecordService instance;

	private LocalStore localStore;
	
	public static synchronized RecordService getInstance() {
		if (instance == null) {
			instance = new RecordServiceImpl();
		}

		return instance;
	}

	private RecordServiceImpl() {
		localStore = LocalStoreImpl.getInstance();
	}

	public ChildRecordItem[] getMatches(String searchCriteria) {
		return localStore.retrieveMatching(searchCriteria);
	}
	
	public ChildRecord getRecord(String recordId) throws ServiceException {
		ChildRecord records[] = (ChildRecord[])localStore.retrieveAll();
		
		ChildRecord match = null;
		
		for ( int i=0; i<records.length; i++ ) {
			if ( records[i].getRecordId().equals(recordId) ) {
				match = records[i];
				break;
			}
		}
		
		return match;
	}

	public String getRecordId() throws ServiceException {
		Random random = new Random();
		
		int id = random.nextInt();
		id = (id < 0) ? -id : id;
		
		return String.valueOf(id);
	}

	public void save(ChildRecord record) throws ServiceException {
		localStore.persist(record);
	}


}
