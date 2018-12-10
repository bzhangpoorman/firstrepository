package com.bzhang.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.utils.HttpClientUtil;
import com.bzhang.ego.commons.utils.IDUtils;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbItemDescDubboService;
import com.bzhang.ego.dubbo.service.TbItemDubboService;
import com.bzhang.ego.manage.service.TbItemService;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.pojo.TbItemParamItem;
import com.bzhang.ego.redis.dao.JedisDao;
import com.google.common.collect.Maps;

@Service
public class TbItemServiceImpl implements TbItemService{
	
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	
	@Value("${search.add.url}")
	private String searchUrl;
	
	@Value("${search.delete.url}")
	private String deleteUrl;
	
	@Value("${search.addbyid.url}")
	private String addByIdUrl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.item.key}")
	private String itemKey;
	
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
			long id=Long.parseLong(string);
			tbItem.setId(id);
			tbItem.setStatus(status);
			tbItem.setUpdated(date);
			res= tbItemDubboServiceImpl.updateItemStatus(tbItem);
			if (res<=0) {
				
				return res;
			}
			//更新redis中数据
			if (status==2||status==3) {
				jedisDaoImpl.del(itemKey+string);
			}
		}
		
		//更新solr中数据
		
		Map<String,String> map=Maps.newHashMap();
		map.put("ids", ids);
		if (status==2||status==3) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpClientUtil.doPost(deleteUrl, map);
					
				}
			}).start();
		}else {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpClientUtil.doPost(addByIdUrl, map);
				}
			}).start();
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
		if (index==1) {
			//更新solr中数据,新开线程提升响应速度
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpClientUtil.doPostJson(searchUrl, JsonUtils.objectToJson(tbItem));
				}
			}).start();
		}
		
		return index;
	}

	@Override
	public int updateItem(TbItem tbItem,String desc,String itemParams,Long itemParamId) throws Exception {
		// 商品更新时间
		Date date = new Date();

		tbItem.setUpdated(date);

		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(tbItem.getId());
		tbItemDesc.setUpdated(date);
		if (!StringUtils.isBlank(desc)) {
			tbItemDesc.setItemDesc(desc);

		}

		int index = 0;
		if (StringUtils.isNotBlank(itemParams)&&itemParamId!=null) {
			// 更新商品及商品描述及规格参数
			TbItemParamItem tbItemParamItem = new TbItemParamItem();
			tbItemParamItem.setId(itemParamId);
			tbItemParamItem.setParamData(itemParams);
			tbItemParamItem.setUpdated(date);
			index = tbItemDubboServiceImpl.updateItemAndDescAndParamItem(tbItem, tbItemDesc, tbItemParamItem);

		} else {
			// 更新商品及商品描述
			index = tbItemDubboServiceImpl.updateItemAndItemDesc(tbItem, tbItemDesc);
		}
		
		if (index==1) {
			//更新solr中数据,新开线程提升响应速度
			TbItem item = tbItemDubboServiceImpl.selectById(tbItem.getId());
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpClientUtil.doPostJson(searchUrl, JsonUtils.objectToJson(item));
				}
			}).start();
		}
		return index;
	}

}
