package com.bzhang.server6;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * 封装request
 * @author bzhang
 *
 */
public class Request {
	private boolean isRequset;
	
	//协议
	private String protocol;
	
	//请求方式，目前仅支持get/post
	private String method;
	
	//请求url
	private String url;
	
	//请求参数存放容器
	private Map<String , List<String>> parameter;
	
	public static final String NEWLINE = "\r\n";
	
	//读缓冲区
	private ByteBuffer readBuffer=ByteBuffer.allocate(2048);
	
	//请求信息，包含头、行、正文
	private StringBuilder requestInfo;
	
	//请求行存放容器
	private Map<String , String> headerMap;
	
	//浏览器cookie存放容器
	private Map<String,Cookie> cookies;
	
	//session信息
	private Session session;
	
	//字符集编码，默认编码为utf-8
	private String charset="utf-8";
	
	/**
	 * 取得浏览器发送的所有cookie信息
	 * @return
	 */
	public Map<String, Cookie> getCookies() {
		return cookies;
	}
	
	/**
	 * 获取请求行信息
	 * @param name
	 * @return
	 */
	public String getHeader(String name) {
		return headerMap.get(name);
	}
	
	/**
	 * 获取request字符编码
	 * @return
	 */
	public String getCharset() {
		return charset;
	}


	public Request() {
		protocol="";
		method="";
		url="";
		parameter=new HashMap<String, List<String>>();
		headerMap=new HashMap<>();
		cookies=new HashMap<>();
		
		
	}
	
	public Request(SocketChannel socketChannel) {
		this();
		readData(socketChannel);
		
		
		//getSession();
	}

	/**
	 * 获取该客户端下对应的session对象，若没有则新生成一个session。
	 * @return
	 */
	public Session getSession() {
		return session;
	}
	
	/**
	 * 读取requestInfo信息
	 * @param socket
	 * @throws IOException
	 */
	private void readData(SocketChannel socket) {
		int read =0;
		requestInfo=new StringBuilder();
		if (socket.isOpen()) {
			try {
				do {
					readBuffer.clear();
					read = socket.read(readBuffer);
					readBuffer.flip();
					if (read>0) {
						System.out.println(read);
						requestInfo.append(new String(readBuffer.array(), charset));
					}
				} while (read>0);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(requestInfo.toString());
		parseRequestInfo();
	}

	/**
	 * 分析requestInfo报文
	 */
	private void parseRequestInfo() {
		if (requestInfo==null||(requestInfo.toString().trim()).equals("")) {
			return;
		}
		//请求不为空
		isRequset=true;
		
		String info=requestInfo.toString();
		//System.out.println("#######"+requestInfo);
		String infoHeader=info.substring(info.indexOf(NEWLINE),info.lastIndexOf(NEWLINE+NEWLINE));
		System.out.println(infoHeader+"____");
		//获取请求头信息
		String requestHead=info.substring(0, info.indexOf(NEWLINE));
		System.out.println("requestHead:"+requestHead);
		
		//获取请求方式  ，只支持get和post
		method=requestHead.substring(0,requestHead.indexOf(" "));
		//System.out.println("method:"+method+"**");
		
		//获取协议
		protocol=requestHead.substring(requestHead.trim().lastIndexOf(" "), requestHead.trim().length()).trim();
		//System.out.println("protocol:"+protocol+"**");
		
		//获取url
		String urlStr=requestHead.substring(requestHead.indexOf("/"),requestHead.trim().lastIndexOf(" "));
		String param="";
		if (method.equalsIgnoreCase("get")) {
			if (urlStr.contains("?")) {
				url=urlStr.substring(urlStr.indexOf("/"),urlStr.indexOf("?"));
				param=urlStr.substring(urlStr.indexOf("?")+1).trim();
				
			}else {
				url=urlStr;
			}
			
		}else if (method.equalsIgnoreCase("post")) {
			url=urlStr;
			param=info.substring(info.lastIndexOf(NEWLINE)).trim();
		}
		System.out.println("url:"+url);
		//构建head信息
		buildHeader(infoHeader);
		//构建cookie
		buildCookie(getHeader("Cookie"));
		
		if (param=="") {
			return;
		}
		buildParamMap(param);
		
		
	}

	/**
	 * 组装header信息
	 * @param infoHeader
	 */
	private void buildHeader(String infoHeader) {
		String[] h1 = infoHeader.split(NEWLINE);
		for (int i = 0; i < h1.length; i++) {
			String[] hp = h1[i].trim().split(":");
			if (hp.length==2) {
				System.out.println(h1[i]);
				headerMap.put(hp[0].trim(), hp[1].trim());
			}
			
		}
		
	}

	/**
	 * 生成cookie信息
	 * @param c
	 */
	public void buildCookie(String c) {
		if (c!=null&&!c.trim().equals("")) {
			String[] sp = c.trim().split(";");
			if(sp.length>0) {
				
				for (int i=0;i<sp.length;i++) {
					String[] ss = sp[i].trim().split("=");
					cookies.put(ss[0],new Cookie(ss[0], ss[1]));
					System.out.println(ss[0]+":"+ss[1]);
				}
				
			}
		}
		
		
		if (cookies.containsKey("SESSIONID")) {
			String value = cookies.get("SESSIONID").getValue();
			System.out.println(value+"*****");
			if (value!=null&&!value.trim().equals("")) {
				if (WebApp.getSession(value)==null) {
					session=new Session();
				}else {
					session=WebApp.getSession(value);
				}
			}
		}else {
			session=new Session();
		}
		System.out.println("session"+session.getId());
		
	}

	/**
	 * 参数生成，parameter参数创建
	 * @param param
	 */
	public void buildParamMap(String param) {
		String[] params=param.split("&");
		for (String para : params) {
			String[] sp = para.split("=");
			if (sp.length==1) {
				sp=Arrays.copyOf(sp, 2);
				sp[1]=null;
			}
			if (!parameter.containsKey(sp[0])) {
				List<String> list=new ArrayList<String>();
				list.add(decode(sp[1], charset));
				parameter.put(sp[0], list);
			}else {
				parameter.get(sp[0]).add(decode(sp[1], charset));
			}
			System.out.println("key:"+sp[0]+",value:"+sp[1]);
		}
		System.out.println(parameter);
	}
	
	/**
	 * 参数带中文导致乱码处理，统一使用utf-8编码
	 * @param value
	 * @param code
	 * @return
	 */
	public String decode(String value,String code) {
		try {
			if (value!=null) {
				return URLDecoder.decode(value,code);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 客户端参数的获取，多值参数的获取
	 * @param key
	 * @return
	 */
	public String[] getParameterValue(String key) {
		List<String> list = parameter.get(key);
		if (list==null) {
			return null;
		}
		return list.toArray(new String[0]);
	}
	
	
	/**
	 * 单值参数的获取
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		String[] values = getParameterValue(key);
		return values==null?null:values[0];
	}

	public boolean isRequset() {
		return isRequset;
	}

	public void setRequset(boolean isRequset) {
		this.isRequset = isRequset;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
