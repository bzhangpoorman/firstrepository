package com.bzhang.server5;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * web容器
 * @author bzhang
 *
 */
public class WebApp {
	private static ServletContext servletContext;
	private static HashMap<String, Servlet> alreadyUseServlet;
	private static ReentrantLock lock=new ReentrantLock();
	
	/**
	 * 初始化servletContext,將已有映射的url-->servlet.name-->servlet类路径保存下
	 */
	static {
		servletContext=new ServletContext();
		alreadyUseServlet=new HashMap<>();
		Map<String, String> mapping = servletContext.getMapping();
		/*mapping.put("/login", "login");
		mapping.put("/log", "login");
		mapping.put("/reg", "register");
		mapping.put("/checklogin", "checklogin");*/
		
		Map<String, String> servlet = servletContext.getServlet();
		/*servlet.put("login", "com.bzhang.server3.LoginServlet");
		servlet.put("register", "com.bzhang.server3.RegisterServlet");
		servlet.put("checklogin", "com.bzhang.server3.CheckLoginServlet");*/
		
		SAXParserFactory factory=SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("com/bzhang/server5/web.xml")
					, new WebHandler(servlet,mapping));
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据url获取servlet
	 * @param url
	 * @return
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Servlet getServlet(String url) throws Exception, IllegalAccessException, ClassNotFoundException {
		if (url==null||url.trim().equals("")) {
			return null;
			
		}
		//通过反射创建实例并返回
		String className = servletContext.getServlet().get(servletContext.getMapping().get(url));
		
		if (className!=null&&!className.trim().equals("")) {
			lock.lock();
			try {
				if (alreadyUseServlet.containsKey(className)) {
					System.out.println(alreadyUseServlet.get(className) + "!!caonima!!");
					return alreadyUseServlet.get(className);
				} else {
					Servlet servlet = (Servlet) Class.forName(className).newInstance();
					alreadyUseServlet.put(className, servlet);
					System.out.println(servlet);
					return servlet;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
			
			
		}
		return null;
	}
}
