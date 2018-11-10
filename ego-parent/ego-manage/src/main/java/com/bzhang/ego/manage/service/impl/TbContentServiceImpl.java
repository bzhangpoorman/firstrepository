package com.bzhang.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbContentDubboService;
import com.bzhang.ego.manage.service.TbContentService;
import com.bzhang.ego.pojo.TbContent;
import com.bzhang.ego.redis.dao.JedisDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class TbContentServiceImpl implements TbContentService {
	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.bigpic.key}")
	private String bigPic;

	@Override
	public EasyUIDataGrid showByCategoryId(Integer pageNum, Integer pageSize, Long categoryId) {
		return tbContentDubboServiceImpl.showContent(pageNum, pageSize, categoryId);
	}

	@Override
	public EgoResult insertContent(TbContent tbContent) {
		EgoResult egoResult= new EgoResult();
		Date date =new Date();
		tbContent.setCreated(date);
		tbContent.setUpdated(date);
		int res = tbContentDubboServiceImpl.insertContent(tbContent);
		
		if (res==1) {
			//更新redis中的信息
			updateRedis(tbContent);
			
			egoResult.setStatus(EgoResultReason.OK_INSERT.getCode());
			egoResult.setMsg(EgoResultReason.OK_INSERT.getValue());
			
		}
		return egoResult;
	}
	
	/**
	 * 更新redis中数据
	 * @param tbContent
	 */
	private void updateRedis(TbContent tbContent) {
		
		String value = jedisDaoImpl.get(bigPic);
		System.out.println(value);
		if(StringUtils.isNotBlank(value)) {
			List<HashMap> jsonToList = JsonUtils.jsonToList(value, HashMap.class);
			//将更新信息转成map
			HashMap<String, Object> map = buildMap(tbContent);
			if(jsonToList.size()==6) {
				jsonToList.remove(5);
			}
			jsonToList.add(0, map);
				
			value = JsonUtils.objectToJson(jsonToList);
			String set = jedisDaoImpl.set(bigPic, value);
		}
	}
	
	/**
	 * 组装map元素
	 * @param tbContent
	 * @return
	 */
	private HashMap<String, Object> buildMap(TbContent tbContent) {
		HashMap<String , Object> map=Maps.newHashMap();
		map.put("srcB",tbContent.getPic2());
		map.put("height", 240);
		map.put("alt", "傻逼看不到");
		map.put("width", 670);
		map.put("src", tbContent.getPic());
		map.put("widthB",550);
		map.put("href", tbContent.getUrl());
		map.put("heigthB",240);
		return map;
	}

	@Override
	public EgoResult updateContent(TbContent tbContent) {
		EgoResult egoResult= new EgoResult();
		Date date =new Date();
		tbContent.setUpdated(date);
		int res = tbContentDubboServiceImpl.updateContent(tbContent);
		if (res==1) {
			updateRedis(tbContent);
			egoResult.setStatus(EgoResultReason.OK_UPDATE.getCode());
			egoResult.setMsg(EgoResultReason.OK_UPDATE.getValue());
			
		}
		return egoResult;
	}

	@Override
	public EgoResult deleteContents(String idStr) throws Exception {
		EgoResult egoResult=new EgoResult();
		String[] split = StringUtils.split(idStr, ",");
		List<Long> ids=Lists.newArrayList();
		for (String string : split) {
			ids.add(Long.parseLong(string));
		}
		int res = tbContentDubboServiceImpl.deleteContents(ids);
		if (res==ids.size()) {
			//更新redis中的数据
			List<TbContent> list = tbContentDubboServiceImpl.selectByCount(6, true);
			List<HashMap> mapList=Lists.newArrayList();
			for (TbContent tbContent : list) {
				mapList.add(buildMap(tbContent));
			}
			String value = JsonUtils.objectToJson(mapList);
			jedisDaoImpl.set(bigPic, value);
			
			egoResult.setStatus(EgoResultReason.OK_DELETE.getCode());
			egoResult.setMsg(EgoResultReason.OK_DELETE.getValue());
		}
		return egoResult;
	}
	
	
}
