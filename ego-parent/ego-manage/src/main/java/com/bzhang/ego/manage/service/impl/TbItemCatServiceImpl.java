package com.bzhang.ego.manage.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.pojo.EasyUITree;
import com.bzhang.ego.dubbo.service.TbItemCatDubboService;
import com.bzhang.ego.manage.service.TbItemCatService;
import com.bzhang.ego.pojo.TbItemCat;
import com.google.common.collect.Lists;

@Service
public class TbItemCatServiceImpl implements TbItemCatService{
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	
	@Override
	public List<EasyUITree> show(Long pid) {
		List<EasyUITree> easyUITrees=Lists.newArrayList();
		List<TbItemCat> list = tbItemCatDubboServiceImpl.show(pid);
		for (TbItemCat tbItemCat : list) {
			EasyUITree easyUITree=new EasyUITree();
			easyUITree.setId(tbItemCat.getId());
			easyUITree.setText(tbItemCat.getName());
			easyUITree.setState(tbItemCat.getIsParent()?"closed":"open");
			easyUITrees.add(easyUITree);
		}
		return easyUITrees;
	}
	
}
