package com.bzhang.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bzhang.ego.item.service.TbItemService;

@Controller
public class TbItemController {
	
	@Resource
	private TbItemService tbItemServiceImpl;
	
	@RequestMapping("item/{id}.html")
	public String showItem(@PathVariable Long id,Model model) {
		
		model.addAttribute("item", tbItemServiceImpl.showItem(id));
		return "item";
	}
}
