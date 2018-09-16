package com.bzhang.mmall.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.dao.ShippingMapper;
import com.bzhang.mmall.pojo.Shipping;
import com.bzhang.mmall.service.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

@Service("shippingService")
public class ShippingServiceImpl implements ShippingService{
	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ServerResponse addShipping(Integer userId,Shipping shipping) {
		if (StringUtils.isBlank(shipping.getReceiverName())) {
			return ServerResponse.createByErrorMsg("收货人不能为空");
		}
		if (StringUtils.isBlank(shipping.getReceiverMobile())&&StringUtils.isBlank(shipping.getReceiverPhone())){
			return ServerResponse.createByErrorMsg("联系方式至少需要一个");
		}
		if ((StringUtils.isBlank(shipping.getReceiverProvince())&&StringUtils.isBlank(shipping.getReceiverCity()))
				||StringUtils.isBlank(shipping.getReceiverDistrict())
				||StringUtils.isBlank(shipping.getReceiverAddress())){
			return ServerResponse.createByErrorMsg("收货地址不能为空");
		}
		if (shipping.getReceiverZip()==null) {
			shipping.setReceiverZip("");
		}
		shipping.setUserId(userId);
		int res = shippingMapper.insert(shipping);
		if (res>0) {
			Map<String, Integer> map=Maps.newHashMap();
			map.put("shippingId", shipping.getId());
			return ServerResponse.createBySuccess("添加地址成功",map);
			
		}
		return ServerResponse.createByErrorMsg("添加地址失败");
	}

	@Override
	public ServerResponse delShipping(Integer userId, Integer shippingId) {

		if (shippingId==null) {
			return ServerResponse.createByErrorMsg("参数错误");
		}
		int res = shippingMapper.deleteByUserIdAndShippingId(userId ,shippingId);
		if (res>0) {
			return ServerResponse.createBySuccessMsg("删除地址成功");
		}
		return ServerResponse.createByErrorMsg("地址不存在或已删除");
	}

	@Override
	public ServerResponse updShipping(Integer userId, Shipping shipping) {
		if (StringUtils.isBlank(shipping.getReceiverName())) {
			return ServerResponse.createByErrorMsg("收货人不能为空");
		}
		if (StringUtils.isBlank(shipping.getReceiverMobile())&&StringUtils.isBlank(shipping.getReceiverPhone())){
			return ServerResponse.createByErrorMsg("联系方式至少需要一个");
		}
		if ((StringUtils.isBlank(shipping.getReceiverProvince())&&StringUtils.isBlank(shipping.getReceiverCity()))
				||StringUtils.isBlank(shipping.getReceiverDistrict())
				||StringUtils.isBlank(shipping.getReceiverAddress())){
			return ServerResponse.createByErrorMsg("收货地址不能为空");
		}
		if (shipping.getReceiverZip()==null) {
			shipping.setReceiverZip("");
		}
		shipping.setUserId(userId);
		int res = shippingMapper.updateByPrimaryKeySelective(shipping);
		if (res>0) {
			Map<String, Integer> map=Maps.newHashMap();
			map.put("shippingId", shipping.getId());
			return ServerResponse.createBySuccess("修改地址成功",map);
			
		}
		return ServerResponse.createByErrorMsg("修改地址失败");
	}

	@Override
	public ServerResponse<PageInfo> listShipping(Integer userId,Integer pageNum,Integer pageSize) {
		if (userId==null) {
			return ServerResponse.createByErrorMsg("参数错误");
		}
		PageHelper.startPage(pageNum,pageSize);
		List<Shipping> shippingList = shippingMapper.selectShippingByUserId(userId);
		PageInfo pageInfo=new PageInfo(shippingList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse<Shipping> selShipping(Integer userId, Integer shippingId) {
		if (shippingId==null) {
			return ServerResponse.createByErrorMsg("参数错误");
		}
		Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId ,shippingId);
		if (shipping!=null) {
			return ServerResponse.createBySuccess(shipping);
		}
		return ServerResponse.createByErrorMsg("地址不存在或已删除");
	}

}
