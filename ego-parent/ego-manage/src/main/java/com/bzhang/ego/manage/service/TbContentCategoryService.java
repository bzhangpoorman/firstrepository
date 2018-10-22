package com.bzhang.ego.manage.service;

import java.util.List;

import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.pojo.TbContentCategory;

public interface TbContentCategoryService {
	/**
	 * 显示内容分类表目录
	 * @param pid
	 * @return
	 */
	List<TbContentCategory> showAll(Long pid);
	
	/**
	 * 新增内容分类信息，并修改父节点的isParent属性
	 * @param newContentCategory
	 * @return
	 * @throws Exception
	 */
	EgoResult addContentCategory(TbContentCategory newContentCategory) throws Exception;
	
	/**
	 * 修改内容分类中name信息
	 * @param tbContentCategory
	 * @return
	 */
	int updateContentCategory(TbContentCategory tbContentCategory);
	
	/**
	 * 修改status状态
	 * @param tbContentCategory
	 * @return
	 */
	int deleteContentCategory(TbContentCategory tbContentCategory);
}
