package com.bzhang.ego.dubbo.service;

import java.util.List;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.pojo.TbContent;

public interface TbContentDubboService {
	/**
	 * 根据categoryId查询，并分页显示content表信息
	 * @param pageNum
	 * @param pageSize
	 * @param categoryId
	 * @return
	 */
	EasyUIDataGrid showContent(Integer pageNum,Integer pageSize,Long categoryId);
	
	/**
	 * 新增content信息
	 * @param tbContent
	 * @return
	 */
	int insertContent(TbContent tbContent);
	
	/**
	 * 修改content信息
	 * @param tbContent
	 * @return
	 */
	int updateContent(TbContent tbContent);
	
	/**
	 * 根据id删除content
	 * @param ids
	 * @return
	 */
	int deleteContents(List<Long> ids) throws Exception;
	
	/**
	 * 查询最新的count个数据，并排序
	 * @param count 查询个数
	 * @param isSort 是否排序
	 * @return
	 */
	List<TbContent> selectByCount(Integer count,Boolean isSort);
}
