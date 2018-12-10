package com.bzhang.server5;

import java.util.HashMap;
import java.util.Map;

public class ServletContext {

	//关联servlet,为每个servlet取个名
	private Map<String, String> servlet;
	
	/*
	 * 为每个url映射一个servlet名
	 */
	private Map<String, String> mapping;
	
	public ServletContext() {
		servlet = new HashMap<String, String>();
		mapping = new HashMap<String, String>();
		
	}

	public Map<String, String> getServlet() {
		return servlet;
	}

	public void setServlet(Map<String, String> servlet) {
		this.servlet = servlet;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
}
