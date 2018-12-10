package com.bzhang.server4;

import com.bzhang.server4.Request;
import com.bzhang.server4.Response;

public abstract class Servlet {

	public void service(Request request, Response response) throws Exception {
		this.doGet(request, response);
		this.doPost(request, response);
	}
	
	public abstract void doGet(Request request, Response response) throws Exception;
	
	public abstract void doPost(Request request, Response response) throws Exception;
}
