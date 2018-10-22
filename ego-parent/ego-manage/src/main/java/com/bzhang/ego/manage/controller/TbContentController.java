package com.bzhang.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.manage.service.TbContentService;
import com.bzhang.ego.pojo.TbContent;

@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentServiceImpl;
	
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGrid showList(@RequestParam(value="page",defaultValue="1")Integer pageNum,
			@RequestParam(value="rows",defaultValue="10")Integer pageSize,
			@RequestParam(defaultValue="0")Long categoryId) {
		return tbContentServiceImpl.showByCategoryId(pageNum, pageSize, categoryId);
	}
	
	@RequestMapping("content/save")
	@ResponseBody
	public EgoResult showList(TbContent tbContent) {
		return tbContentServiceImpl.insertContent(tbContent);
	}
	
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public EgoResult updateContent(TbContent tbContent) {
		return tbContentServiceImpl.updateContent(tbContent);
	}
	
	@RequestMapping("content/delete")
	@ResponseBody
	public EgoResult deleteContent(String ids) {
		try {
			return tbContentServiceImpl.deleteContents(ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
