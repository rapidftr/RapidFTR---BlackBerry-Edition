package com.rapidftr.services.impl;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.services.RecordService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.LocalStore;
import com.rapidftr.utilities.TestRecordUtil;
import com.rapidftr.utilities.impl.LocalStoreImpl;

public class RecordServiceImpl implements RecordService {
	private static final String ID = "12367688AA";

	private static RecordService instance;

	public static synchronized RecordService getInstance() {
		if (instance == null) {
			instance = new RecordServiceImpl();
		}

		return instance;
	}

	private RecordServiceImpl() {
	}

	public ChildRecordItem[] getMatches(String searchCriteria) {
		LocalStore localStore = LocalStoreImpl.getInstance();
		
		return localStore.retrieveAll();
	}
	
	public ChildRecord getRecord(String recordId) throws ServiceException {
		return TestRecordUtil.createTestRecord(recordId);
	}

	public String getRecordId() throws ServiceException {
		return ID;
	}

	public void save(ChildRecord record) throws ServiceException {
		// TODO Auto-generated method stub
		
	}


}
