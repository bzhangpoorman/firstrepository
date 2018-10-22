package com.bzhang.ego.manage.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.dubbo.service.TbContentDubboService;
import com.bzhang.ego.manage.service.TbContentService;
import com.bzhang.ego.pojo.TbContent;
import com.google.common.collect.Lists;

@Service
public class TbContentServiceImpl implements TbContentService {
	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;

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
			egoResult.setStatus(EgoResultReason.OK_INSERT.getCode());
			egoResult.setReason(EgoResultReason.OK_INSERT.getValue());
			
		}
		return egoResult;
	}

	@Override
	public EgoResult updateContent(TbContent tbContent) {
		EgoResult egoResult= new EgoResult();
		Date date =new Date();
		tbContent.setUpdated(date);
		int res = tbContentDubboServiceImpl.updateContent(tbContent);
		if (res==1) {
			egoResult.setStatus(EgoResultReason.OK_UPDATE.getCode());
			egoResult.setReason(EgoResultReason.OK_UPDATE.getValue());
			
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
			egoResult.setStatus(EgoResultReason.OK_DELETE.getCode());
			egoResult.setReason(EgoResultReason.OK_DELETE.getValue());
		}
		return egoResult;
	}
	
	
}
