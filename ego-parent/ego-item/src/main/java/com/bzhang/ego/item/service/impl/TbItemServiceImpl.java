package com.bzhang.ego.item.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbItemDubboService;
import com.bzhang.ego.item.service.TbItemService;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.redis.dao.JedisDao;
import com.bzhang.ego.vo.TbItemVo;

@Service
public class TbItemServiceImpl implements TbItemService{

	@Reference 
	TbItemDubboService tbItemDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.item.key}")
	private String itemKey;
	
	@Override
	public TbItemVo showItem(Long id) {
		
		String key=itemKey+id;
		
		//redis中是否有缓存
		if (jedisDaoImpl.exists(key)) {
			String res = jedisDaoImpl.get(key);
			System.out.println(res);
			if (StringUtils.isNotBlank(res)) {
				System.out.println("jinlaile");
				return JsonUtils.jsonToPojo(res, TbItemVo.class);
			}
		}
		
		TbItem item = tbItemDubboServiceImpl.selectById(id);
		TbItemVo tbItemVo=new TbItemVo();
		tbItemVo.setId(item.getId());
		tbItemVo.setTitle(item.getTitle());
		tbItemVo.setPrice(item.getPrice());
		tbItemVo.setSellPoint(item.getSellPoint());
		tbItemVo.setImages(item.getImage()==null&&"".equals(item.getImage())?new String[1]:item.getImage().split(","));
		
		//更新redis缓存
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String toJson = JsonUtils.objectToJson(tbItemVo);
				jedisDaoImpl.set(key, toJson);
			}
		}).start();
		
		
		return tbItemVo;
	}
	
}
