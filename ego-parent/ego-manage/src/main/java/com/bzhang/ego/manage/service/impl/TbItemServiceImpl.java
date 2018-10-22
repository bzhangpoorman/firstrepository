package com.bzhang.ego.manage.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.utils.IDUtils;
import com.bzhang.ego.dubbo.service.TbItemDescDubboService;
import com.bzhang.ego.dubbo.service.TbItemDubboService;
import com.bzhang.ego.manage.service.TbItemService;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.pojo.TbItemParamItem;

@Service
public class TbItemServiceImpl implements TbItemService{
	
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	
	@Override
	public EasyUIDataGrid show(Integer pageNum, Integer pageSize) {
		return tbItemDubboServiceImpl.show(pageNum, pageSize);
	}

	@Override
	public int updateItemStatus(String ids,Byte status) {
		String[] idStr = ids.split(",");
		int res = 0;
		Date date = new Date();
		TbItem tbItem=new TbItem();
		for (String string : idStr) {
			tbItem.setId(Long.parseLong(string));
			tbItem.setStatus(status);
			tbItem.setUpdated(date);
			res= tbItemDubboServiceImpl.updateItemStatus(tbItem);
			if (res<=0) {
				return res;
			}
		}
		return res;
	}

	@Override
	public int insertItem(TbItem tbItem,String desc,String itemParams) throws Exception {
		// 生成商品id
		Long itemId = IDUtils.genItemId();
		// 商品生成时间
		Date date = new Date();

		tbItem.setId(itemId);
		tbItem.setCreated(date);
		tbItem.setUpdated(date);
		tbItem.setStatus((byte) 1);

		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		if (!StringUtils.isBlank(desc)) {
			tbItemDesc.setItemDesc(desc);

		}
		
		int index = 0;
		if (StringUtils.isNotBlank(itemParams)) {
			// 新增商品及商品描述及规格参数
			TbItemParamItem tbItemParamItem=new TbItemParamItem();
			tbItemParamItem.setItemId(itemId);
			tbItemParamItem.setParamData(itemParams);
			tbItemParamItem.setCreated(date);
			tbItemParamItem.setUpdated(date);
			index = tbItemDubboServiceImpl.insertItemAndDescAndParamItem(tbItem, tbItemDesc, tbItemParamItem);
			
		}else {
			// 新增商品及商品描述
			index=tbItemDubboServiceImpl.insertItemAndItemDesc(tbItem, tbItemDesc);
		}
		
		
		return index;
	}

}
