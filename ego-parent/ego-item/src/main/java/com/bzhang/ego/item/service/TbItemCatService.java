package com.bzhang.ego.item.service;

import com.bzhang.ego.item.pojo.PortalMenu;

public interface TbItemCatService {
	/**
	 * 查询出所有类目并转换成前台portal所需要的格式
	 * @return
	 */
	PortalMenu showItemCatMenu();
}
