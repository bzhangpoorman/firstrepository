package com.bzhang.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.item.service.TbItemDescService;
import com.bzhang.ego.pojo.TbItemDesc;

@Controller
public class TbItemDescController {

	
	@Resource
	private TbItemDescService tbItemDescServiceImpl;
	
	@RequestMapping(value="item/desc/{itemId}.html",produces="text/html;charset=utf-8")
	@ResponseBody
	public String showDesc(@PathVariable Long itemId) {
		
		return tbItemDescServiceImpl.showDesc(itemId);
	}
}
