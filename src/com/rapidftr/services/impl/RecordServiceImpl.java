package com.rapidftr.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

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
/**
		try {
	
//	          case CONNECTION_BES:
//	            url = url + ";deviceside=false";
//	            break;
//	          case CONNECTION_BIS:
//	            url = url + ";XXXXXXXXXXXXXXXX";
//	            break;
//	          case CONNECTION_TCPIP:
//	            url = url + ";deviceside=true";
//	            break;
//	          case CONNECTION_WIFI:
//	            url = url + ";interface=wifi";
	        

//			String resp = getViaHttpConnection("http://proximobus.appspot.com/agencies/sf-muni.js;deviceside=true;apn=wap.gprs.unifon.com.ar;t...");
			String resp = getViaHttpConnection("http://madeleine:3000/children/31bf2c074aa06488a3fb7b243328ade2;interface=wifi");
			
//			o "https://www.blackberry.com/go/mobile/samplehttps.shtml;deviceside=true;apn=wap.gprs.unifon.com.ar;t..."
			System.out.println("HTTP RESP " + resp);
		} catch (Exception e) {
			System.out.println("HTTP ERR " + e);
			throw new ServiceException(e.getMessage());
		}
**/
	}

	String getViaHttpConnection(String url) throws IOException {
		String response = null;

		HttpConnection c = null;
		InputStream is = null;
		int rc;

		try {
			c = (HttpConnection) Connector.open(url);

			// Getting the response code will open the connection,
			// send the request, and read the HTTP response headers.
			// The headers are stored until requested.
			rc = c.getResponseCode();
			if (rc != HttpConnection.HTTP_OK) {
				throw new IOException("HTTP response code: " + rc);
			}

			is = c.openInputStream();

			// Get the ContentType
			String type = c.getType();

			// Get the length and process the data
			int len = (int) c.getLength();
			if (len > 0) {
				int actual = 0;
				int bytesread = 0;
				byte[] data = new byte[len];
				while ((bytesread != len) && (actual != -1)) {
					actual = is.read(data, bytesread, len - bytesread);
					bytesread += actual;
				}

				response = new String(data);
			}

			// else {
			// int ch;
			// while ((ch = is.read()) != -1) {
			// ...
			// }
			// }

		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Not an HTTP URL");
		} finally {
			if (is != null)
				is.close();
			if (c != null)
				c.close();
		}

		return response;
	}

}
