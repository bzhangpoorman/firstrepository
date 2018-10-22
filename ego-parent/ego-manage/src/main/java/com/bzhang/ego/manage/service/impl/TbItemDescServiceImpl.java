package com.bzhang.ego.manage.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.dubbo.service.TbItemDescDubboService;
import com.bzhang.ego.manage.service.TbItemDescService;
import com.bzhang.ego.pojo.TbItemDesc;

@Service
public class TbItemDescServiceImpl implements TbItemDescService{
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;

	@Override
	public int insertItemDesc(TbItemDesc tbItemDesc) {
		return tbItemDescDubboServiceImpl.insertItemDesc(tbItemDesc);
	}
	
	
}
