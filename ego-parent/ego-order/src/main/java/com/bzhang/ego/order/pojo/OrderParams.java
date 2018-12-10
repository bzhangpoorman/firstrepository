package com.bzhang.ego.order.pojo;

import java.util.List;

import com.bzhang.ego.pojo.TbOrderItem;
import com.bzhang.ego.pojo.TbOrderShipping;

/**
 * 订单页面所需数据
 * @author bzhang
 *
 */
public class OrderParams {

	private String payment;
	
	private int payment_type;
	private List<TbOrderItem> orderItems;
	
	private TbOrderShipping orderShipping;

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public int getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(int payment_type) {
		this.payment_type = payment_type;
	}

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
}
