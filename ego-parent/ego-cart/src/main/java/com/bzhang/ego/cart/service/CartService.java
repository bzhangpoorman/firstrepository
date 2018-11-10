package com.bzhang.ego.cart.service;

import java.util.List;

import com.bzhang.ego.vo.TbItemVo;

public interface CartService {
	/**
	 * 将商品添加到购物车中
	 * @param id
	 * @param num
	 * @param uuid
	 * @return
	 */
	Boolean addToCart(Long id,Integer num,String uuid);
	
	/**
	 * 显示购物车
	 * @param uuid
	 * @return
	 */
	List<TbItemVo> showCart(String uuid);
	
	/**
	 * 更改购物车中商品数量
	 * @param id
	 * @param num
	 * @param uuid
	 * @return
	 */
	Boolean updateCart(Long id,Integer num,String uuid);
	
	/**
	 * 删除购物车中商品
	 * @param id
	 * @param uuid
	 * @return
	 */
	Boolean deleteCartItem(Long id,String uuid);
}
