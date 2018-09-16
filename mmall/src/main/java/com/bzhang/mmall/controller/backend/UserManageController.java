package com.bzhang.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.UserService;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

	@Autowired 
	private UserService userService;
	
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session) {
		ServerResponse<User> serverResponse = userService.login(username, password);
		if (serverResponse.isSuccess()) {
			User user = serverResponse.getData();
			if (user.getRole()==Const.Role.ROLE_ADMIN) {
				session.setAttribute(Const.CURRENT_USER, user);
				return serverResponse;
			}else {
				return ServerResponse.createByErrorMsg("该用户不是管理员，无法登陆！");
			}
		}
		return serverResponse;
	}
}
