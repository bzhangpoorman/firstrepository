package com.bzhang.ego.dubbo.service.impl;

import javax.annotation.Resource;

import com.bzhang.ego.dubbo.service.TbItemDescDubboService;
import com.bzhang.ego.mapper.TbItemDescMapper;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.pojo.TbItemDescExample;

public class TbItemDescDubboServiceImpl implements TbItemDescDubboService{
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	
	@Override
	public int insertItemDesc(TbItemDesc tbItemDesc) {
		return tbItemDescMapper.insert(tbItemDesc);
	}

	@Override
	public TbItemDesc selectByItemId(Long itemId) {
		return tbItemDescMapper.selectByPrimaryKey(itemId);
	}

}
