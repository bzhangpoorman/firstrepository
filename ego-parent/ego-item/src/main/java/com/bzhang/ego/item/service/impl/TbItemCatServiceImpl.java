package com.bzhang.ego.item.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.dubbo.service.TbItemCatDubboService;
import com.bzhang.ego.item.pojo.PortalMenu;
import com.bzhang.ego.item.pojo.PortalMenuNode;
import com.bzhang.ego.item.service.TbItemCatService;
import com.bzhang.ego.pojo.TbItemCat;
import com.google.common.collect.Lists;

@Service
public class TbItemCatServiceImpl implements TbItemCatService{
	
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	
	@Override
	public PortalMenu showItemCatMenu() {
		List<Object> list = showAllByParentId((long) 0);
		PortalMenu portalMenu=new PortalMenu();
		portalMenu.setData(list);
		return portalMenu;
	}
	
	/**
	 * 递归查询类目，并组装结果
	 * @param pid
	 * @return
	 */
	private List<Object> showAllByParentId(Long pid) {
		List<TbItemCat> list = tbItemCatDubboServiceImpl.show(pid);
		if (CollectionUtils.isNotEmpty(list)) {
			List<Object> portalMenuNodes=Lists.newArrayList();
			for (TbItemCat tbItemCat : list) {
				if (!tbItemCat.getIsParent()) {
					portalMenuNodes.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
				}else {
					PortalMenuNode pNode=new PortalMenuNode();
					List<Object> resList = showAllByParentId(tbItemCat.getId());
					pNode.setU("/products/"+tbItemCat.getId()+".html");
					pNode.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
					pNode.setI(resList);
					portalMenuNodes.add(pNode);
				}
				
			}
			return portalMenuNodes;
		}
		return null;
		
	}

	

}
