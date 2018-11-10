package com.bzhang.ego.order.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.utils.IDUtils;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbOrderDubboService;
import com.bzhang.ego.order.pojo.OrderParams;
import com.bzhang.ego.order.service.TbOrderService;
import com.bzhang.ego.pojo.TbOrder;
import com.bzhang.ego.pojo.TbOrderItem;
import com.bzhang.ego.pojo.TbOrderShipping;
import com.bzhang.ego.pojo.TbUser;
import com.bzhang.ego.redis.dao.JedisDao;
import com.bzhang.ego.vo.TbItemVo;
import com.google.common.collect.Lists;

@Service
public class TbOrderServiceImpl implements TbOrderService{
	
	@Reference
	private TbOrderDubboService tbOrderDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.user.key}")
	private String userKey;
	
	
	@Value("${redis.usercart.key}")
	private String cartKey;
	
	@Override
	public List<TbItemVo> getCart(String ids, String uuid) {
		List<String> idList = Arrays.asList(StringUtils.split(ids, ","));
		String key=userKey+uuid;
		TbUser user = getUserFromRedis(key);
		if (user!=null) {
			String k=cartKey+user.getUsername();
			
			if (jedisDaoImpl.exists(k)) {
				String cartJson = jedisDaoImpl.get(k);
				if (StringUtils.isNotBlank(cartJson)) {
					List<TbItemVo> newList=Lists.newArrayList();
					List<TbItemVo> list = JsonUtils.jsonToList(cartJson, TbItemVo.class);
					for (TbItemVo tbItemVo : list) {
						for (String string : idList) {
							if (Long.parseLong(string)==(long)tbItemVo.getId()) {
								if (tbItemVo.getStock()>=tbItemVo.getNum()) {
									tbItemVo.setEnough(true);
								}else {
									tbItemVo.setEnough(false);
								}
								newList.add(tbItemVo);
							}
						}
					}
					
					return newList;
				}
				
			}
		}
		return null;
	}

	
	private TbUser getUserFromRedis(String key) {
		if (jedisDaoImpl.exists(key)) {
			String res=jedisDaoImpl.get(key);
			if (StringUtils.isNotBlank(res)) {
				return JsonUtils.jsonToPojo(res, TbUser.class);
				
			}
		}
		return null;
	}


	@Override
	public String creatOrder(OrderParams params, String uuid) {
		String key=userKey+uuid;
		TbUser user = getUserFromRedis(key);
		if (user!=null) {
			//生成订单时间
			Date date = new Date();
			//order表信息
			long id = IDUtils.genItemId();
			TbOrder tbOrder=new TbOrder();
			tbOrder.setOrderId(id+"");
			tbOrder.setPayment(params.getPayment());
			tbOrder.setPaymentType(params.getPayment_type());
			tbOrder.setCreateTime(date);
			tbOrder.setUpdateTime(date);
			tbOrder.setStatus(1);
			tbOrder.setUserId(user.getId());
			tbOrder.setBuyerNick(user.getUsername());
			tbOrder.setBuyerRate(0);
			
			//生成order_item信息
			for (TbOrderItem orderItem : params.getOrderItems()) {
				orderItem.setOrderId(id+"");
				orderItem.setId(IDUtils.genItemId()+"");
			}
			
			TbOrderShipping orderShipping = params.getOrderShipping();
			orderShipping.setCreated(date);
			orderShipping.setUpdated(date);
			orderShipping.setOrderId(id+"");
			
			try {
				int res = tbOrderDubboServiceImpl.insertOrder(tbOrder, params.getOrderItems(), orderShipping);
				if (res==1) {
					//删除购物车中已下订单商品
					String k=cartKey+user.getUsername();
					
					if (jedisDaoImpl.exists(k)) {
						String cartJson = jedisDaoImpl.get(k);
						if (StringUtils.isNotBlank(cartJson)) {
							List<TbItemVo> newList=Lists.newArrayList();
							List<TbOrderItem> orderItems = params.getOrderItems();
							List<TbItemVo> list = JsonUtils.jsonToList(cartJson, TbItemVo.class);
							for (int i = 0; i < list.size(); i++) {
								for (int j = 0; j < orderItems.size(); j++) {
									if (orderItems.get(j).getItemId().equals(list.get(i).getId().toString())) {
										newList.add(list.get(i));
										break;
									}
								}
							}
							for (TbItemVo tbItemVo : newList) {
								System.out.println(tbItemVo.getTitle());
							}
							list.removeAll(newList);
							jedisDaoImpl.set(k, JsonUtils.objectToJson(list));
							
						}
						
					}
					
					return tbOrder.getOrderId();
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		return null;
	}
}
