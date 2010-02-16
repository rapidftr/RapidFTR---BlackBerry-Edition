package com.rapidftr.utilities.impl;

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Arrays;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.utilities.LocalStore;

public class LocalStoreImpl implements LocalStore {
	static long storeId = 0xe05ae2fdL;

	private static LocalStore instance;
	
	private PersistentObject store;

	public static synchronized LocalStore getInstance() {
		if ( instance == null ) {
			instance = new LocalStoreImpl();
		}
		
		return instance;
	}
	
	private LocalStoreImpl() {
		store = PersistentStore.getPersistentObject(storeId);
	}

	public ChildRecord retrieve(String recordId) {
		ChildRecord record = null;
		
		synchronized (store) {
			Vector _data = (Vector) store.getContents();
			if ((_data != null) &&  (!_data.isEmpty())) {
				
				for ( int i=0; i<_data.size(); i++ ) {
					ChildRecord childRecord = (ChildRecord) _data.elementAt(i);
					
					if ( childRecord.equals(recordId) ) {
						record = childRecord;
						break;
					}
				}
			}
		}	
		
		return record;	
	}
	
	public void persist(ChildRecord[] records) {
		for ( int i=0; i<records.length; i++ ) {
			persist(records[i]);
		}
	}

	public int countStoredRecords() {
		ChildRecordItem records[] = retrieveAll();
		
		return (records == null) ? 0 : records.length;
	}
	
	public ChildRecordItem[] retrieveMatching(String searchCriteria) {
		ChildRecordItem[] matchedRecords = new ChildRecordItem[0];
		
		if ( searchCriteria.length() > 0 ) {
			ChildRecordItem[] records = retrieveAll();

			for ( int i=0; i<records.length; i++ ) {
				String searchString = searchCriteria.toLowerCase();
				
				if ( ( records[i].getName().toLowerCase().indexOf(searchString) != -1 ) 
						|| ( records[i].getRecordId().indexOf(searchString) != -1 ) ) {
					Arrays.add(matchedRecords, records[i]);
				}
			}
			
			return matchedRecords;
		}
		else {
			return retrieveAll();
		}
	}
	
	public ChildRecordItem[] retrieveAll() {
		ChildRecord records[] = null;
		
		synchronized (store) {
			Vector _data = (Vector) store.getContents();
			if ((_data != null) && (!_data.isEmpty())) {
				records = new ChildRecord[_data.size()];
				
				for ( int i=0; i<_data.size(); i++ ) {
					records[i] = (ChildRecord) _data.elementAt(i);
				}
			}
		}	
		
		return records;
	}

	
	public void persist(ChildRecord record) {
		synchronized (store) {
			Vector _data = (Vector) store.getContents();
			
			boolean isEmpty = (_data == null) || _data.isEmpty();
			
			_data = (isEmpty) ? new Vector() : _data;
			
			_data.addElement(record);
			
			store.setContents(_data);
			store.commit();
		}
	}

}
