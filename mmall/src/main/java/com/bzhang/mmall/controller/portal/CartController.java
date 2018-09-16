package com.bzhang.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Product;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.CartService;
import com.bzhang.mmall.vo.CartVo;

@Controller
@RequestMapping("/cart/")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping("add_product_to_cart.do")
	@ResponseBody
	public ServerResponse<CartVo> addProductToCart(HttpSession session,Integer productId,Integer count) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.addProduct(user.getId(), productId, count);
	}
	
	@RequestMapping("update_cart.do")
	@ResponseBody
	public ServerResponse<CartVo> updateCart(HttpSession session,Integer productId,Integer count) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.updateCart(user.getId(), productId, count);
	}
	
	@RequestMapping("delete_product.do")
	@ResponseBody
	public ServerResponse<CartVo> deleteCartProduct(HttpSession session,String  productIds) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.deleteCart(user.getId(), productIds);
	}
	
	@RequestMapping("list_cart.do")
	@ResponseBody
	public ServerResponse<CartVo> listCart(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.listCart(user.getId());
	}
	
	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectAllOrNot(user.getId(),Const.Cart.CART_CHECKED,null);
	}
	
	@RequestMapping("select_null.do")
	@ResponseBody
	public ServerResponse<CartVo> selectNull(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectAllOrNot(user.getId(),Const.Cart.CART_UNCHECKED,null);
	}
	
	@RequestMapping("select_one.do")
	@ResponseBody
	public ServerResponse<CartVo> selectOne(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectAllOrNot(user.getId(),Const.Cart.CART_CHECKED,productId);
	}
	
	@RequestMapping("select_one_not.do")
	@ResponseBody
	public ServerResponse<CartVo> selectOneNot(HttpSession session,Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return cartService.selectAllOrNot(user.getId(),Const.Cart.CART_UNCHECKED,productId);
	}
	
	@RequestMapping("get_cart_product_num.do")
	@ResponseBody
	public ServerResponse<Integer> getCartProductNum(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createBySuccess(0);
		}
		return cartService.getCartProductNum(user.getId());
	}
}
