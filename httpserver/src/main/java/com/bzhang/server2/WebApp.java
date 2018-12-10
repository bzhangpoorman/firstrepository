package com.bzhang.server2;

import java.util.Map;

public class WebApp {
	private static ServletContext servletContext;
	static {
		servletContext=new ServletContext();
		Map<String, String> mapping = servletContext.getMapping();
		mapping.put("/login", "login");
		mapping.put("/log", "login");
		mapping.put("/reg", "register");
		
		Map<String, Servlet> servlet = servletContext.getServlet();
		servlet.put("login", new LoginServlet());
		servlet.put("register", new RegisterServlet());
	}
	
	public static Servlet getServlet(String url) {
		if (url==null||url.trim().equals("")) {
			return null;
			
		}
		
		return servletContext.getServlet().get(servletContext.getMapping().get(url));
	}
}
