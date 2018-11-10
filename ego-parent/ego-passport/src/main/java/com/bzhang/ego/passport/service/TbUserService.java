package com.bzhang.ego.passport.service;

import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.pojo.TbUser;

public interface TbUserService {
	/**
	 * 用户登录验证
	 * @param user
	 * @param uuid 
	 * @return
	 */
	EgoResult checkUser(TbUser user, String uuid);
	
	/**
	 * 获取用户登录信息
	 * @param token
	 * @return
	 */
	EgoResult getUser(String token);
	
	/**
	 * 安全退出
	 * @param token
	 * @return
	 */
	EgoResult logout(String token);
	
	/**
	 * 检查用户名，邮箱，手机是否可用，是否已经存在
	 * @param param
	 * @param type
	 * @return
	 */
	EgoResult checkParam(String param,int type);
	
	/**
	 * 注册新用户
	 * @param user
	 * @return
	 */
	EgoResult insertUser(TbUser user);
	
	
}
