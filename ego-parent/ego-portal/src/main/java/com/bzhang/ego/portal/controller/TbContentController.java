package com.bzhang.ego.portal.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bzhang.ego.portal.service.TbContentService;

@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentServiceImpl;
	
	@RequestMapping("showPic")
	public String showPortalPic(Model model) {
		model.addAttribute("ad1", tbContentServiceImpl.showBigPic());
		return "index";
	}
}
