package com.bzhang.ego.item.service;

import com.bzhang.ego.vo.TbItemVo;

public interface TbItemService {
	/**
	 * 商品页显示商品信息
	 * @param id
	 * @return
	 */
	TbItemVo showItem(Long id);
}
