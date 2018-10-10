package com.bzhang.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.OrderService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {
	
	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> getList(HttpSession session,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		if (user.getRole()!=Const.Role.ROLE_ADMIN) {
			return ServerResponse.createByErrorMsg("该用户不是管理员！");
		}
		return orderService.getManageOrderList(pageNum, pageSize);
		
	}
	
	@RequestMapping("get_detail.do")
	@ResponseBody
	public ServerResponse getDetail(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		if (user.getRole()!=Const.Role.ROLE_ADMIN) {
			return ServerResponse.createByErrorMsg("该用户不是管理员！");
		}
		return orderService.getManageOrderDetail(orderNo);
		
	}
	
	@RequestMapping("search_order.do")
	@ResponseBody
	public ServerResponse<PageInfo> searchOrder(HttpSession session,Long orderNo,@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		if (user.getRole()!=Const.Role.ROLE_ADMIN) {
			return ServerResponse.createByErrorMsg("该用户不是管理员！");
		}
		return orderService.searchOrder(orderNo,pageNum,pageSize);
		
	}
	
	@RequestMapping("send_goods.do")
	@ResponseBody
	public ServerResponse<String> sendGoods(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
		}
		if (user.getRole()!=Const.Role.ROLE_ADMIN) {
			return ServerResponse.createByErrorMsg("该用户不是管理员！");
		}
		return orderService.sendGoods(orderNo);
		
	}
}
