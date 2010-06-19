package com.rapidftr.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.xml.parsers.SAXParser;
import net.rim.device.api.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class HttpServer {
	private static final String IMAGE_MIME_TYPE = "image/jpg";

	private static final int GET_IMAGE = 1;
	private static final int GET_HTML = 2;
	private static final int GET_STREAM = 3;

	private static HttpServer instance;

	private int requestTimeout;

	public static synchronized HttpServer getInstance() {
		if (instance == null) {
			instance = new HttpServer();
		}

		return instance;
	}

	public HttpServer() {
		requestTimeout = Properties.getInstance().getHttpRequestTimeout();
	}

	public Hashtable getSessionParameters() throws Exception {
		Hashtable output = getAsHtmlFromServer("children/new");

		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

		TokenHandler handler = new TokenHandler();

		InputStream is = (InputStream) output.get("response");
		output.remove("response");

		parser.parse(is, handler);

		String token = handler.getToken();

		is.close();

		HttpConnection conection = (HttpConnection) output.get("connection");

		conection.close();
		output.remove("connection");

		output.put("token", token);

		return output;
	}

	public String persistToServer(String uri, Hashtable params,
			String photoKey, byte[] photoData, boolean isUpdate)
			throws Exception {
		String imageName = "photo.jpg";

		HttpMultipartRequest req = new HttpMultipartRequest(getUrlPrefix()
				+ uri, params, photoKey, imageName, IMAGE_MIME_TYPE, photoData);

		byte[] response = req.send(isUpdate);


		// parse the HTTP response
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

		PostResponseHandler handler = new PostResponseHandler();

		parser.parse(new ByteArrayInputStream(response), handler);

		return handler.getId();
	}

	public InputStream getFromServer(String uri) throws Exception {
		String response = (String) getFromServer(uri, GET_STREAM);

		response = transformResponse(response);

		return new ByteArrayInputStream(response.getBytes());
	}

	private String readResponse(InputStream is, int len) throws IOException {
		String response = null;

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

		return response;
	}

	private String transformResponse(String response) {
		Hashtable tags = new Hashtable();

		tags.put("<-id>", "<id>");
		tags.put("</-id>", "</id>");
		tags.put("<-rev>", "<rev>");
		tags.put("</-rev>", "</rev>");
		tags.put("<-attachments>", "<attachments>");
		tags.put("</-attachments>", "</attachments>");

		return replaceTags(response, tags);
	}

	private String replaceTags(String response, Hashtable tags) {
		Enumeration e = tags.keys();

		while (e.hasMoreElements()) {
			String oldTag = (String) e.nextElement();
			String newTag = (String) tags.get(oldTag);

			int ind;

			while ((ind = response.indexOf(oldTag)) != -1) {
				response = response.substring(0, ind) + newTag
						+ response.substring(ind + oldTag.length());
			}
		}

		return response;
	}

	public byte[] getImageFromServer(String uri) throws Exception,
			NoMoreTransportsException {

		return (byte[]) getFromServer(uri, GET_IMAGE);
	}

	public Hashtable getAsHtmlFromServer(String uri) throws Exception {
		return (Hashtable) getFromServer(uri, GET_HTML);
	}

	public Object getFromServer(String uri, int type) throws Exception {
		String url = getUrlPrefix() + uri + ";ConnectionTimeout="
				+ requestTimeout;

		Object output = null;

		HttpConnection connection = null;
		InputStream is = null;

		HttpConnectionFactory factory = new HttpConnectionFactory(url,
				HttpConnectionFactory.TRANSPORT_WIFI
						| HttpConnectionFactory.TRANSPORT_BIS
						| HttpConnectionFactory.TRANSPORT_DIRECT_TCP
						| HttpConnectionFactory.TRANSPORTS_ANY);

		while (true) {
			try {
				connection = factory.getNextConnection();

				connection.setRequestMethod("GET");

				switch (type) {
				case GET_IMAGE:
					connection.setRequestProperty("Accept", "image/jpeg");
					break;
				case GET_HTML:
					break;
				case GET_STREAM:
					connection.setRequestProperty("Accept", "application/xml");
					break;
				}

				int rc = connection.getResponseCode();

				if (rc == HttpConnection.HTTP_OK) {
					is = connection.openInputStream();

					switch (type) {
					case GET_IMAGE:
						output = handleImageResponse(connection, is);
						break;
					case GET_HTML:
						output = handleHtmlResponse(connection, is);
						break;
					case GET_STREAM:
						output = handleStreamResponse(connection, is);
						break;
					}

					break;
				}
			} catch (Exception e) {
				if (is != null)
					is.close();
				if (connection != null)
					connection.close();

				throw e;
			}
		}

		return output;
	}

	private Object handleImageResponse(HttpConnection connection, InputStream is)
			throws IOException {
		byte[] response = null;

		// Get the length and process the data
		int len = (int) connection.getLength();

		response = new byte[len];

		is.read(response, 0, len);

		is.close();
		connection.close();

		return response;
	}

	private Object handleStreamResponse(HttpConnection connection,
			InputStream is) throws IOException {

		// Get the length and process the data
		int len = (int) connection.getLength();

		String response = readResponse(is, len);

		is.close();
		connection.close();

		return response;
	}

	private Object handleHtmlResponse(HttpConnection connection, InputStream is)
			throws IOException {
		Hashtable output = new Hashtable();

		output.put("cookie", connection.getHeaderField("Set-Cookie"));

		output.put("response", is);
		output.put("connection", connection);

		return output;
	}

	private String getUrlPrefix() {
		String hostName = Properties.getInstance().getHostName();
		int port = Properties.getInstance().getPort();

		return "http://" + hostName + ":" + port + "/";
	}

	private class TokenHandler extends DefaultHandler {
		private boolean isAttribute = false;

		String token = null;

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			if (qName.equals("input")) {
				if (attributes != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						if ((attributes.getLocalName(i).equals("name"))
								&& (attributes.getValue(i)
										.equals("authenticity_token"))) {
							isAttribute = true;

						} else if ((attributes.getLocalName(i).equals("value"))
								&& (isAttribute)) {
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

	private class PostResponseHandler extends DefaultHandler {
		String id = null;

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			if (qName.equals("a")) {
				if (attributes != null) {
					String value = attributes.getValue("href");

					int index = value.lastIndexOf('/');

					if (index != -1) {
						id = value.substring(index + 1);
					}
				}
			}
		}

		public String getId() {
			return id;
		}

	}
}
