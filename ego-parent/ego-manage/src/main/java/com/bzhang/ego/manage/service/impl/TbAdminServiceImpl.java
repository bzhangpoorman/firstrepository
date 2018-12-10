package com.bzhang.ego.manage.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.dubbo.service.TbAdminDubboService;
import com.bzhang.ego.manage.service.TbAdminService;
import com.bzhang.ego.pojo.TbAdmin;

@Service
public class TbAdminServiceImpl implements TbAdminService{
	
	@Reference
	private TbAdminDubboService tbAdminDubboServiceImpl;
	
	@Override
	public boolean checkAdmin(TbAdmin tbAdmin) {
		return tbAdminDubboServiceImpl.isAdmin(tbAdmin);
		
	}

}
