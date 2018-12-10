package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.bzhang.ego.dubbo.service.TbAdminDubboService;
import com.bzhang.ego.mapper.TbAdminMapper;
import com.bzhang.ego.pojo.TbAdmin;
import com.bzhang.ego.pojo.TbAdminExample;

public class TbAdminDubboServiceImpl implements TbAdminDubboService{

	@Resource
	private TbAdminMapper tbAdminMapper;
	@Override
	public Boolean isAdmin(TbAdmin tbAdmin) {
		TbAdminExample example=new TbAdminExample();
		example.createCriteria().andUsernameEqualTo(tbAdmin.getUsername()).andPasswordEqualTo(tbAdmin.getPassword());
		
		List<TbAdmin> list = tbAdminMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		return true;
	}

}
