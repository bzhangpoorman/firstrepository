package com.bzhang.server5;

public class RegisterServlet extends Servlet{

	@Override
	public void doGet(Request request, Response response) throws Exception {
	}

	@Override
	public void doPost(Request request, Response response) throws Exception {
		
		response.print("<html>\r\n" + 
				"	<head>\r\n" + 
				"		<title>登录界面</title>\r\n" + 
				"		<script type=\"text/javascript\" language =\"javascript\" >\r\n" + 
				"			function check(){\r\n" + 
				"				var u=$(\"uname\").value;\r\n" + 
				"				var p=$(\"pwd\").value;\r\n" + 
				"				if(u==\"bzhang\"&&p==123456){\r\n" + 
				"					return true;\r\n" + 
				"				\r\n" + 
				"				}else{\r\n" + 
				"					document.getElementById(\"uname\").value=\"\";\r\n" + 
				"					document.getElementById(\"pwd\").value=\"\";\r\n" + 
				"					return false;\r\n" + 
				"\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"\r\n" + 
				"			function $(str){\r\n" + 
				"				return document.getElementById(str);\r\n" + 
				"			}\r\n" + 
				"		</script>\r\n" + 
				"	</head>\r\n" + 
				"	<body>\r\n" + 
				"		<h1>欢迎登陆</h1>\r\n" + 
				"		<form action=\"/checklogin\" method=\"post\">\r\n" + 
				"		用户名：<input type=\"text\" id=\"uname\" name=\"uname\" ><br/>\r\n" + 
				"		密&nbsp;&nbsp码：<input type=\"password\" id=\"pwd\" name=\"password\" ><br/>\r\n" + 
				"		<input type=\"submit\" value=\"登陆\" onclick=\"return check();\"/><input type=\"reset\" value=\"重新登陆\" />\r\n" + 
				"		</form>\r\n" + 
				"	</body>\r\n" + 
				"</html>");
		
	}
	
	
}
