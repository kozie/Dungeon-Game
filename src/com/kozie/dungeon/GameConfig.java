package com.kozie.dungeon;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GameConfig {
	
	private Map<String, String> config;
	
	public GameConfig() {
		init();
	}
	
	public void init() {
		try {
			InputStream xml = GameComponent.class.getResourceAsStream("/config.xml");
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			doc.getDocumentElement().normalize();
			
			config = Collections.synchronizedMap(new HashMap<String, String>());
			readNodes(doc.getDocumentElement().getChildNodes());
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String get(String key) {
		
		if (config.containsKey(key)) {
			return config.get(key);
		}
		
		return null;
	}
	
	private void readNodes(NodeList list) {
		readNodes(list, "");
	}
	
	private void readNodes(NodeList list, String path) {
		path = path.trim();
		
		for (int i = 0; i < list.getLength(); i++) {
			
			Node node = (Node) list.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE && node.hasChildNodes()) {
				
				Element elem = (Element) node;
				String nextPath = new String();
				
				if (path.length() > 0) {
					nextPath = path + ".";
				}
				
				nextPath = nextPath + elem.getTagName();
				
				readNodes(elem.getChildNodes(), nextPath);
			} else {
				
				String val = node.getNodeValue().trim();
				
				if (!val.isEmpty()) {
					config.put(path, val);
				}
			}
		}
	}
}