package com.bzhang.ego.dubbo.service;

import java.util.List;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.pojo.TbItemParamItem;

public interface TbItemDubboService {
	/**
	 * 商品分页查询
	 * @param pageNum 当前页
	 * @param pageSize 每页商品数
	 * @return
	 */
	EasyUIDataGrid show(Integer pageNum,Integer pageSize);
	
	/**
	 * 商品的上、下架、删除状态的修改
	 * @param id
	 * @param status
	 * @return
	 */
	int updateItemStatus(TbItem tbItem);
	
	/**
	 * 新增商品
	 * @param tbItem
	 * @return
	 */
	int insertItem(TbItem tbItem);
	
	/**
	 * 新增商品及商品描述，让两个insert操作处于同一事务下
	 * @param tbItem
	 * @param tbItemDesc
	 * @return
	 * @throws Exception
	 */
	int insertItemAndItemDesc(TbItem tbItem ,TbItemDesc tbItemDesc) throws Exception ;
	
	/**
	 * 新增商品及商品描述及商品规格参数详情，让三个insert操作处于同一事务下
	 * @param tbItem
	 * @param tbItemDesc
	 * @param tbItemParamItem
	 * @return
	 * @throws Exception
	 */
	int insertItemAndDescAndParamItem(TbItem tbItem ,TbItemDesc tbItemDesc,TbItemParamItem tbItemParamItem) throws Exception ;
	
	/**
	 * 查询全部未删除数据
	 * @return
	 */
	List<TbItem> selectAllByStatus(Byte status);
	
	/**
	 * 根据主键查询商品
	 * @param id
	 * @return
	 */
	TbItem selectById(Long id);
	
	
}
