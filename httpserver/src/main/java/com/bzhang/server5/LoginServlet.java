package com.bzhang.server5;

import java.util.concurrent.TimeUnit;

public class LoginServlet extends Servlet{

	@Override
	public void doGet(Request request, Response response) throws Exception {
		
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		String wife = request.getParameter("wife");
		response.setContentType("Content-Type: text/javascript;charset=UTF-8");
		//TimeUnit.SECONDS.sleep(10);
		response.print("wo fuck"+wife+"***"+this.toString()+"***"+request+"***"+response);
		
	}

}
