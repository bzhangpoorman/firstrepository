package com.bzhang.mmall.service;

import java.util.List;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Shipping;
import com.github.pagehelper.PageInfo;

public interface ShippingService {

	ServerResponse addShipping(Integer userId,Shipping shipping);

	ServerResponse delShipping(Integer userId ,Integer shippingId);

	ServerResponse updShipping(Integer userId, Shipping shipping);

	ServerResponse<PageInfo> listShipping(Integer userId,Integer pageNum,Integer pageSize);

	ServerResponse selShipping(Integer userId, Integer shippingId);

}
