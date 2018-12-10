package com.bzhang.server2;

import java.util.HashMap;
import java.util.Map;

public class ServletContext {

	//关联servlet,为每个servlet取个名
	private Map<String, Servlet> servlet;
	
	/*
	 * 为每个url映射一个servlet名
	 */
	private Map<String, String> mapping;
	
	public ServletContext() {
		servlet = new HashMap<String, Servlet>();
		mapping = new HashMap<String, String>();
		
	}

	public Map<String, Servlet> getServlet() {
		return servlet;
	}

	public void setServlet(Map<String, Servlet> servlet) {
		this.servlet = servlet;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
}
