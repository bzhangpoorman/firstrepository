package com.bzhang.ego.manage.service;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.pojo.TbItemParam;

public interface TbItemParamService {
	/**
	 * 分页查询规格参数
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	EasyUIDataGrid show(Integer pageNum,Integer pageSize);
	
	/**
	 * 根据规格参数ids，多选删除
	 * @param ids
	 * @return
	 */
	int deleteParams(String ids);
	
	/**
	 * 根据商品类目id查询规格参数json模板
	 * @param id
	 * @return
	 */
	TbItemParam selectByItemCatId(Long itemCatId);
	
	/**
	 * 新增规格参数信息
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	int insertItemParam(Long itemCatId,String paramData);
	
}
