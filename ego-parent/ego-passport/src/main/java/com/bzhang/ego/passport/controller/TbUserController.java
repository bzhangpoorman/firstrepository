package com.bzhang.ego.passport.controller;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.CookieUtils;
import com.bzhang.ego.passport.service.TbUserService;
import com.bzhang.ego.pojo.TbUser;

@Controller
public class TbUserController {
	
	@Resource
	private TbUserService tbUserServiceImpl;
	
	
	
	/**
	 * 跳转到登录页面，登录成功后跳转回原页面
	 * @param url 原页面地址
	 * @param model
	 * @return
	 */
	@RequestMapping("user/showLogin")
	public String showLogin(@RequestHeader("Referer") String url,Model model,String referer) {
		if (StringUtils.isNotBlank(referer)) {
			model.addAttribute("redirect",referer);
		}else if (StringUtils.isNotBlank(url)){
			model.addAttribute("redirect", url);
		}
		
		return "login";
	}
	
	/**
	 * 登录验证
	 * @param user
	 * @return
	 */
	@RequestMapping("user/login")
	@ResponseBody
	public EgoResult login(TbUser user,HttpServletResponse response,HttpServletRequest request) {
		String uuid = UUID.randomUUID().toString();
		
		EgoResult result = tbUserServiceImpl.checkUser(user,uuid);
		if (result.getStatus()==200) {
			CookieUtils.setCookie(request, response, "TT_TOKEN", uuid,1800);
			
		}
		return result;
	}
	
	/**
	 * 获取用户登录信息
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("user/token/{token}")
	@ResponseBody
	public Object getUser(@PathVariable String token,String callback) {
		EgoResult egoResult = tbUserServiceImpl.getUser(token);
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mValue=new MappingJacksonValue(egoResult);
			mValue.setJsonpFunction(callback);
			return mValue;
		}
		return egoResult;
	}
	
	/**
	 * 安全退出
	 * @param token
	 * @param callback
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("user/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token,String callback,HttpServletRequest request,HttpServletResponse response) {
		EgoResult egoResult = tbUserServiceImpl.logout(token);
		if (egoResult.getStatus()==200) {
			CookieUtils.deleteCookie(request, response, "TT_TOKEN");
			
		}
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mValue=new MappingJacksonValue(egoResult);
			mValue.setJsonpFunction(callback);
			return mValue;
		}
		return egoResult;
	}
	
	/**
	 * 跳转到用户注册页面
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping("user/showRegister")
	public String showRegister(@RequestHeader("Referer") String url,Model model) {
		model.addAttribute("redirect", url);
		return "register";
	}
	
	@RequestMapping("user/check/{param}/{type}")
	@ResponseBody
	public EgoResult checkParams(@PathVariable String param,@PathVariable Integer type) {
		return tbUserServiceImpl.checkParam(param, type);
	}
	
	
	@RequestMapping("user/register")
	@ResponseBody
	public EgoResult register(TbUser user) {
		return tbUserServiceImpl.insertUser(user);
	}
}
