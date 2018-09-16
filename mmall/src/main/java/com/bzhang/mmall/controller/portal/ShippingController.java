package com.bzhang.mmall.controller.portal;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Shipping;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.ShippingService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/shpiing/")
public class ShippingController {
	@Autowired
	private ShippingService shippingService;
	
	@RequestMapping("add_shipping.do")
	@ResponseBody
	public ServerResponse addShipping(HttpSession session,Shipping shipping) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.addShipping(user.getId(),shipping);
	}
	
	@RequestMapping("del_shipping.do")
	@ResponseBody
	public ServerResponse delShipping(HttpSession session,Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.delShipping(user.getId(),shippingId);
	}
	
	@RequestMapping("upd_shipping.do")
	@ResponseBody
	public ServerResponse updShipping(HttpSession session,Shipping shipping) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.updShipping(user.getId(),shipping);
	}
	
	@RequestMapping("sel_shipping.do")
	@ResponseBody
	public ServerResponse selShipping(HttpSession session,Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.selShipping(user.getId(),shippingId);
	}
	
	@RequestMapping("list_shipping.do")
	@ResponseBody
	public ServerResponse<PageInfo> listShipping(HttpSession session,
			@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return shippingService.listShipping(user.getId(),pageNum,pageSize);
	}
}
