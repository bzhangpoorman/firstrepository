package com.bzhang.ego.manage.service;

import java.util.List;

import com.bzhang.ego.commons.pojo.EasyUITree;

public interface TbItemCatService {
	/**
	 * 商品类目目录查询显示
	 * @param pid
	 * @return
	 */
	List<EasyUITree> show (Long pid);
}
