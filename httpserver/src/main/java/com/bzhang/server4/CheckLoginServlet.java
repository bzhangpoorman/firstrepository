package com.bzhang.server4;

public class CheckLoginServlet extends Servlet{

	@Override
	public void doGet(Request request, Response response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		String username = request.getParameter("uname");
		String password = request.getParameter("password");
		if (username!=null&&!username.trim().equals("")) {
			if (password!=null&&!password.trim().equals("")) {
				response.print("注册成功");
				return;
			}
		}
		response.print("注册失败");
		
	}

}
