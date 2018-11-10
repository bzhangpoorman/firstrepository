package com.bzhang.ego.order.service;

import java.util.List;

import com.bzhang.ego.order.pojo.OrderParams;
import com.bzhang.ego.vo.TbItemVo;

public interface TbOrderService {
	/**
	 * 根据购物车信息生成订单
	 * @param ids
	 * @param uuid
	 * @return
	 */
	List<TbItemVo> getCart(String ids,String uuid);
	
	/**
	 * 新增订单信息
	 * @param params
	 * @param uuid
	 * @return
	 */
	String creatOrder(OrderParams params,String uuid);
}
