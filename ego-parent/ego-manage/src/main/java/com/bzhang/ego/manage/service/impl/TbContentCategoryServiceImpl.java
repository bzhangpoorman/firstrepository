package com.bzhang.ego.manage.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.IDUtils;
import com.bzhang.ego.dubbo.service.TbContentCategoryDubboService;
import com.bzhang.ego.manage.service.TbContentCategoryService;
import com.bzhang.ego.pojo.TbContentCategory;
import com.google.common.collect.Maps;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
	@Reference
	private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;

	@Override
	public List<TbContentCategory> showAll(Long pid) {
		return tbContentCategoryDubboServiceImpl.selectAll(pid);
	}

	@Override
	public EgoResult addContentCategory(TbContentCategory newContentCategory) throws Exception {
		EgoResult egoResult =new EgoResult();
		if (checkName(newContentCategory.getParentId(), newContentCategory.getName())) {
			return egoResult;
		}
		
		Date date =new Date();
		Long id = IDUtils.genItemId();
		newContentCategory.setId(id);
		newContentCategory.setUpdated(date);
		newContentCategory.setCreated(date);
		newContentCategory.setIsParent(false);
		newContentCategory.setSortOrder(1);
		newContentCategory.setStatus(1);
		int res = tbContentCategoryDubboServiceImpl.insertAndUpdateContentCategory(newContentCategory);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_INSERT.getCode());
			egoResult.setReason(EgoResultReason.OK_INSERT.getValue());
			Map<String, Long> map=Maps.newHashMap();
			map.put("id", id);
			egoResult.setData(map);
			
		}
		return egoResult;
	}
	
	/**
	 * 查询名称name在数据库中是否已存在
	 * @param pid
	 * @param name
	 * @return
	 */
	private boolean checkName(Long pid,String name) {
		List<TbContentCategory> all = tbContentCategoryDubboServiceImpl.selectAll(pid);
		for (TbContentCategory tbContentCategory : all) {
			boolean oper = StringUtils.equals(name, tbContentCategory.getName());
			if (oper==true) {
				return true;
			}
			
		}
		return false;
	}
	
	@Override
	public int updateContentCategory(TbContentCategory tbContentCategory) {
		Long pid = tbContentCategoryDubboServiceImpl.selectParentIdById(tbContentCategory.getId());
		if (checkName(pid, tbContentCategory.getName())) {
			return 0;
		}
		Date updated = new Date();
		tbContentCategory.setUpdated(updated);
		return tbContentCategoryDubboServiceImpl.updateContentCategory(tbContentCategory);
	}

	@Override
	public int deleteContentCategory(TbContentCategory tbContentCategory) {
		//Long pid = tbContentCategoryDubboServiceImpl.selectParentIdById(tbContentCategory.getId());
		
		Date updated=new Date();
		tbContentCategory.setStatus(2);
		tbContentCategory.setUpdated(updated);
		int res = 1;
		res=tbContentCategoryDubboServiceImpl.updateContentCategory(tbContentCategory);
	
		
		List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selectAll(tbContentCategory.getParentId());
		
		if (CollectionUtils.isEmpty(list)) {
			TbContentCategory parent=new TbContentCategory();
			parent.setId(tbContentCategory.getParentId());
			parent.setUpdated(updated);
			parent.setIsParent(false);
			res += tbContentCategoryDubboServiceImpl.updateContentCategory(parent);
			if (res==1) {
				return 0;
			}
		}
		return 1;
	}
	
	
}
