package com.bzhang.ego.item.service;

import com.bzhang.ego.pojo.TbItemDesc;

public interface TbItemDescService {
	
	/**
	 * 显示商品详情
	 * @param itemId
	 * @return
	 */
	String showDesc(Long itemId);
	
}
