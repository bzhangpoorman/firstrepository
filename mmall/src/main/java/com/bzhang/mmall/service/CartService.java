package com.bzhang.mmall.service;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.vo.CartVo;

public interface CartService {

	ServerResponse<CartVo> addProduct(Integer userId, Integer productId, Integer count);

	ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count);

	ServerResponse<CartVo> deleteCart(Integer userId, String  productIds);

	ServerResponse<CartVo> listCart(Integer userId);

	ServerResponse<CartVo> selectAllOrNot(Integer userId, int cartChecked,Integer productId);

	ServerResponse<Integer> getCartProductNum(Integer userId);
}
