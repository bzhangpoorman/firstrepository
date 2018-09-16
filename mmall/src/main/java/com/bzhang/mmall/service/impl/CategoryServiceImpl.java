package com.bzhang.mmall.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.dao.CategoryMapper;
import com.bzhang.mmall.pojo.Category;
import com.bzhang.mmall.service.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
	private Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public ServerResponse addCategory(String categoryName, Integer parentId) {
		if (parentId==null||StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMsg("添加品类，参数错误");
		}
		
		Category category=new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);
		int count = categoryMapper.insert(category);
		if (count>0) {
			return ServerResponse.createBySuccessMsg("创建目录成功");
		}
		return ServerResponse.createByErrorMsg("创建失败");
	}

	@Override
	public ServerResponse setCategoryNameById(String categoryName, Integer categoryId) {
		if (categoryId==null||StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMsg("更新品类参数错误");
		}
		int count = categoryMapper.updateCategoryNameById(categoryName, categoryId);
		if (count>0) {
			return ServerResponse.createBySuccessMsg("修改品类名称成功");
		}
		return ServerResponse.createByErrorMsg("修改失败，品类不存在");
	}

	@Override
	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
		if (categoryId==null) {
			return ServerResponse.createByErrorMsg("更新品类参数错误");
		}
		List<Category> list = categoryMapper.selectChildrenParallelById(categoryId);
		if (CollectionUtils.isEmpty(list)) {
			logger.info("未找到当前分类的子类");
		}
		
		return ServerResponse.createBySuccess(list);
	}

	@Override
	public ServerResponse<List<Integer>> getCategoryAndDeepChildren(Integer categoryId) {
		Set<Category> categorySet = findChildrenCategory(Sets.newHashSet(), categoryId);
		if (CollectionUtils.isEmpty(categorySet)) {
			logger.info("未找到当前分类及子类");
		}
		List<Integer> list=Lists.newArrayList();
		if (categoryId!=null) {
			for (Category category : categorySet) {
				list.add(category.getId());
			}
		}
		return ServerResponse.createBySuccess(list);
		
	}
	
	private Set<Category> findChildrenCategory(Set<Category> categorySet,Integer categoryId){
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category!=null) {
			categorySet.add(category);
		}
		List<Category> list=categoryMapper.selectChildrenParallelById(categoryId);
		for (Category c : list) {
			findChildrenCategory(categorySet, c.getId());
		}
		return categorySet;
	}

}
