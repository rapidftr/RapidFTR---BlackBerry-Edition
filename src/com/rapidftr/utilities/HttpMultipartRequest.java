package com.rapidftr.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

public class HttpMultipartRequest {

	static final String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";

	byte[] postBytes = null;
	String url = null;
	String cookie;

	public HttpMultipartRequest(String url, Hashtable params, String fileField,
			String fileName, String fileType, byte[] fileBytes)
			throws Exception {
		this.url = url + ";ConnectionTimeout="
				+ Properties.getInstance().getHttpRequestTimeout();

		this.cookie = Properties.getInstance().getSessionCookie();

		String boundary = getBoundaryString();

		String boundaryMessage = getBoundaryMessage(boundary, params,
				fileField, fileName, fileType);

		String endBoundary = "\r\n--" + boundary + "--\r\n";

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		bos.write(boundaryMessage.getBytes());

		bos.write(fileBytes);

		bos.write(endBoundary.getBytes());

		this.postBytes = bos.toByteArray();

		bos.close();
	}

	String getBoundaryString() {
		return BOUNDARY;
	}

	String getBoundaryMessage(String boundary, Hashtable params,
			String fileField, String fileName, String fileType) {
		StringBuffer res = new StringBuffer("--").append(boundary).append(
				"\r\n");

		Enumeration keys = params.keys();

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) params.get(key);

			res.append("Content-Disposition: form-data; name=\"").append(key)
					.append("\"\r\n").append("\r\n").append(value).append(
							"\r\n").append("--").append(boundary)
					.append("\r\n");
		}
		res.append("Content-Disposition: form-data; name=\"").append(fileField)
				.append("\"; filename=\"").append(fileName).append("\"\r\n")
				.append("Content-Type: ").append(fileType).append("\r\n\r\n");

		return res.toString();
	}

	public byte[] send(boolean isUpdate) throws Exception {
		HttpConnection connection = null;

		InputStream is = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] res = null;

		HttpConnectionFactory factory = new HttpConnectionFactory(url,
				HttpConnectionFactory.TRANSPORT_WIFI
						| HttpConnectionFactory.TRANSPORT_BIS
						| HttpConnectionFactory.TRANSPORT_DIRECT_TCP);

		try {
			while (true) {
				connection = factory.getNextConnection();

				try {
					res = handlePost(connection, isUpdate);

					if (res != null) {
						break;
					}
				} catch (IOException e) {
					System.out.println("IO Exc " + e);
				}
			}
		} catch (Exception e) {
			System.out.println("EXCEPTION " + e);
			throw e;
		} finally {
			try {
				if (bos != null)
					bos.close();

				if (is != null)
					is.close();

				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return res;
	}

	private byte[] handlePost(HttpConnection connection, boolean isUpdate)
			throws IOException {
		byte[] res = null;

		connection.setRequestProperty("cookie", cookie);

		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + getBoundaryString());

//		connection.setRequestProperty("Accept", "application/xml");
		
		String requestMethod = (isUpdate) ? "PUT" : HttpConnection.POST;

		connection.setRequestMethod(requestMethod);

		OutputStream dout = connection.openOutputStream();

		dout.write(postBytes);

		dout.close();

		int rc = connection.getResponseCode();

		System.out.println("RC " + rc);

//		if (rc == HttpConnection.HTTP_CREATED) {
		if (rc == HttpConnection.HTTP_MOVED_TEMP) {
			res = getResponse(connection);
		}

		return res;
	}

	private byte[] getResponse(HttpConnection connection) throws IOException {
		InputStream is = connection.openInputStream();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int ch;

		while ((ch = is.read()) != -1) {
			bos.write(ch);
		}

		return bos.toByteArray();
	}
}
