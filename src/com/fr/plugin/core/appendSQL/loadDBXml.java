package com.fr.plugin.core.appendSQL;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fr.general.FRLogger;
import com.fr.stable.CodeUtils;

public class loadDBXml{

	private static Map<String, String> map = new HashMap<String, String>();

//	public static void main(String argv[]) {
	public static Map<String, String> getDBConfig(){
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				
				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
					if (qName.equalsIgnoreCase("Connection")) {
						map.put("name", attributes.getValue("name"));
					}

					if (qName.equalsIgnoreCase("JDBCDatabaseAttr")) {
						map.put("url", attributes.getValue("url"));
						map.put("driver", attributes.getValue("driver"));
						map.put("user", attributes.getValue("user"));
						map.put("user", attributes.getValue("user"));
						map.put("password", CodeUtils.passwordDecode(attributes.getValue("password")));
						map.put("encryptPassword", attributes.getValue("encryptPassword"));
					}
					
				}
				
				public void endElement(String uri, String localName, String qName) throws SAXException {
//					System.out.println("End Element :" + qName);
				}

				
				public void characters(char ch[], int start, int length) throws SAXException {
//					System.out.println("Nick Name : " + new String(ch, start, length));
				}

			};
			FRLogger.getLogger().info("root dir : "+System.getProperty("user.dir"));
			saxParser.parse("../WebReport/WEB-INF/resources/dbConfig.xml", handler);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}