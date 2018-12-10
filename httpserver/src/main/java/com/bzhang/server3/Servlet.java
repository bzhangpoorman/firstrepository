package com.bzhang.server3;

import com.bzhang.server3.Request;
import com.bzhang.server3.Response;

public abstract class Servlet {

	public void service(Request request, Response response) throws Exception {
		this.doGet(request, response);
		this.doPost(request, response);
	}
	
	public abstract void doGet(Request request, Response response) throws Exception;
	
	public abstract void doPost(Request request, Response response) throws Exception;
}
