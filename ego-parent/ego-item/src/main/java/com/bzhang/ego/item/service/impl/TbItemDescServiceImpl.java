package com.bzhang.ego.item.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.dubbo.service.TbItemDescDubboService;
import com.bzhang.ego.item.service.TbItemDescService;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.redis.dao.JedisDao;
import com.bzhang.ego.redis.dao.impl.JedisDaoImpl;

@Service
public class TbItemDescServiceImpl implements TbItemDescService{
	
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.desc.key}")
	private String descKey;
	
	@Override
	public String showDesc(Long itemId) {
		String key=descKey+itemId;
		if (jedisDaoImpl.exists(key)) {
			String json = jedisDaoImpl.get(key);
			if (StringUtils.isNotBlank(json)) {
				return json;
			}
		}
		
		String desc = tbItemDescDubboServiceImpl.selectByItemId(itemId).getItemDesc();
		jedisDaoImpl.set(key, desc);
		return desc;
	}

}
