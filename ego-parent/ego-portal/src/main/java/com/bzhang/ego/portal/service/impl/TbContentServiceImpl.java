package com.bzhang.ego.portal.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbContentDubboService;
import com.bzhang.ego.pojo.TbContent;
import com.bzhang.ego.portal.service.TbContentService;
import com.bzhang.ego.redis.dao.JedisDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class TbContentServiceImpl implements TbContentService{
	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.bigpic.key}")
	private String bigPic;
	
	@Override
	public String showBigPic() {
		if (jedisDaoImpl.exists(bigPic)) {
			String value = jedisDaoImpl.get(bigPic);
			if (StringUtils.isNotBlank(value)) {
				return value;
			}
		}
		List<TbContent> list = tbContentDubboServiceImpl.selectByCount(6, true);
		List<Map<String, Object>> listResult=Lists.newArrayList();
		for (TbContent tc : list) {
			Map<String , Object> map=Maps.newHashMap();
			map.put("srcB",tc.getPic2());
			map.put("height", 240);
			map.put("alt", "傻逼看不到");
			map.put("width", 670);
			map.put("src", tc.getPic());
			map.put("widthB",550);
			map.put("href", tc.getUrl());
			map.put("heigthB",240);
			listResult.add(map);
		}
		String json = JsonUtils.objectToJson(listResult);
		jedisDaoImpl.set(bigPic, json);
		return json;
	}

}
