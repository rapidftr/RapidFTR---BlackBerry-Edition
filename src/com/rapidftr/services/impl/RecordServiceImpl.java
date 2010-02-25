package com.rapidftr.services.impl;

import java.io.InputStream;
import java.util.Hashtable;

import net.rim.device.api.util.Arrays;
import net.rim.device.api.xml.parsers.SAXParser;
import net.rim.device.api.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.model.Identification;
import com.rapidftr.services.RecordService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.HttpServer;
import com.rapidftr.utilities.LocalStore;
import com.rapidftr.utilities.Properties;
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

	public ChildRecordItem[] getMatches(String searchCriteria)
			throws ServiceException {
		ChildRecordItem[] records = null;

		try {
			InputStream is = HttpServer.getInstance().getFromServer("children");

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			RecordHandler handler = new RecordHandler(searchCriteria);

			parser.parse(is, handler);

			records = handler.getRecords();

			System.out.println("Got matches from server " + records);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		// augment with any extra data in local store
		return localStore.augmentRecords(records);
	}

	public ChildRecord getRecord(ChildRecordItem item) throws ServiceException {
		ChildRecord record = null;

		System.out.println("SEARCH USING ID " + item.getId());

		try {
			HttpServer server = HttpServer.getInstance();

			InputStream is = server.getFromServer("children/" + item.getId());

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			ItemHandler handler = new ItemHandler(item);

			parser.parse(is, handler);

			record = handler.getRecord();

			byte[] photo = server.getImageFromServer("children/" + item.getId()
					+ ".jpg");

			record.setPhoto(photo);

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		return record;
	}

	// from local store
	/**
	 * public ChildRecord getRecord(String recordId) throws ServiceException {
	 * ChildRecord records[] = (ChildRecord[]) localStore.retrieveAll();
	 * 
	 * ChildRecord match = null;
	 * 
	 * for (int i = 0; i < records.length; i++) { if
	 * (records[i].getRecordId().equals(recordId)) { match = records[i]; break;
	 * } }
	 * 
	 * return match; }
	 **/

	public void save(ChildRecord record) throws ServiceException {
		try {
			String id = persistToServer(record, record.getPhoto());

			if (id == null) {
				throw new ServiceException("Failed to save child record");
			}

			record.setId(id);

			localStore.persist(record);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private String persistToServer(ChildRecord record, byte[] photoData)
			throws Exception {

		HttpServer server = HttpServer.getInstance();

		String token = Properties.getInstance().getAuthenticityToken();

		Hashtable params = new Hashtable();

		String action;
		String uri;
		boolean isUpdate;

		if (record.getId() == null) { // new record
			action = "Create";
			uri = "children";
			isUpdate = false;
		} else { // updated record
			action = "Update";
			uri = "children/" + record.getId();
			isUpdate = true;
		}

		params.put("commit", action);

		params.put("authenticity_token", token);

		params.put("child[name]", record.getName());
		params.put("child[age]", String.valueOf(record.getIdentification()
				.getAge()));

		String isExact = (record.getIdentification().isExactAge()) ? "exact"
				: "approximate";

		params.put("child[is_age_exact]", isExact);
		String gender = (record.getIdentification().isMale()) ? "male"
				: "female";

		params.put("child[gender]", gender);
		params.put("child[origin]", record.getIdentification().getOrigin());

		params.put("child[last_known_location]", record.getIdentification()
				.getLastKnownLocation());

		params.put("child[date_of_separation]", record.getIdentification()
				.getFormattedSeparationDate());

		return server.persistToServer(uri, params, "child[photo]", photoData,
				isUpdate);
	}

	private class RecordHandler extends DefaultHandler {
		private String searchCriteria;
		private ChildRecordItem records[];
		private String currentTag;
		private boolean isMatch;

		private StringBuffer value = new StringBuffer();

		private ChildRecordItem currentRecord;

		public RecordHandler(String searchCriteria) {
			this.searchCriteria = searchCriteria;

			records = new ChildRecordItem[0];
			isMatch = false;
		}

		public void characters(char[] ch, int start, int length) {
			if (currentTag.equals("name")) {
				value.append(ch, start, length);

				if ((searchCriteria.length() == 0)
						|| (value.toString().toLowerCase().indexOf(
								searchCriteria.toLowerCase()) != -1)) {
					isMatch = true;
					currentRecord = new ChildRecordItem();

					Arrays.add(records, currentRecord);

					currentRecord.setName(value.toString());
				} else {
					isMatch = false;
				}
			} else if (isMatch) {
				value.append(ch, start, length);

				String stringValue = value.toString();

				if (stringValue.trim().length() > 0) {
					if (currentTag.equals("unique-identifier")) {
						currentRecord.setRecordId(stringValue);
					} else if (currentTag.equals("id")) {
						currentRecord.setId(stringValue);
					}
				}
			}
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			currentTag = qName;

			value = new StringBuffer();
		}

		public ChildRecordItem[] getRecords() {
			return records;
		}

		public void setRecords(ChildRecordItem[] records) {
			this.records = records;
		}
	}

	private class ItemHandler extends DefaultHandler {
		private ChildRecord record;
		private String currentTag;

		private StringBuffer value = new StringBuffer();

		public ItemHandler(ChildRecordItem item) {
			record = new ChildRecord(item);
			record.setIdentification(new Identification());
			record.getIdentification().setName(item.getName());
		}

		public void characters(char[] ch, int start, int length) {

			value.append(ch, start, length);

			String stringValue = value.toString();

			if (currentTag.equals("date-of-separation")) {
				record.getIdentification().setDateOfSeparation(stringValue);
			} else if (currentTag.equals("is-age-exact")) {
				record.getIdentification().setExactAge(
						stringValue.equalsIgnoreCase("Exact"));
			} else if (currentTag.equals("gender")) {
				record.getIdentification().setMale(
						stringValue.equalsIgnoreCase("Male"));
			} else if (currentTag.equals("origin")) {
				record.getIdentification().setOrigin(stringValue);
			} else if (currentTag.equals("last-known-location")) {
				record.getIdentification().setLastKnownLocation(stringValue);
			} else if (currentTag.equals("age")) {
				record.getIdentification()
						.setAge(Integer.parseInt(stringValue));
			} else if (currentTag.equals("length")) {
				record.setPhotoLength(Integer.parseInt(stringValue));
			}
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			currentTag = qName;

			value = new StringBuffer();
		}

		public ChildRecord getRecord() {
			return record;
		}
	}
}
