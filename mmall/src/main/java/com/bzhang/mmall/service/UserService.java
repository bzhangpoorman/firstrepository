package com.bzhang.mmall.service;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.User;

public interface UserService {
	ServerResponse<User> login(String username,String password);
	ServerResponse<User> register(User user);
	ServerResponse<String> checkValid(String str,String type);
	ServerResponse<String > getQuestionByUsername(String username);
	ServerResponse<String > checkAnswer(String question,String username,String answer);
	ServerResponse<String > forgetResetPassword(String username,String passwordNew,String forgetToken);
	ServerResponse<String > loginResetPassword(String username,String password,String passwordNew);
	ServerResponse<User> updateUserInfo(User user);
	ServerResponse<String > checkAdminRole(User user);
	

}
