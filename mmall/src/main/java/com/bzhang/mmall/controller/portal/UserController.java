package com.bzhang.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.UserService;

@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private UserService userService;
	
	/**
	 * 用户登录功能
	 * @param username 用户名
	 * @param password 密码
	 * @param session
	 * @return
	 */
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session) {
		ServerResponse<User> serverResponse = userService.login(username, password);
		if (serverResponse.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
		}
		return serverResponse;
	}
	
	@RequestMapping(value="logout.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		session.invalidate();
		return ServerResponse.createBySuccess();
	}
	
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> register(User user){
		return userService.register(user);
	}
	
	@RequestMapping(value="check_valid.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return userService.checkValid(str, type);
	}
	
	@RequestMapping(value="get_user.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUser(HttpSession session){
		User user=(User) session.getAttribute(Const.CURRENT_USER);
		if (user!=null) {
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMsg("用户未登录，无法获取用户信息！");
	}
	
	@RequestMapping(value="forget_get_question.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return userService.getQuestionByUsername(username);
	}
	
	@RequestMapping(value="check_answer.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String > checkAnswer(String question,String username,String answer){
		return userService.checkAnswer(question, username, answer);
	}
	
	@RequestMapping(value="forget_reset_password.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String > forgetResetPassword(String username,String passwordNew,String forgetToken){
		return userService.forgetResetPassword(username, passwordNew, forgetToken);
	}
	
	@RequestMapping(value="login_reset_password.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String > loginResetPassword(HttpSession session,String password,String passwordNew){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorMsg("用户未登录获过期，请重新登陆！");
		}
		return userService.loginResetPassword(user.getUsername(), password, passwordNew);
	}
	
	@RequestMapping(value="update_user_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> updateUserInfo(User user,HttpSession session){
		User current_user = (User) session.getAttribute(Const.CURRENT_USER);
		if (current_user==null) {
			return ServerResponse.createByErrorMsg("用户未登录获过期，请重新登陆！");
		}
		user.setId(current_user.getId());
		user.setUsername(current_user.getUsername());
		ServerResponse<User> resp = userService.updateUserInfo(user);
		if (resp.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, resp.getData());
		}
		return resp;
	}
	
	@RequestMapping(value="get_user_info.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user =(User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，需要登陆！status=10");
		}
		return ServerResponse.createBySuccess(user);
	}
	
	
	
	
	
	
}
