package com.rapidftr.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class HttpServer {
	public static final String SERVER_NAME = "madeleine";

	private static final String IMAGE_MIME_TYPE = "image/jpg";

	private static final String CONNECTION_BES = ";deviceside=false";
	private static final String CONNECTION_BIS = ";XXXXXXXXXXXXXXXX";
	private static final String CONNECTION_TCPIP = ";deviceside=true";
	private static final String CONNECTION_WIFI = ";interface=wifi";

	public boolean persistToServer(Hashtable params, String photoKey,
			byte[] photoData) throws Exception {
		String imageName = "photo.jpg";

		HttpMultipartRequest req = new HttpMultipartRequest(getUrlPrefix()
				+ "children" + CONNECTION_TCPIP, params, photoKey, imageName,
				IMAGE_MIME_TYPE, photoData);

		return (req.send() != null);
	}

	public InputStream getAsStreamFromServer(String uri) throws IOException {
		String url = getUrlPrefix() + uri + CONNECTION_TCPIP;

		HttpConnection c = null;
		InputStream is = null;
		int rc;

		try {
			c = (HttpConnection) Connector.open(url);

			c.setRequestProperty("Accept", "application/xml");
			// c.setRequestProperty("Accept", "application/json");

			rc = c.getResponseCode();
			if (rc != HttpConnection.HTTP_OK) {
				throw new IOException("HTTP response code: " + rc);
			}

			is = c.openInputStream();
		} catch (Exception e) {
			if (is != null)
				is.close();
			if (c != null)
				c.close();
			
			throw new IllegalArgumentException("Not an HTTP URL");
		} 
		
		return is;
	}

	public String getFromServer(String uri) throws IOException {
		String url = getUrlPrefix() + uri + CONNECTION_TCPIP;

		String response = null;

		HttpConnection c = null;
		InputStream is = null;
		int rc;

		try {
			c = (HttpConnection) Connector.open(url);

			c.setRequestProperty("Accept", "application/xml");
			// c.setRequestProperty("Accept", "application/json");

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
		} catch (Exception e) {
			throw new IllegalArgumentException("Not an HTTP URL");
		} finally {
			if (is != null)
				is.close();
			if (c != null)
				c.close();
		}

		return response;
	}

	private String getUrlPrefix() {
		String hostName = Properties.getInstance().getHostName();
		
		return "http://" + hostName + ":3000/";
	}
}
