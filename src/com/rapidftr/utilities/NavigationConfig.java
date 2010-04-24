package com.rapidftr.utilities;

import java.io.InputStream;
import java.util.Vector;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NavigationConfig {
	private static final String XML_FILE = "/navigation.config";

	private static NavigationConfig instance;

	private Vector config;

	public static synchronized NavigationConfig getInstance() {
		if (instance == null) {
			instance = new NavigationConfig();
		}

		return instance;
	}

	private NavigationConfig() {
		try {
			load();
		} catch (Exception e) {
			System.out.println("Error loading configuration data");
		}
	}

	public Vector getConfiguration() {
		return config;
	}

	private void load() throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();

		InputStream is = this.getClass().getResourceAsStream(XML_FILE);

		Document document = documentBuilder.parse(is);

		Element root = document.getDocumentElement();

		NodeList nodes = root.getChildNodes();

		config = new Vector();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if ((node.getLocalName() != null)
					&& (node.getLocalName().equals("screen"))) {
				NamedNodeMap map = node.getAttributes();

				NavigationInfo navigationInfo = new NavigationInfo();

				config.addElement(navigationInfo);

				int screenId = Integer.parseInt(map.getNamedItem("id")
						.getNodeValue());

				navigationInfo.setScreenId(screenId);
				navigationInfo.setScreenName(map.getNamedItem("name")
						.getNodeValue());

				NodeList childNodes = node.getChildNodes();

				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);

					NavigationAction action = loadAction(childNode);

					if (action != null) {
						navigationInfo.addAction(action);
					}
				}
			}
		}
	}

	private NavigationAction loadAction(Node actionNode) {
		NavigationAction action = null;

		if ((actionNode.getLocalName() != null)
				&& (actionNode.getLocalName().equals("action"))) {
			action = new NavigationAction();

			NamedNodeMap actionNodeMap = actionNode.getAttributes();

			action.setId(Integer.parseInt(actionNodeMap.getNamedItem("id")
					.getNodeValue()));

			boolean type = actionNodeMap.getNamedItem("type").getNodeValue()
					.equals("push");

			action.setPush(type);

			Node screenIdNode = actionNodeMap.getNamedItem("screenId");
			
			if ( screenIdNode != null ) {
				action.setScreenId(Integer.parseInt(screenIdNode.getNodeValue()));
			}
			else {
				action.setScreenId(-1);
			}
		}

		return action;
	}
}
