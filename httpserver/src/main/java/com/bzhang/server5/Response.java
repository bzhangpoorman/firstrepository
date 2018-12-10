package com.bzhang.server5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;

/**
 * 封装response
 * @author bzhang
 *
 */
public class Response {

	//存放响应头信息
	private StringBuilder headInfo;
	
	//正文长度
	private int contentLength;
	
	//正文信息
	private StringBuilder content;
	
	//输出缓冲区
	private ByteBuffer writeBuffer=ByteBuffer.allocate(2048);
	
	//响应头对象
	private CreateHead createHead;
	
	//更改字符集编码，默认utf-8
	private String charset="utf-8";
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Response() {
		content=new StringBuilder();
		headInfo=new StringBuilder();
		createHead=new CreateHead();
		
		
	}
	
	/**
	 * 更改content_type参数
	 * @param value
	 */
	public void setContentType(String value) {
		String[] split = value.split(":");
		if (split.length==2) {
			addHeader(split[0], split[1]);
		}
		
		return;
	}
	
	public void addHeader(String name, String value) {
		String res=name+":"+value;
		List<String> params = createHead.getParams();
		if (params.contains(res)) {
			return;
		}
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).contains(name)) {
				params.set(i, res);
				return;
			}
		}
		params.add(res);
	}
	
	/**
	 * 构建响应头
	 * @param code
	 */
	private void createResponseHead(int code) {
		headInfo = createHead.create(code);
		headInfo.append(CreateHead.CONTENTlENGTH).append(contentLength)
		.append(CreateHead.NEWLINE).append(CreateHead.NEWLINE);
	}
	
	/**
	 * 追加正文内容
	 * @param info
	 * @return
	 */
	public Response print(String info) {
		content.append(info);
		contentLength+=info.getBytes().length;
		return this;
	}
	
	/**
	 * 追加正文内容，带换行
	 * @param info
	 * @return
	 */
	public Response println(String info) {
		content.append(info).append(CreateHead.NEWLINE);
		try {
			contentLength+=(info+CreateHead.NEWLINE).getBytes(charset).length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * 输出信息到客户端浏览器
	 * @param code
	 * @param client
	 * @throws IOException 
	 */
	public void pushToClient(int code,SocketChannel client)  {
		createResponseHead(code);
		//System.out.println("bbbbbbbbb"+client.toString());
		if (client.isOpen()) {
			writeBuffer.clear();
			try {
				/*
				 * 先将响应头写出
				 */
				writeBuffer.put(headInfo.toString().getBytes(charset));
				writeBuffer.flip();
				client.write(writeBuffer);
				//System.out.println("head:"+headInfo);

				/*
				 * 正文根据打下，分批次写出，防止缓冲区容量不足导致输出错误
				 */
				byte[] output = content.toString().getBytes(charset);
				int looptime = (contentLength - 1) / writeBuffer.capacity() + 1;
				int capacity = writeBuffer.capacity();
				for (int i = 0; i < looptime; i++) {
					//System.out.println(i+"****");
					writeBuffer.clear();
					if (i == looptime - 1) {
						writeBuffer.put(output, i * capacity, contentLength - i * capacity);
					} else {
						writeBuffer.put(output, i * capacity, capacity);
					}
					writeBuffer.flip();
					client.write(writeBuffer);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		
		
	}
	
	/**
	 * 构建response响应头及响应体
	 * @author bzhang
	 *
	 */
	private class CreateHead{
		
		public static final String OK_200 = "HTTP/1.1 200 OK";
		public static final String NEWLINE = "\r\n";
		public static final String NOT_FOUND_404 = "HTTP/1.1 404 NOT FOUND";
		public static final String SERVER_ERROR_500 = "HTTP/1.1 500 SEVER ERROR";
		private String contentType = "Content-Type: text/html;charset=UTF-8";
		private String connection = "Connection: Keep-alive";
		public static final String CONTENTlENGTH = "Content-Length: ";
		private String server="server: NIOserver";
		private String date="Date: ";
		
		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getConnection() {
			return connection;
		}

		public void setConnection(String connection) {
			this.connection = connection;
		}

		
		
		private List<String> params;
		
		
		private int code;
		private StringBuilder sb;
		
		public CreateHead() {
			this.sb = new StringBuilder();
			this.params=new ArrayList<String>();
			init();
		}
		
		public StringBuilder create(int code) {
			if (code==200) {
				sb.append(OK_200);
			}else if (code==500) {
				sb.append(SERVER_ERROR_500);
			}else{
				sb.append(NOT_FOUND_404);
			}
			
			for (String param : params) {
				sb.append(NEWLINE);
				sb.append(param);
			}
			sb.append(NEWLINE);
			return sb;
		}
		
		private void init() {
			
			params.add(contentType);
			params.add(connection);
			params.add(server);
			params.add(date+new Date(System.currentTimeMillis()));
			//params.add(contentLength);
			/*sb.append(contentType);
			sb.append(NEWLINE);
			sb.append(connection);
			sb.append(NEWLINE);
			sb.append(server);
			sb.append(NEWLINE);
			sb.append(date).append(new Date(System.currentTimeMillis()));
			sb.append(NEWLINE);
			sb.append(contentLength);*/
			
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public StringBuilder getSb() {
			return sb;
		}
		public void setSb(StringBuilder sb) {
			this.sb = sb;
		}
		
		public List<String> getParams() {
			return params;
		}

		public void setParams(List<String> params) {
			this.params = params;
		}
	}
}
