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
}
