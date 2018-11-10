package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.bzhang.ego.dubbo.service.TbUserDubboService;
import com.bzhang.ego.mapper.TbUserMapper;
import com.bzhang.ego.pojo.TbUser;
import com.bzhang.ego.pojo.TbUserExample;

public class TbUserDubboServiceImpl implements TbUserDubboService{
	
	@Resource
	private TbUserMapper tbUserMapper;

	@Override
	public TbUser selectByIdAndPassword(TbUser user) {
		TbUserExample example=new TbUserExample();
		example.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
		List<TbUser> list = tbUserMapper.selectByExample(example);
		
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		
		return list.get(0);
	}

	@Override
	public boolean selectByUsername(String username) {
		TbUserExample example=new TbUserExample();
		example.createCriteria().andUsernameEqualTo(username);
		
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean selectByEmail(String email) {
		TbUserExample example=new TbUserExample();
		example.createCriteria().andEmailEqualTo(email);
		
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean selectByPhone(String phone) {
		TbUserExample example=new TbUserExample();
		example.createCriteria().andPhoneEqualTo(phone);
		
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return true;
		}
		
		return false;
	}

	@Override
	public int insertUser(TbUser user) {
		return tbUserMapper.insertSelective(user);
		
		
	}

}
