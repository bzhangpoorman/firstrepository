package com.bzhang.server5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装request
 * @author bzhang
 *
 */
public class Request {
	private boolean isRequset;
	
	private String protocol;

	private String method;
	
	private String url;
	
	private Map<String , List<String>> parameter;
	
	public static final String NEWLINE = "\r\n";
	
	private ByteBuffer readBuffer=ByteBuffer.allocate(2048);
	
	private StringBuilder requestInfo;
	
	//字符集编码，默认编码为utf-8
	private String charset="utf-8";
	
	
	public String getCharset() {
		return charset;
	}


	public Request() {
		protocol="";
		method="";
		url="";
		parameter=new HashMap<String, List<String>>();
		
	}
	
	public Request(SocketChannel socketChannel) {
		this();
		readData(socketChannel);
	}


	/**
	 * 读取requestInfo信息
	 * @param socket
	 * @throws IOException
	 */
	private void readData(SocketChannel socket) {
		int read =0;
		requestInfo=new StringBuilder();
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
		if (param=="") {
			return;
		}
		buildParamMap(param);
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
