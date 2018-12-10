package com.bzhang.server2;

public class RegisterServlet extends Servlet{

	@Override
	public void doGet(Request request, Response response) throws Exception {
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (!"bbcaonima".equals(username)&&!"666666".equals(password)) {
			response.print("注册成功");
		}else {
			response.print("注册失败");
		}
		
	}
	
	
}
