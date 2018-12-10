package com.bzhang.server6;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 解析web.xml文件
 * @author bzhang
 *
 */
public class WebHandler extends DefaultHandler{

	private Map<String, String> servlet;
	
	private Map<String, String> mapping;
	
	private String key;
	
	private String value;
	
	private String key_qName;
	
	private String value_qName;
	
	private String pre_qName;

	public WebHandler(Map<String, String> servlet, Map<String, String> mapping) {
		super();
		this.servlet = servlet;
		this.mapping = mapping;
	}
	

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		key="";
		value="";
		key_qName="";
		value_qName="";
		pre_qName="";
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("servlet")||qName.equals("mapping")) {
			pre_qName=qName;
		}else if (pre_qName.equals("servlet")&&qName.equals("name")) {
			key_qName=qName;
		}else if (pre_qName.equals("servlet")&&qName.equals("servlet-name")) {
			value_qName=qName;
		}else if (pre_qName.equals("mapping")&&qName.equals("name")) {
			value_qName=qName;
		}else if (pre_qName.equals("mapping")&&qName.equals("url-pattern")) {
			key_qName=qName;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String content = new String(ch, start,length);
		if (content!=null&&!content.trim().equals("")) {
			if (pre_qName.equals("servlet")&&key_qName.equals("name")&&value_qName.equals("")) {
				key=content;
			}else if (pre_qName.equals("servlet")&&value_qName.equals("servlet-name")) {
				value=content;
			}
			
			if (pre_qName.equals("mapping")&&value_qName.equals("name")&&key_qName.equals("")) {
				value=content;
			}else if (pre_qName.equals("mapping")&&key_qName.equals("url-pattern")) {
				key=content;
			}
			if (pre_qName.equals("servlet")) {
				servlet.put(key, value);
			}else if (pre_qName.equals("mapping")) {
				mapping.put(key, value);
			}
			
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("servlet")||qName.equals("mapping")) {
			key="";
			value="";
			key_qName="";
			value_qName="";
			pre_qName="";
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
}
