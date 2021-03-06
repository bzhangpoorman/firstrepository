package com.bzhang.ego.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @author bzhang
 *
 */
@Controller
public class PageController {
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/")
	public String welcome() {
		return "index";
	}
	
	/**
	 * 根据url跳转到正确的jsp页面
	 * @param page
	 * @return
	 */
	@RequestMapping("{page}")
	public String showPage(@PathVariable String page){
		
		return page;
	}
	
	/**
	 * 根据url跳转到正确的jsp页面
	 * @param page
	 * @return
	 */
	@RequestMapping("rest/page/{page}")
	public String showP(@PathVariable String page){
		return page;
	}
}
