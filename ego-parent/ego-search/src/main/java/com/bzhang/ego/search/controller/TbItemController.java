package com.bzhang.ego.search.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.search.service.TbItemService;

@Controller
public class TbItemController {
	
	@Resource
	private TbItemService tbItemServiceImpl;
	
	/**
	 * 初始化solr
	 * @return
	 */
	@RequestMapping(value="solr/init",produces="text/html;charset=utf-8")
	@ResponseBody
	public String initSolr() {
		long s=System.currentTimeMillis();
		try {
			tbItemServiceImpl.init();
			long e=System.currentTimeMillis();
			return "初始化时间"+(e-s)+"ms";
		} catch (SolrServerException e) {
			e.printStackTrace();
			return "初始化失败";
		} catch (IOException e) {
			e.printStackTrace();
			return "初始化失败";
		}
	}
	
	@RequestMapping("search.html")
	public String search(Model model,String q,
			@RequestParam(value="page",defaultValue="1")Integer pageNum,
			@RequestParam(value="rows",defaultValue="12")Integer pageSize) {
		try {
			q=new String(q.getBytes("iso-8859-1"),"utf-8");
			System.out.println(q);
			Map<String, Object> map = tbItemServiceImpl.searchItem(q, pageNum, pageSize);
			model.addAttribute("query", q);
			model.addAttribute("itemList",map.get("itemList"));
			model.addAttribute("totalPages", map.get("totalPages"));
			model.addAttribute("page", pageNum);
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
		return "search";
	}
	
	@RequestMapping("solr/add")
	@ResponseBody
	public int insertOrUpdate(@RequestBody TbItem tbItem) {
		try {
			return tbItemServiceImpl.insertOrUpdateToSolr(tbItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@RequestMapping("solr/addbyid")
	@ResponseBody
	public int insertById(String ids) {
		try {
			return tbItemServiceImpl.insertById(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@RequestMapping("solr/delete")
	@ResponseBody
	public int deleteSolrById(String ids) {
		try {
			return tbItemServiceImpl.deleteSolrById(ids);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
