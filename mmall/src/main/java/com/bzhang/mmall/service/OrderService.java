package com.bzhang.mmall.service;

import java.util.Map;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.vo.OrderVo;
import com.github.pagehelper.PageInfo;

public interface OrderService {
	
	ServerResponse pay(Integer userId,String path,Long orderNo);
	
	ServerResponse alipayCallBack(Map<String, String> params);
	
	ServerResponse<Boolean> queryOrderPayStatus(Integer userId,Long orderNo);

	ServerResponse createOrder(Integer userId, Integer shippingId);

	ServerResponse cancelOrder(Integer userId, Long orderNo);
	
	ServerResponse getOrderCartProduct(Integer userId);

	ServerResponse<OrderVo> getDetail(Integer userId, Long orderNo);

	ServerResponse<PageInfo> getOrderList(Integer userId,Integer pageNum,Integer pageSize);

	ServerResponse<PageInfo> getManageOrderList(Integer pageNum, Integer pageSize);

	ServerResponse<OrderVo> getManageOrderDetail(Long orderNo);

	ServerResponse<PageInfo> searchOrder(Long orderNo, Integer pageNum, Integer pageSize);

	ServerResponse<String> sendGoods(Long orderNo);
}
