package com.bzhang.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.ego.commons.pojo.EasyUITree;
import com.bzhang.ego.manage.service.TbItemCatService;

/**
 * 商品类目信息查询
 * @author bzhang
 *
 */
@Controller
public class TbItemCatController {
	@Resource
	private TbItemCatService tbItemCatServiceImpl;
	
	/**
	 * 根据父类id查询子类列表
	 * @param pid
	 * @return
	 */
	@RequestMapping("item/cat/list")
	@ResponseBody
	public List<EasyUITree> listItemCat(@RequestParam(value="id",defaultValue="0")Long pid){
		return tbItemCatServiceImpl.show(pid);
	}
}
