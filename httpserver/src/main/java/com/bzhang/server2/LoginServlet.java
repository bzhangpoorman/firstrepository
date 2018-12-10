package com.bzhang.server2;

public class LoginServlet extends Servlet{

	@Override
	public void doGet(Request request, Response response) throws Exception {
		
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		String wife = request.getParameter("wife");
		
		response.print("wo fuck"+wife);
		
	}

}
