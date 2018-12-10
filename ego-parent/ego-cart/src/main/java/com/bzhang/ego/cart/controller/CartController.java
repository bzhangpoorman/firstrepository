package com.bzhang.ego.cart.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.cart.service.CartService;
import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.CookieUtils;
import com.bzhang.ego.vo.TbItemVo;

/**
 * 购物车功能controller层，包含对购物内商品的新增，数量修改，删除，以及购物车页面的跳转
 * @author bzhang
 *
 */
@Controller
public class CartController {
	
	@Resource
	private CartService cartServiceImpl;
	
	/**
	 * 购物车展示
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("cart/cart.html")
	public String showCart(HttpServletRequest request,Model model) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		List<TbItemVo> list = cartServiceImpl.showCart(uuid);
		model.addAttribute("cartList", list);
		return "cart";
	}
	
	/**
	 * 购物车商品新增
	 * @param id
	 * @param num
	 * @param request
	 * @return
	 */
	@RequestMapping("cart/add/{id}.html")
	public String addToCart(@PathVariable Long id,Integer num,HttpServletRequest request) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		Boolean res = cartServiceImpl.addToCart(id, num, uuid);
		if (res) {
			return "cartSuccess";
		}
		return "exception";
		
	}
	
	/**
	 * 购物车中商品数量修改
	 * @param id
	 * @param num
	 * @param request
	 * @return
	 */
	@RequestMapping("cart/update/num/{id}/{num}.action")
	@ResponseBody
	public EgoResult updateCart(@PathVariable Long id,@PathVariable Integer num,HttpServletRequest request) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		Boolean res = cartServiceImpl.updateCart(id, num, uuid);
		EgoResult egoResult=new EgoResult();
		if (res) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE.getCode());
			egoResult.setMsg(EgoResultReason.OK_UPDATE.getValue());
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_UPDATE.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_UPDATE.getValue());
		}
		return egoResult;
	}
	
	/**
	 * 购物车中商品删除
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("cart/delete/{id}.action")
	@ResponseBody
	public EgoResult deleteCart(@PathVariable Long id,HttpServletRequest request) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		Boolean res = cartServiceImpl.deleteCartItem(id, uuid);
		EgoResult egoResult=new EgoResult();
		if (res) {
			egoResult.setStatus(EgoResultReason.OK_DELETE.getCode());
			egoResult.setMsg(EgoResultReason.OK_DELETE.getValue());
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_DELETE.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_DELETE.getValue());
		}
		return egoResult;
	}
	
}
