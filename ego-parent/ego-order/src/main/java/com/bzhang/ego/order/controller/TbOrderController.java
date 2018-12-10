package com.bzhang.ego.order.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bzhang.ego.commons.utils.CookieUtils;
import com.bzhang.ego.order.pojo.OrderParams;
import com.bzhang.ego.order.service.TbOrderService;
import com.bzhang.ego.vo.TbItemVo;

/**
 * 订单controller
 * @author bzhang
 *
 */
@Controller
public class TbOrderController {
	
	@Resource
	private TbOrderService tbOrderServiceImpl;
	
	/**
	 * 购物车中已选商品获取
	 * @param ids
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("order/order-cart.html")
	public String showOrder(String ids,HttpServletRequest request,Model model) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		List<TbItemVo> list = tbOrderServiceImpl.getCart(ids, uuid);
		model.addAttribute("cartList", list);
		
		return "order-cart";
	}
	
	/**
	 * 页面显示
	 * @param path
	 * @return
	 */
	@RequestMapping("show/{path}")
	public String showJsp(@PathVariable String path) {
		return path;
	}
	
	/**
	 * 生成订单
	 * @param params
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("order/create.html")
	public String createOrder(OrderParams params,HttpServletRequest request,Model model) {
		String uuid = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String orderId = tbOrderServiceImpl.creatOrder(params, uuid);
		if (StringUtils.isNotBlank(orderId)) {
			model.addAttribute("orderId", orderId);
			model.addAttribute("payment", params.getPayment());
			model.addAttribute("date", new Date(System.currentTimeMillis()+1000*60*60*24*3));
			return "success";
		}else {
			return "error/exception";
		}
		
		
		
	}
}
