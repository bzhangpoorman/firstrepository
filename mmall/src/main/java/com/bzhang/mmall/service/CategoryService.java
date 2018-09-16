package com.bzhang.mmall.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Category;

public interface CategoryService {
	ServerResponse addCategory(String categoryName,Integer parentId);
	ServerResponse setCategoryNameById(String categoryName,Integer categoryId);
	ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
	ServerResponse<List<Integer>> getCategoryAndDeepChildren(Integer categoryId);
}
