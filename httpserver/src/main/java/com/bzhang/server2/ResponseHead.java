package com.bzhang.server2;

import java.util.Date;

public class ResponseHead {

	public static final String OK_200 = "HTTP/1.1 200 OK";
	public static final String NEWLINE = "\r\n";
	public static final String NOT_FOUND_404 = "HTTP/1.1 404 Not Find";
	public static final String SERVER_ERROR_500 = "HTTP/1.1 500 Internal Server Error";
	public static final String CONTENT_TYPE = "Content-Type: text/plain;charset=UTF-8";
	public static final String CONNECTION = "Connection: Keep-alive";
	public static final String CONTENT_LENGTH = "Content-Length: ";
	public static final String SERVER="Server: NIOServer";
	public static final String DATE="Date: ";
	

	public static StringBuilder getHead(Integer status) {
		StringBuilder sb=new StringBuilder();
		if (status.equals(200)) {
			sb.append(OK_200);
		}else if (status.equals(500)) {
			sb.append(SERVER_ERROR_500);
		}else{
			sb.append(NOT_FOUND_404);
		}
		sb.append(NEWLINE);
		sb.append(CONTENT_TYPE);
		sb.append(NEWLINE);
		sb.append(CONNECTION);
		sb.append(NEWLINE);
		sb.append(SERVER);
		sb.append(NEWLINE);
		sb.append(DATE).append(new Date(System.currentTimeMillis()));
		sb.append(NEWLINE);
		sb.append(CONTENT_LENGTH);
		return sb;
	}
	
}
