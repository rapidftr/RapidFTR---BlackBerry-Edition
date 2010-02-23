package com.rapidftr.services.impl;

import java.io.InputStream;
import java.util.Hashtable;

import net.rim.device.api.xml.parsers.SAXParser;
import net.rim.device.api.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.rapidftr.services.LoginService;
import com.rapidftr.services.ServiceException;
import com.rapidftr.utilities.HttpServer;
import com.rapidftr.utilities.Properties;

public class LoginServiceImpl implements LoginService {
	private static LoginService instance;

	private String loggedInUser;
	private String loggedInFullName;

	public static synchronized LoginService getInstance() {
		if (instance == null) {
			instance = new LoginServiceImpl();
		}

		return instance;
	}

	private LoginServiceImpl() {
	}

	public boolean login(String userName, String password)
			throws ServiceException {
		boolean isValid = false;

		try {
			isValid = validateLogin(userName, password);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		if (isValid) {
			loggedInUser = userName.toLowerCase();

			// now, get the Authenticity Token & server-side cookie
			// and save them to the Properties
			try {
				setSessionParameters();
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
		}

		return isValid;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	
	public String getLoggedInFullName() {
		return loggedInFullName;
	}

	public void setLoggedInFullName(String loggedInFullName) {
		this.loggedInFullName = loggedInFullName;
	}

	boolean validateLogin(String userName, String password) throws Exception {
		InputStream is = HttpServer.getInstance().getFromServer("users");

		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

		LoginHandler handler = new LoginHandler(userName, password);
		
		parser.parse(is, handler);

		boolean isAuthenticated = handler.isAuthenticated();
		
		if ( isAuthenticated ) {
			loggedInFullName = handler.getFullName();
		}

		return isAuthenticated;
	}

	private void setSessionParameters() throws Exception {
		HttpServer server = HttpServer.getInstance();

		Hashtable sessionParameters = server.getSessionParameters();

		Properties properties = Properties.getInstance();

		properties
				.setAuthenticityToken((String) sessionParameters.get("token"));
		properties.setSessionCookie((String) sessionParameters.get("cookie"));
	}

	private class LoginHandler extends DefaultHandler {
		private String userName;
		private String fullName;
		private String password;
		private boolean isAuthenticated;
		private boolean isUser;

		private String currentTag;
		private StringBuffer value;

		public LoginHandler(String userName, String password) {
			this.userName = userName;
			this.password = password;

			isAuthenticated = false;
			isUser = false;
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) {

			currentTag = qName;
			value = new StringBuffer();
		}

		public void characters(char[] ch, int start, int length) {
			if (currentTag.equals("user-name")) {
				value.append(ch, start, length);

				isUser = ( value.toString().equalsIgnoreCase(userName) );
			}
			else if (currentTag.equals("password")) {
				if ( isUser ) {
					value.append(ch, start, length);
					
					isAuthenticated = ( value.toString().equals(password) );
				}
			}
			else if (currentTag.equals("full-name")) {
				if ( isUser ) {
					value.append(ch, start, length);
					
					fullName = value.toString();
				}
			}
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public boolean isAuthenticated() {
			return isAuthenticated;
		}

		public void setAuthenticated(boolean isAuthenticated) {
			this.isAuthenticated = isAuthenticated;
		}

	}
}
