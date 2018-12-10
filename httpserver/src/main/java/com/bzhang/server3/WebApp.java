package com.bzhang.server3;

import java.util.Map;

public class WebApp {
	private static ServletContext servletContext;
	static {
		servletContext=new ServletContext();
		Map<String, String> mapping = servletContext.getMapping();
		mapping.put("/login", "login");
		mapping.put("/log", "login");
		mapping.put("/reg", "register");
		mapping.put("/checklogin", "checklogin");
		
		Map<String, String> servlet = servletContext.getServlet();
		servlet.put("login", "com.bzhang.server3.LoginServlet");
		servlet.put("register", "com.bzhang.server3.RegisterServlet");
		servlet.put("checklogin", "com.bzhang.server3.CheckLoginServlet");
	}
	
	public static Servlet getServlet(String url) throws Exception, IllegalAccessException, ClassNotFoundException {
		if (url==null||url.trim().equals("")) {
			return null;
			
		}
		//通过反射创建实例并返回
		String className = servletContext.getServlet().get(servletContext.getMapping().get(url));
		
		if (className!=null&&!className.trim().equals("")) {
			return (Servlet)Class.forName(className).newInstance();
		}
		return null;
	}
}
