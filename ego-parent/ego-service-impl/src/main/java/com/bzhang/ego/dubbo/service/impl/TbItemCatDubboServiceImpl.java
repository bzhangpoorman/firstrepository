package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bzhang.ego.dubbo.service.TbItemCatDubboService;
import com.bzhang.ego.mapper.TbItemCatMapper;
import com.bzhang.ego.pojo.TbItemCat;
import com.bzhang.ego.pojo.TbItemCatExample;

public class TbItemCatDubboServiceImpl implements TbItemCatDubboService{
	@Resource
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<TbItemCat> show(Long pid) {
		TbItemCatExample tbItemCatExample=new TbItemCatExample();
		tbItemCatExample.createCriteria().andParentIdEqualTo(pid);
				
		List<TbItemCat> list = tbItemCatMapper.selectByExample(tbItemCatExample);
		return list;
	}
	
}
