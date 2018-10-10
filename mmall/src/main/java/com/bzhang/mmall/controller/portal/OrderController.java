package com.bzhang.mmall.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Shipping;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.OrderService;
import com.bzhang.mmall.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
	private static final Logger logger=LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("create_order.do")
	@ResponseBody
	public ServerResponse  createOrder(HttpSession session,Integer shippingId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.createOrder(user.getId(), shippingId);
	}
	
	@RequestMapping("cancel_order.do")
	@ResponseBody
	public ServerResponse  cancelOrder(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.cancelOrder(user.getId(), orderNo);
	}
	
	@RequestMapping("get_order_cart_product.do")
	@ResponseBody
	public ServerResponse  getOrderCartProduct(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.getOrderCartProduct(user.getId());
	}
	
	@RequestMapping("get_detail.do")
	@ResponseBody
	public ServerResponse<OrderVo>  getDetail(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.getDetail(user.getId(),orderNo);
	}
	
	@RequestMapping("get_list.do")
	@ResponseBody
	public ServerResponse<PageInfo>  getOrderList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.getOrderList(user.getId(),pageNum,pageSize);
	}
	
	@RequestMapping("pay.do")
	@ResponseBody
	public ServerResponse  pay(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		String path = session.getServletContext().getRealPath("upload");
		return orderService.pay(user.getId(), path, orderNo);
	}
	
	@RequestMapping("alipay_callback.do")
	@ResponseBody
	public Object   alipayCallback(HttpServletRequest request) {
		Map<String, String > params=Maps.newHashMap();
		Map requestParams=request.getParameterMap();
		Iterator iterator = requestParams.keySet().iterator();
		while (iterator.hasNext()) {
			String  key = (String ) iterator.next();
			String[] values=(String[]) requestParams.get(key);
			StringBuilder valueBuilder=new StringBuilder();
			for (int i = 0; i < values.length; i++) {
				valueBuilder.append(values[i]);
				if (i<values.length-1) {
					valueBuilder.append(",");
				}
			}
			params.put(key, valueBuilder.toString());
			
		}
		logger.info("支付宝回调，sign:{},trade_status:{},参数:{}	",params.get("sign"),params.get("trade_status"),params.toString());
		
		params.remove("sign_type");
		try {
			boolean rsaCheckV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
			if (!rsaCheckV2) {
				return ServerResponse.createByErrorMsg("非法请求，验证失败，报警咯！");
			}
			
		} catch (AlipayApiException e) {

			logger.info("支付宝回调异常",e);
		}
		
		//验证数据是否正确
		
		//支付宝回调
		ServerResponse serverResponse = orderService.alipayCallBack(params);
		if (serverResponse.isSuccess()) {
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	
	@RequestMapping("query_order_pay_status.do")
	@ResponseBody
	public ServerResponse<Boolean>  queryOrderPayStatus(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		return orderService.queryOrderPayStatus(user.getId(), orderNo);
	}
}
