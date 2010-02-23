package com.rapidftr.services.impl;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.SAXParser;
import net.rim.device.api.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
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
		try {
			// InputStream is = (new HttpServer())
			// .getAsStreamFromServer("children");

			InputStream is = this.getClass().getResourceAsStream("/joe.xml");

			// ChildRecordItem[] items = (new Parser()).parse(is);

			// String responseFromServer = (new HttpServer())
			// .getFromServer("children");
			// //
			// System.out.println("From Server: " + responseFromServer);

			// SAX
			String parserClassName = "com.sun.xml.parser.Parser";

			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			parser.parse(is, new CarHandler());
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		return localStore.retrieveMatching(searchCriteria);
	}

	public ChildRecord getRecord(String recordId) throws ServiceException {
		ChildRecord records[] = (ChildRecord[]) localStore.retrieveAll();

		ChildRecord match = null;

		for (int i = 0; i < records.length; i++) {
			if (records[i].getRecordId().equals(recordId)) {
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

		try {
			if (!persistToServer(record, record.getPhoto())) {
				throw new ServiceException("Failed to save child record");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean persistToServer(ChildRecord record, byte[] photoData)
			throws Exception {

		HttpServer server = HttpServer.getInstance();

		String token = Properties.getInstance().getAuthenticityToken();

		Hashtable params = new Hashtable();
		params.put("commit", "Create");

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

		return server.persistToServer(params, "child[photo]", photoData, token);
	}

	private class Parser {

		ChildRecordItem[] parse(InputStream is) throws Exception {
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();

			Document document = documentBuilder.parse(is);

			Element root = document.getDocumentElement();

			NodeList nodes = root.getChildNodes();

			System.out.println("Got nodes " + nodes);

			return null;
		}
	}

	private class CarHandler extends DefaultHandler {
		StringBuffer value = new StringBuffer();
		Vector cars = new Vector();

		public void characters(char[] ch, int start, int length) {
			// value.append(ch, start, len);
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			System.out.println("Got el " + qName);

			// if ("car".equals(qName)) {
			//
			// currentCar = new Car();
			// }
		}

		public void endElement(String uri, String localName, String qName) {
			// if ("car".equals(qName)) {
			// cars.addElement(currentCar);
			// currentCar = null;
			// } else if ("type".equals(qName)) {
			// currentCar.setType(value.toString());
			// value.setLength(0);
			// } else if ("year".equals(qName)) {
			// currentCar.setYear(value.toString());
			// value.setLength(0);
			// }
		}
	}
}
