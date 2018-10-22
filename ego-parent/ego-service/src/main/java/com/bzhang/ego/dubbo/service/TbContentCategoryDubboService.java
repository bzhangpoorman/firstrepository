package com.bzhang.ego.dubbo.service;

import java.util.List;

import com.bzhang.ego.pojo.TbContentCategory;

public interface TbContentCategoryDubboService {
	/**
	 * 根据pid查询内容分类表所有子目录
	 * @param pid
	 * @return
	 */
	List<TbContentCategory> selectAll(Long pid);
	
	/**
	 * 新增内容分类子节点，及更新父节点信息
	 * @param parentContentCategory
	 * @param newContentCategory
	 * @return
	 */
	int insertAndUpdateContentCategory(TbContentCategory newContentCategory) throws Exception;
	
	/**
	 * 修改内容分类信息
	 * @param tbContentCategory
	 * @return
	 */
	int updateContentCategory(TbContentCategory tbContentCategory);
	
	/**
	 * 根据id查父节点id
	 * @param id
	 * @return
	 */
	Long selectParentIdById(Long id);
	
}
