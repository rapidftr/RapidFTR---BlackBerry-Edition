package com.rapidftr.utilities;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.model.Identification;

public class TestRecordUtil {

	private static final ChildRecordItem CHILD_DATA[] = new ChildRecordItem[8];

	static {
		CHILD_DATA[0] = new ChildRecordItem("DONAL-12367600AA", "Jane Doe",
				Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[1] = new ChildRecordItem("DONAL-12367603AA",
				"Jennifer Smith", Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[2] = new ChildRecordItem("DONAL-12367604AA", "Lisa Cooke",
				Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[3] = new ChildRecordItem("DONAL-12367608AA", "Emily Jones",
				Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[4] = new ChildRecordItem("DONAL-12367600AA", "John Doe",
				Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[5] = new ChildRecordItem("DONAL-12367603AA",
				"Jennifer Smith", Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[6] = new ChildRecordItem("DONAL-12367604AA", "Lisa Cooke",
				Utilities.getImageAsBytes("img/cait.jpg"));
		CHILD_DATA[7] = new ChildRecordItem("DONAL-12367608AA", "Emily Jones",
				Utilities.getImageAsBytes("img/cait.jpg"));
	}

	public static ChildRecord[] createTestRecords() {
		ChildRecord[] records = new ChildRecord[CHILD_DATA.length];
		
		for ( int i=0; i<CHILD_DATA.length; i++ ) {
			records[i] = createTestRecord(CHILD_DATA[i].getRecordId());
		}
		
		return records;
	}
	
	public static ChildRecord createTestRecord(String recordId) {
		ChildRecord record = new ChildRecord();

		ChildRecordItem item = null;

		for (int i = 0; i < CHILD_DATA.length; i++) {
			if (recordId.equals(CHILD_DATA[i].getRecordId())) {
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
}
