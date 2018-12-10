package com.bzhang.ego.cart.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.cart.service.CartService;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbItemDubboService;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbUser;
import com.bzhang.ego.redis.dao.JedisDao;
import com.bzhang.ego.vo.TbItemVo;
import com.google.common.collect.Lists;

/**
 * 购物车service层接口实现
 * @author bzhang
 *
 */
@Service
public class CartServiceImpl implements CartService{
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.user.key}")
	private String userKey;
	
	
	@Value("${redis.usercart.key}")
	private String cartKey;
	
	@Override
	public Boolean addToCart(Long id, Integer num,String uuid) {
		TbItem item = tbItemDubboServiceImpl.selectById(id);
		TbItemVo tbItemVo=new TbItemVo();
		if (item!=null) {
			tbItemVo.setId(item.getId());
			tbItemVo.setNum(num);
			tbItemVo.setStock(item.getNum());
			tbItemVo.setTitle(item.getTitle());
			tbItemVo.setPrice(item.getPrice());
			tbItemVo.setSellPoint(item.getSellPoint());
			tbItemVo.setImages(item.getImage()==null&&"".equals(item.getImage())?new String[1]:item.getImage().split(","));
			
		}else {
			return false;
		}
		
		String key=userKey+uuid;
		TbUser user = getUserFromRedis(key);
		
		//标志信息，判断购物车中是否已经存某商品
		boolean exist=false;
		
		if (user!=null) {
			String k=cartKey+user.getUsername();
			List<TbItemVo> list = null;
			if (jedisDaoImpl.exists(k)) {
				
				String cartJson = jedisDaoImpl.get(k);
				if (StringUtils.isNotBlank(cartJson)) {
					list = JsonUtils.jsonToList(cartJson, TbItemVo.class);
					for (TbItemVo itemVo : list) {
						if ((long)itemVo.getId()==tbItemVo.getId()) {
							itemVo.setNum(itemVo.getNum()+num);
							exist=true;
							break;
						}
					}
				}else {
					list = Lists.newArrayList();
				}
				
			}else {
				list = Lists.newArrayList();
			}
			if (!exist) {
				list.add(tbItemVo);
			}
			
			jedisDaoImpl.set(k, JsonUtils.objectToJson(list));
			return true;
		}
		return false;
	}

	private TbUser getUserFromRedis(String key) {
		if (jedisDaoImpl.exists(key)) {
			String res=jedisDaoImpl.get(key);
			if (StringUtils.isNotBlank(res)) {
				return JsonUtils.jsonToPojo(res, TbUser.class);
				
			}
		}
		return null;
	}

	@Override
	public List<TbItemVo> showCart(String uuid) {
		String key=userKey+uuid;
		TbUser user = getUserFromRedis(key);
		if (user!=null) {
			String k=cartKey+user.getUsername();
			
			if (jedisDaoImpl.exists(k)) {
				String cartJson = jedisDaoImpl.get(k);
				if (StringUtils.isNotBlank(cartJson)) {
					List<TbItemVo> list = JsonUtils.jsonToList(cartJson, TbItemVo.class);
					return list;
				}
				
			}
		}
		return null;
	}

	@Override
	public Boolean updateCart(Long id, Integer num, String uuid) {
		String key=userKey+uuid;
		TbUser user = getUserFromRedis(key);
		if (user!=null) {
			String k=cartKey+user.getUsername();
			
			if (jedisDaoImpl.exists(k)) {
				String cartJson = jedisDaoImpl.get(k);
				if (StringUtils.isNotBlank(cartJson)) {
					List<TbItemVo> list = JsonUtils.jsonToList(cartJson, TbItemVo.class);
					for (TbItemVo itemVo : list) {
						if ((long)itemVo.getId()==id) {
							itemVo.setNum(num);
							jedisDaoImpl.set(k, JsonUtils.objectToJson(list));
							return true;
						}
					}
				}
				
			}
		}
		
		return false;
	}

	@Override
	public Boolean deleteCartItem(Long id, String uuid) {
		String key=userKey+uuid;
		TbUser user = getUserFromRedis(key);
		if (user!=null) {
			String k=cartKey+user.getUsername();
			
			if (jedisDaoImpl.exists(k)) {
				String cartJson = jedisDaoImpl.get(k);
				if (StringUtils.isNotBlank(cartJson)) {
					List<TbItemVo> list = JsonUtils.jsonToList(cartJson, TbItemVo.class);
					for (TbItemVo itemVo : list) {
						if ((long)itemVo.getId()==id) {
							list.remove(itemVo);
							jedisDaoImpl.set(k, JsonUtils.objectToJson(list));
							return true;
						}
					}
				}
				
			}
		}
		
		return false;
	}

}
