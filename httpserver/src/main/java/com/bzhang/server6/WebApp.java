package com.bzhang.server6;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
	
	//已访问过的servlet对象，存放容器
	private static HashMap<String, Servlet> alreadyUseServlet;
	private static ReentrantLock lock=new ReentrantLock();
	
	//session容器
	private static Map<String , Session> sessionMap;
	
	//定时任务
	private static ScheduledThreadPoolExecutor schedlueService=(ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
	
	/**
	 * 初始化servletContext,將已有映射的url-->servlet.name-->servlet类路径保存下
	 */
	static {
		servletContext=new ServletContext();
		alreadyUseServlet=new HashMap<>();
		sessionMap=new HashMap<>();
		
		Map<String, String> mapping = servletContext.getMapping();
		Map<String, String> servlet = servletContext.getServlet();
		
		/*
		 * 解析web.xml
		 */
		SAXParserFactory factory=SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("com/bzhang/server6/web.xml")
					, new WebHandler(servlet,mapping));
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		schedlueService.scheduleAtFixedRate(new SchedlueTask(sessionMap), 5, 10, TimeUnit.SECONDS);
	}
	
	/**
	 * 新增或修改session
	 * @param session
	 */
	public static void setSession(Session session) {
		sessionMap.put(session.getId(), session);
	}
	
	/**
	 * 取出session
	 * @param id
	 * @return
	 */
	public static Session getSession(String id) {
		return sessionMap.get(id);
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
