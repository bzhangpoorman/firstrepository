package com.bzhang.ego.dubbo.service;

import com.bzhang.ego.pojo.TbUser;

public interface TbUserDubboService {
	/**
	 * 根据用户名和密码验证用户
	 * @param user
	 * @return
	 */
	TbUser selectByIdAndPassword(TbUser user);
	
	/**
	 * 检查用户名是否已经存在
	 * @param username
	 * @return
	 */
	boolean selectByUsername(String username);
	
	/**
	 * 检查邮箱是否已存在
	 * @param email
	 * @return
	 */
	boolean selectByEmail(String email);
	
	/**
	 * 检查手机号码是否已经存在
	 * @param phone
	 * @return
	 */
	boolean selectByPhone(String phone);
	
	/**
	 * 注册新用户
	 * @param user
	 * @return
	 */
	int insertUser(TbUser user);
	
	
}
