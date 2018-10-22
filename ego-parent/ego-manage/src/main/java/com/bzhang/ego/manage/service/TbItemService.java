package com.bzhang.ego.manage.service;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.pojo.TbItem;

public interface TbItemService {
	/**
	 * 分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	EasyUIDataGrid show(Integer pageNum,Integer pageSize);
	
	/**
	 * 修改商品状态
	 * @param ids
	 * @param status
	 * @return
	 */
	int updateItemStatus(String ids,Byte status);
	
	/**
	 * 商品新增
	 * @param tbItem
	 * @return
	 */
	int insertItem(TbItem tbItem,String desc,String itemParams)throws Exception;
	
}
