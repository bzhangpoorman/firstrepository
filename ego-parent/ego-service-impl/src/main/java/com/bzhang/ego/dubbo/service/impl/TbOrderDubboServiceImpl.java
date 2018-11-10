package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bzhang.ego.dubbo.service.TbOrderDubboService;
import com.bzhang.ego.mapper.TbOrderItemMapper;
import com.bzhang.ego.mapper.TbOrderMapper;
import com.bzhang.ego.mapper.TbOrderShippingMapper;
import com.bzhang.ego.pojo.TbOrder;
import com.bzhang.ego.pojo.TbOrderItem;
import com.bzhang.ego.pojo.TbOrderShipping;

public class TbOrderDubboServiceImpl implements TbOrderDubboService{
	
	@Resource
	private TbOrderMapper tbOrderMapper;
	
	@Resource
	private TbOrderItemMapper  tbOrderItemMapper;
	
	@Resource
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	@Override
	public int insertOrder(TbOrder tbOrder, List<TbOrderItem> orderItems, TbOrderShipping orderShipping) throws Exception {
		int res=0;
		res += tbOrderMapper.insertSelective(tbOrder);
		for (TbOrderItem tbOrderItem : orderItems) {
			res+=tbOrderItemMapper.insertSelective(tbOrderItem);
		}
		res+=tbOrderShippingMapper.insertSelective(orderShipping);
		if (res==orderItems.size()+2) {
			return 1;
		}else {
			throw new Exception("添加订单失败");
		}
	}

}
