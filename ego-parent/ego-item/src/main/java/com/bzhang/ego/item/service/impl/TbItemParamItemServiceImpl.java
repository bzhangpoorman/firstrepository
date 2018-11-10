package com.bzhang.ego.item.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.dubbo.service.TbItemParamItemDubboService;
import com.bzhang.ego.item.pojo.ParamItemVo;
import com.bzhang.ego.item.pojo.ParamNode;
import com.bzhang.ego.item.service.TbItemParamItemService;
import com.bzhang.ego.pojo.TbItemParamItem;
import com.bzhang.ego.redis.dao.JedisDao;

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService{
	
	@Reference
	private TbItemParamItemDubboService tbItemParamItemDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.param.key}")
	private String paramItemKey;
	
	@Override
	public String showParamItem(Long itemId) {
		String key=paramItemKey+itemId;
		if (jedisDaoImpl.exists(key)) {
			String json = jedisDaoImpl.get(key);
			if (StringUtils.isNotBlank(json)) {
				return json;
			}
		}
		
		TbItemParamItem paramItem = tbItemParamItemDubboServiceImpl.selectByItemId(itemId);
		List<ParamItemVo> list = JsonUtils.jsonToList(paramItem.getParamData(), ParamItemVo.class);
		StringBuilder sb=new StringBuilder();
		sb.append("<table width='500' style='color:gray'>");
		for (ParamItemVo paramItemVo : list) {
			
			List<ParamNode> paramNodes = paramItemVo.getParams();
			for (int i = 0; i < paramItemVo.getParams().size(); i++) {
				sb.append("<tr>");
				if (i==0) {
					sb.append("<td align='right' width='30%'>").append(paramItemVo.getGroup()).append("</td>");
					
				}else {
					sb.append("<td>").append("</td>");
					
				}
				sb.append("<td align='right'>").append(paramNodes.get(i).getK()).append(":</td>");
				sb.append("<td>").append(paramNodes.get(i).getV()).append("</td>");
				sb.append("</tr>");
			}
			sb.append("<hr style='color:blue'/>");
			
		}
		sb.append("</table>");
		
		
		if (paramItem!=null) {
			jedisDaoImpl.set(key, sb.toString());
		}
		
		return sb.toString();
	}

}
