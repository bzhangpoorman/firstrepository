package com.bzhang.ego.item.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.item.service.TbItemParamItemService;

@Controller
public class TbItemParamItemController {
	@Resource
	private TbItemParamItemService tbItemParamItemServiceImpl;
	
	@RequestMapping(value="item/param/{itemId}.html",produces="text/html;charset=utf-8")
	@ResponseBody
	public String showParamItem(@PathVariable Long itemId) {
		return tbItemParamItemServiceImpl.showParamItem(itemId);
	}
}
