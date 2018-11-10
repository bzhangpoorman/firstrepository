package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.bzhang.ego.dubbo.service.TbItemParamItemDubboService;
import com.bzhang.ego.mapper.TbItemParamItemMapper;
import com.bzhang.ego.pojo.TbItemParamItem;
import com.bzhang.ego.pojo.TbItemParamItemExample;


public class TbItemParamItemDubboServiceImpl implements TbItemParamItemDubboService{
	
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Override
	public TbItemParamItem selectByItemId(Long itemId) {
		TbItemParamItemExample example=new TbItemParamItemExample();
		example.createCriteria().andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

}
