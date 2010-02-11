package com.rapidftr.services.impl;

import net.rim.device.api.system.EncodedImage;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.model.Identification;
import com.rapidftr.services.RecordService;
import com.rapidftr.services.ServiceException;

public class RecordServiceImpl implements RecordService {

	private static final ChildRecordItem CHILD_DATA[] = new ChildRecordItem[8];

	static {
		CHILD_DATA[0] = new ChildRecordItem("DONAL-12367600AA", "Jane Doe",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[1] = new ChildRecordItem("DONAL-12367603AA", "Jennifer Smith",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[2] = new ChildRecordItem("DONAL-12367604AA", "Lisa Cooke",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[3] = new ChildRecordItem("DONAL-12367608AA", "Emily Jones",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[4] = new ChildRecordItem("DONAL-12367600AA", "John Doe",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[5] = new ChildRecordItem("DONAL-12367603AA", "Jennifer Smith",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[6] = new ChildRecordItem("DONAL-12367604AA", "Lisa Cooke",
				getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[7] = new ChildRecordItem("DONAL-12367608AA", "Emily Jones",
				getImageAsBytes("img/cait.jpg"));
	}

	private static final String ID = "-12367688AA";

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
		return CHILD_DATA;
	}
	
	public ChildRecord getRecord(String recordId) throws ServiceException {
		ChildRecord record = new ChildRecord();
		
		ChildRecordItem item = null;
		
		for ( int i=0; i<CHILD_DATA.length; i++ ) {
			if  (recordId.equals(CHILD_DATA[i].getRecordId()) ) {
				item = CHILD_DATA[i];
				break;
			}
		}
		
		Identification identification = new Identification();
		
		identification.setAge(7);
		identification.setDateOfSeparation(Identification.SEP_1_6_MTHS);
		identification.setExactAge(false);
		identification.setLastKnownLocation("Old Town");
		identification.setOrigin("New Town");
		
		record.setIdentification(identification);
		
		record.setRecordId(recordId);
		record.setName(item.getName());
		record.setPhoto(item.getPhoto());

		return record;
	}

	public String getRecordId() throws ServiceException {
		return ID;
	}

	public void save(ChildRecord record) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	private static byte[] getImageAsBytes(String imageName) {
		// Creates an EncodedImage from provided name resource
		EncodedImage image = EncodedImage.getEncodedImageResource(imageName);
		
		// Returns a byte array containing the encoded data for this EncodedImage
		return image.getData();
	}
}
