package com.bzhang.ego.manage.service;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.pojo.TbContent;

public interface TbContentService {
	/**
	 * 根据categoryId，分页查询
	 * @param pageNum
	 * @param pageSize
	 * @param categoryId
	 * @return
	 */
	EasyUIDataGrid showByCategoryId(Integer pageNum,Integer pageSize,Long categoryId);
	
	/**
	 * 新增content信息
	 * @param tbContent
	 * @return
	 */
	EgoResult insertContent(TbContent tbContent);
	
	/**
	 * 根据id修改content信息
	 * @param tbContent
	 * @return
	 */
	EgoResult updateContent(TbContent tbContent);
	
	/**
	 * 根据id删除content信息
	 * @param idStr
	 * @return
	 */
	EgoResult deleteContents(String idStr) throws Exception;
}
