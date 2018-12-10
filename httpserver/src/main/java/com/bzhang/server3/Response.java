package com.bzhang.server3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
	
	public Response() {
		content=new StringBuilder();
		
		
	}
	
	/**
	 * 构建响应头
	 * @param code
	 */
	private void createResponseHead(int code) {
		headInfo=ResponseHead.getHead(code);
		headInfo.append(contentLength).append(ResponseHead.NEWLINE).append(ResponseHead.NEWLINE);
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
		content.append(info).append(ResponseHead.NEWLINE);
		try {
			contentLength+=(info+ResponseHead.NEWLINE).getBytes("utf-8").length;
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
		writeBuffer.clear();

		try {
			/*
			 * 先将响应头写出
			 */
			writeBuffer.put(headInfo.toString().getBytes("utf-8"));
			writeBuffer.flip();
			client.write(writeBuffer);
			System.out.println("head:"+headInfo);

			/*
			 * 正文根据打下，分批次写出，防止缓冲区容量不足导致输出错误
			 */
			byte[] output = content.toString().getBytes("utf-8");
			int looptime = (contentLength - 1) / writeBuffer.capacity() + 1;
			int capacity = writeBuffer.capacity();
			for (int i = 0; i < looptime; i++) {
				System.out.println(i+"****");
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
