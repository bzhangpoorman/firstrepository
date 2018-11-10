package com.bzhang.ego.dubbo.service;

import java.util.List;

import com.bzhang.ego.pojo.TbOrder;
import com.bzhang.ego.pojo.TbOrderItem;
import com.bzhang.ego.pojo.TbOrderShipping;

public interface TbOrderDubboService {
	/**
	 * 新增订单信息，包括order，orderItem，orderShipping
	 * @param tbOrder
	 * @param orderItems
	 * @param orderShipping
	 * @return
	 */
	int insertOrder(TbOrder tbOrder,List<TbOrderItem> orderItems,TbOrderShipping orderShipping) throws Exception;
}
