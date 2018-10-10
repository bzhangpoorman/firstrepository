package com.bzhang.mmall.common;

import java.util.List;

import com.google.common.collect.Lists;

public class Const {
	public static final String  CURRENT_USER="currentUser";
	public static final String USERNAME="username";
	public static final String EMAIL ="email";
	public static final int PRODUCT_SALE=1;
	public static final int PRODUCT_UNSALE=2;
	
	public interface ProductListOrderBy{
		List<String> ORDER_BY=Lists.newArrayList("price_asc","price_desc");
	}
	
	public interface Cart{
		int DEFAULT_CART_CHECKED=1;
		int CART_CHECKED=1;
		int CART_UNCHECKED=0;
		String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
		String LIMIT_NUM_FAILED="LIMIT_NUM_FAILED";
	}
	
	public interface Role{
		int ROLE_CUSTOMER=0;
		int ROLE_ADMIN=1;
	}
	
	public enum OrderStatusEnum{
		CANCELED(0,"已取消"),
		NO_PAY(10,"未支付"),
		PAID(20,"已支付"),
		SHIPPING(40,"已发货"),
		ORDER_SUCCESS(50,"订单完成"),
		ORDER_CLOSE(60,"订单关闭");
	
		
		private int code;
		private String value;
		private OrderStatusEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}
		public int getCode() {
			return code;
		}
		public String getValue() {
			return value;
		}
		
		public static String getDesc(int code) {
			for (OrderStatusEnum orderStatusEnum : values()) {
				if (orderStatusEnum.getCode()==code) {
					return orderStatusEnum.getValue();
				}
			}
			throw new RuntimeException("没有找到对应的枚举");
			
		}
	}	
	
	public interface AlipayCallback{
		String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
		String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";
		
		String RESPONSE_SUCCESS="success";
		String RESPONSE_FAILED="failed";

	}
	
	public enum PayPlatformEnum{
		ALIPAY(1,"支付宝"),
		WECHATPAY(2,"微信支付");
		
	
		
		private int code;
		private String value;
		private PayPlatformEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}
		public int getCode() {
			return code;
		}
		public String getValue() {
			return value;
		}
	}	
	
	public interface ChangeStock{
		String ADD_STOCK="add";
		String REDUCE_STOCK="reduce";
		

	}
}
