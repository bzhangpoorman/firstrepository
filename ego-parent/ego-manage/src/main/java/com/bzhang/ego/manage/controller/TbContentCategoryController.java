package com.bzhang.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EasyUITree;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.manage.service.TbContentCategoryService;
import com.bzhang.ego.pojo.TbContentCategory;
import com.google.common.collect.Lists;

@Controller
public class TbContentCategoryController {
	@Resource
	private TbContentCategoryService tbContentCategoryServiceImpl;
	
	/**
	 * 根据父类id，查询内容分类表的子类列表
	 * @param pid
	 * @return
	 */
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUITree> show(@RequestParam(value="id", defaultValue="0")Long pid) {
		List<EasyUITree> easyUITreeList=Lists.newArrayList();
		List<TbContentCategory> list = tbContentCategoryServiceImpl.showAll(pid);
		if (CollectionUtils.isNotEmpty(list)) {
			for (TbContentCategory tbContentCategory : list) {
				EasyUITree easyUITree=new EasyUITree();
				easyUITree.setId(tbContentCategory.getId());
				easyUITree.setText(tbContentCategory.getName());
				easyUITree.setState(tbContentCategory.getIsParent()?"closed":"open");
				easyUITreeList.add(easyUITree);
			}
		}
		return easyUITreeList;
	}
	
	/**
	 * 新增内容分类信息
	 * @param newContentCategory
	 * @return
	 */
	@RequestMapping("content/category/create")
	@ResponseBody
	public EgoResult addContentCategory(TbContentCategory newContentCategory) {
		EgoResult egoResult= null;
		try {
			egoResult=tbContentCategoryServiceImpl.addContentCategory(newContentCategory);
		} catch (Exception e) {
			e.printStackTrace();
			egoResult= new EgoResult();
		}
		
		return egoResult;
	}
	
	@RequestMapping("content/category/update")
	@ResponseBody
	public EgoResult updateCategory(TbContentCategory tbContentCategory) {
		EgoResult egoResult=new EgoResult();
		int res = tbContentCategoryServiceImpl.updateContentCategory(tbContentCategory);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.OK_UPDATE_STATUS.getValue());
			
		}
		return egoResult;
	}
	
	@RequestMapping("content/category/delete")
	@ResponseBody
	public EgoResult deleteCategory(TbContentCategory tbContentCategory) {
		EgoResult egoResult=new EgoResult();
		int res = tbContentCategoryServiceImpl.deleteContentCategory(tbContentCategory);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE_STATUS.getCode());
			egoResult.setReason(EgoResultReason.OK_UPDATE_STATUS.getValue());
			
		}
		return egoResult;
	}
	
}
