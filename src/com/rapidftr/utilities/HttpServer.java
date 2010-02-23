package com.rapidftr.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.xml.parsers.SAXParser;
import net.rim.device.api.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class HttpServer {
	private static final String IMAGE_MIME_TYPE = "image/jpg";

	private static final String CONNECTION_BES = ";deviceside=false";
	private static final String CONNECTION_BIS = ";XXXXXXXXXXXXXXXX";
	private static final String CONNECTION_TCPIP = ";deviceside=true";	private static final String CONNECTION_WIFI = ";interface=wifi";

	private static HttpServer instance;

	public static synchronized HttpServer getInstance() {
		if (instance == null) {
			instance = new HttpServer();
		}

		return instance;
	}
	
	private HttpServer() {}
	
	public Hashtable getSessionParameters() throws Exception {
		Hashtable output = getAsHtmlFromServer("children/new");

		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

		TokenHandler handler = new TokenHandler();
		
		InputStream is = (InputStream)output.get("response");
		output.remove("response");
		
		parser.parse(is, handler);

		String token =  handler.getToken();
		
		is.close();
	
		HttpConnection conection = (HttpConnection)output.get("connection");
		
		conection.close();
		output.remove("connection");
		
		output.put("token", token);
		
		return output;
	}

	public boolean persistToServer(Hashtable params, String photoKey,
			byte[] photoData, String token) throws Exception {
		String imageName = "photo.jpg";

		System.out.println("Create HttpMultipartRequest");
		
		System.out.println("URL = " + getUrlPrefix()
				+ "children" + getConectionSuffix());
		
		HttpMultipartRequest req = new HttpMultipartRequest(getUrlPrefix()
				+ "children" + getConectionSuffix(), params, photoKey, imageName,
				IMAGE_MIME_TYPE, photoData);

		System.out.println("Created HttpMultipartRequest - now send");
		byte[] response = req.send();

		System.out.println("resp " + new String(response));

		return (response != null);
	}

	public InputStream getAsStreamFromServer(String uri) throws IOException {
		String url = getUrlPrefix() + uri + getConectionSuffix();

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

	public Hashtable getAsHtmlFromServer(String uri) throws IOException {
		String url = getUrlPrefix() + uri + getConectionSuffix();

		Hashtable output = new Hashtable();
		
		HttpConnection c = null;
		InputStream is = null;
		int rc;

		try {
			c = (HttpConnection) Connector.open(url);

			rc = c.getResponseCode();
			if (rc != HttpConnection.HTTP_OK) {
				throw new IOException("HTTP response code: " + rc);
			}

			output.put("cookie", c.getHeaderField("Set-Cookie"));

			is = c.openInputStream();
			
			output.put("response", is);
			output.put("connection", c);
		} catch (Exception e) {
			if (is != null)
				is.close();
			if (c != null)
				c.close();

			throw new IllegalArgumentException("Not an HTTP URL");
		}

		return output;
	}

	private String getUrlPrefix() {
		String hostName = Properties.getInstance().getHostName();
		int port = Properties.getInstance().getPort();

		return "http://" + hostName + ":" + port + "/";
	}

	private String getConectionSuffix() {
		String connection = null;

		switch (Properties.getInstance().getConnectionType()) {
		case Properties.CONNECTION_BIS:
			connection = CONNECTION_BIS;
			break;
		case Properties.CONNECTION_TCPIP:
			connection = CONNECTION_TCPIP;
			break;
		case Properties.CONNECTION_WIFI:
			connection = CONNECTION_WIFI;
			break;
		}

		return connection;
	}

	private class TokenHandler extends DefaultHandler {
		private boolean isAttribute = false;
		
		String token = null;

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			if (qName.equals("input")) {
				if (attributes != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						
						System.out.println("NEXT " + attributes.getLocalName(i) + " " + attributes.getValue(i) );
						if ( (attributes.getLocalName(i).equals("name")) && (attributes.getValue(i).equals("authenticity_token"))) {
							isAttribute = true;

						}
						else if ( (attributes.getLocalName(i).equals("value")) && (isAttribute) ) {
							token = attributes.getValue(i);
							isAttribute = false;
							break;		
						}
					}
				}
			}
		}
		
		public String getToken() {
			return token;
		}
	}
}
