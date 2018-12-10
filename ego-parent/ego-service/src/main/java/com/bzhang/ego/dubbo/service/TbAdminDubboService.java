package com.bzhang.ego.dubbo.service;

import com.bzhang.ego.pojo.TbAdmin;

public interface TbAdminDubboService {
	
	/**
	 * 验证是否是管理员用户
	 * @param tbAdmin
	 * @return
	 */
	Boolean isAdmin(TbAdmin tbAdmin);
}
