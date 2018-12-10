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

/**
 * 对content信息的管理
 * @author bzhang
 *
 */
@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentServiceImpl;
	
	/**
	 * 查询content数据，分页显示
	 * @param pageNum
	 * @param pageSize
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGrid showList(@RequestParam(value="page",defaultValue="1")Integer pageNum,
			@RequestParam(value="rows",defaultValue="10")Integer pageSize,
			@RequestParam(defaultValue="0")Long categoryId) {
		return tbContentServiceImpl.showByCategoryId(pageNum, pageSize, categoryId);
	}
	
	/**
	 * 新增content数据
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("content/save")
	@ResponseBody
	public EgoResult showList(TbContent tbContent) {
		return tbContentServiceImpl.insertContent(tbContent);
	}
	
	/**
	 * 修改content信息
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public EgoResult updateContent(TbContent tbContent) {
		return tbContentServiceImpl.updateContent(tbContent);
	}
	
	/**
	 * 删除content信息，一次删除一个或多个
	 * @param ids
	 * @return
	 */
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
