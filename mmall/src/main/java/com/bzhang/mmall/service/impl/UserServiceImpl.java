package com.bzhang.mmall.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.common.TokenCache;
import com.bzhang.mmall.dao.UserMapper;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.UserService;
import com.bzhang.mmall.util.MD5Util;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if(resultCount==0) {
			return ServerResponse.createByErrorMsg("用户名不存在！");
		}
		//todo 密码加密登录
		String md5Pwd=MD5Util.MD5EncodeUtf8(password);
		
		User user = userMapper.selectLogin(username, md5Pwd);
		if (user==null) {
			return ServerResponse.createByErrorMsg("密码错误！");
		}
		
		user.setPassword(StringUtils.EMPTY);
		
		return ServerResponse.createBySuccess("登录成功",user);
	}

	@Override
	public ServerResponse<User> register(User user) {

		ServerResponse checkValid = this.checkValid(user.getUsername(), Const.USERNAME);
		if (!checkValid.isSuccess()) {
			return checkValid;
		}
		checkValid=this.checkValid(user.getEmail(),Const.EMAIL);
		if (!checkValid.isSuccess()) {
			return checkValid;
		}
		user.setRole(Const.Role.ROLE_CUSTOMER);
		
		//MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		
		int insert = userMapper.insert(user);
		
		if (insert==0) {
			return ServerResponse.createByErrorMsg("注册失败");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("注册成功",user);
	}

	@Override
	public ServerResponse<String> checkValid(String str, String type) {
		if (StringUtils.isNoneBlank(type)) {
			if (Const.USERNAME.equals(type)) {
				int resultCount = userMapper.checkUsername(str);
				if(resultCount>0) {
					return ServerResponse.createByErrorMsg("用户名已存在！");
				}
			}
			if (Const.EMAIL.equals(type)) {
				int resultCount = userMapper.checkEmail(str);
				if(resultCount>0) {
					return ServerResponse.createByErrorMsg("邮箱已存在！");
				}
			}
		}else {
			return ServerResponse.createByErrorMsg("参数错误！");
		}
		return ServerResponse.createBySuccessMsg("校验成功！");
	}

	@Override
	public ServerResponse<String> getQuestionByUsername(String username) {
		int count=userMapper.checkUsername(username);
		if (count==0) {
			return ServerResponse.createByErrorMsg("不存在该用户！");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if (StringUtils.isNoneBlank(question)) {
			return ServerResponse.createBySuccessMsg(question);
		}
		return ServerResponse.createByErrorMsg("密码提示问题为空！");
	}

	@Override
	public ServerResponse<String> checkAnswer(String question,String username,String answer) {

		int count=userMapper.checkAnswer(question, username, answer);
		if (count>0) {
			String forgetToken=UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMsg("答案不正确！");
	}

	@Override
	public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
		if (StringUtils.isBlank(forgetToken)) {
			return ServerResponse.createByErrorMsg("参数错误，token不能为空！");
		}
		int count=userMapper.checkUsername(username);
		if (count==0) {
			return ServerResponse.createByErrorMsg("不存在该用户！");
		}
		String token=TokenCache.getValue(TokenCache.TOKEN_PREFIX+username);
		if (StringUtils.equals(forgetToken, token)) {
			String md5Password=MD5Util.MD5EncodeUtf8(passwordNew);
			int rowCount=userMapper.updatePasswordByUsername(username, md5Password);
			if (rowCount==1) {
				return ServerResponse.createBySuccessMsg("修改密码成功！");
			}
		}else {
			return  ServerResponse.createByErrorMsg("参数错误，请重新获取重置密码的token！");
		}
		return ServerResponse.createByErrorMsg("修改密码失败！");
		
	}

	@Override
	public ServerResponse<String> loginResetPassword(String username,String password, String passwordNew) {
		
		String md5Password=MD5Util.MD5EncodeUtf8(password);
		String md5PasswordNew=MD5Util.MD5EncodeUtf8(passwordNew);
		int count=userMapper.selectByUsernamePassword(username, md5Password);
		if (count==0) {
			return ServerResponse.createByErrorMsg("旧密码错误！");
		}
		int rowCount = userMapper.updatePasswordByUsernamePassword(username, md5Password, md5PasswordNew);
		if (rowCount>0) {
			return ServerResponse.createBySuccessMsg("修改密码成功！");
		}
		return ServerResponse.createByErrorMsg("修改密码失败！");
	}

	@Override
	public ServerResponse<User> updateUserInfo(User user) {
		
		int checkCount=userMapper.checkEmailById(user.getId(), user.getEmail());
		if (checkCount>0) {
			return ServerResponse.createByErrorMsg("email已被使用！");
		}
		//user.setUsername(null);
		user.setCreateTime(null);
		user.setPassword(null);
		user.setRole(null);
		user.setUpdateTime(null);
		int count = userMapper.updateByPrimaryKeySelective(user);
		if (count>0) {
			user=userMapper.selectByPrimaryKey(user.getId());
			user.setPassword(StringUtils.EMPTY);
			return ServerResponse.createBySuccess("更新成功", user);
		}
		return ServerResponse.createByErrorMsg("更新失败");
	}

	/**
	 * 校验用户权限
	 */
	@Override
	public ServerResponse<String> checkAdminRole(User user) {
		if (user.getRole() == Const.Role.ROLE_ADMIN) {
			return ServerResponse.createBySuccess();

		} else {
			return ServerResponse.createByErrorMsg("该用户不是管理员！");
		}
		
	}
	
	
}
