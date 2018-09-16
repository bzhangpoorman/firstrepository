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
import com.bzhang.mmall.dao.CategoryMapper;
import com.bzhang.mmall.pojo.Category;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.CategoryService;
import com.bzhang.mmall.service.UserService;

@Controller
@RequestMapping("manage/category")
public class CategoryManageController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping("add_category.do")
	@ResponseBody
	public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue="0") Integer parentId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return categoryService.addCategory(categoryName, parentId);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}

	}
	
	@RequestMapping("set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(HttpSession session,String categoryName,Integer categoryId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return categoryService.setCategoryNameById(categoryName, categoryId);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}

	}
	
	@RequestMapping("get_children_parallel_category.do")
	@ResponseBody
	public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return categoryService.getChildrenParallelCategory(categoryId);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}

	}
	
	@RequestMapping("get_deep_category.do")
	@ResponseBody
	public ServerResponse getCategoryAndDeepChildren(HttpSession session,@RequestParam(value="categoryId",defaultValue="0")Integer categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录！");
		}
		ServerResponse<String> serverResponse = userService.checkAdminRole(user);
		if (serverResponse.isSuccess()) {
			return categoryService.getCategoryAndDeepChildren(categoryId);
		}else {
			return ServerResponse.createByErrorMsg("该用户没有管理员权限，无法操作！");
		}

	}
	
}
