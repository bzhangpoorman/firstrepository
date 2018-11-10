package com.bzhang.ego.dubbo.service;

import com.bzhang.ego.pojo.TbItemParamItem;

public interface TbItemParamItemDubboService {
	/**
	 * 查询商品规格参数
	 * @param itemId
	 * @return
	 */
	TbItemParamItem selectByItemId(Long itemId);
}
