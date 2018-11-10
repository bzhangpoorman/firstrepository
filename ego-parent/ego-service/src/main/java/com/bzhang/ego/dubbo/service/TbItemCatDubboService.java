package com.bzhang.ego.dubbo.service;

import java.util.List;

import com.bzhang.ego.pojo.TbItemCat;

public interface TbItemCatDubboService {
	/**
	 * 根据parentId查询所有子目录
	 * @param parentId
	 * @return
	 */
	List<TbItemCat> show(Long pid);
	
	/**
	 * 根据id查询类目
	 * @param id
	 * @return
	 */
	TbItemCat selectById(Long id);
}
