package com.bzhang.ego.dubbo.service;

import java.util.List;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.pojo.TbItemParam;

public interface TbItemParamDubboService {
	/**
	 * 规格参数分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	EasyUIDataGrid show(Integer pageNum,Integer pageSize);
	
	/**
	 * 根据规格参数id删除数据
	 * @param ids
	 * @return
	 */
	int deleteById(Long id);
	
	/**
	 * 批量删除规格参数
	 * @param ids
	 * @return
	 */
	int deleteByIds(String ids);
	
	/**
	 * 根据类目id查询规格参数
	 * @param itemCatId
	 * @return
	 */
	TbItemParam selectByItemCatId(Long itemCatId );
	
	/**
	 * 新增规格参数信息
	 * @param tbItemParam
	 * @return
	 */
	int insertItemParam(TbItemParam tbItemParam);
}
