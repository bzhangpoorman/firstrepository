package com.bzhang.ego.manage.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.dubbo.service.TbItemParamDubboService;
import com.bzhang.ego.manage.service.TbItemParamService;
import com.bzhang.ego.pojo.TbItemParam;

@Service
public class TbItemParamServiceImpl implements TbItemParamService{
	@Reference
	private TbItemParamDubboService tbItemParamDubboServiceImpl;
	
	@Override
	public EasyUIDataGrid show(Integer pageNum, Integer pageSize) {
		return tbItemParamDubboServiceImpl.show(pageNum, pageSize);
	}

	@Override
	public int deleteParams(String ids) {
		//效率低下，可在sql中用in (id1,id2...)来提高效率
		/*String[] strings = StringUtils.split(ids, ",");
		int res = 0;
		for (String idStr : strings) {
			res = tbItemParamDubboServiceImpl.deleteById(Long.parseLong(idStr));
			if (res<=0) {
				return res;
			}
		}*/
		return tbItemParamDubboServiceImpl.deleteByIds(ids);
		
	}

	@Override
	public TbItemParam selectByItemCatId(Long itemCatId) {
		return tbItemParamDubboServiceImpl.selectByItemCatId(itemCatId);
	}

	@Override
	public int insertItemParam(Long itemCatId, String paramData) {
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setItemCatId(itemCatId);
		tbItemParam.setParamData(paramData);
		Date date=new Date();
		tbItemParam.setCreated(date);
		tbItemParam.setUpdated(date);
		return tbItemParamDubboServiceImpl.insertItemParam(tbItemParam);
	}

}
