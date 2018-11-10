package com.bzhang.ego.dubbo.service;

import com.bzhang.ego.pojo.TbItemDesc;

public interface TbItemDescDubboService {
	/**
	 * 新增商品描述信息
	 * @param tbItemDesc
	 * @return
	 */
	int insertItemDesc(TbItemDesc tbItemDesc);
	
	/**
	 * 根据itemId查询
	 * @param itemId
	 * @return
	 */
	TbItemDesc selectByItemId(Long itemId);
	
}
