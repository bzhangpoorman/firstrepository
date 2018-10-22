package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.dubbo.service.TbItemParamDubboService;
import com.bzhang.ego.mapper.TbItemParamMapper;
import com.bzhang.ego.pojo.TbItemParam;
import com.bzhang.ego.pojo.TbItemParamExample;
import com.bzhang.ego.pojo.TbItemParamExample.Criteria;
import com.bzhang.ego.vo.TbItemParamVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService{
	@Resource
	private TbItemParamMapper tbItemParamMapper;
	
	@Override
	public EasyUIDataGrid show(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<TbItemParamVo> list = tbItemParamMapper.selecTbItemParamVos();
		PageInfo<TbItemParamVo> pageInfo=new PageInfo<>(list);
		EasyUIDataGrid easyUIDataGrid=new EasyUIDataGrid();
		easyUIDataGrid.setRows(pageInfo.getList());
		easyUIDataGrid.setTotal(pageInfo.getTotal());
		return easyUIDataGrid;
	}

	@Override
	public int deleteById(Long id) {
		return tbItemParamMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public int deleteByIds(String ids) {
		String[] strings = StringUtils.split(ids, ",");
		List<Long> idList=Lists.newArrayList();
		for (String string : strings) {
			idList.add(Long.parseLong(string));
		}
		return tbItemParamMapper.deleteByIdList(idList);
		
	}

	@Override
	public TbItemParam selectByItemCatId(Long itemCatId) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andItemCatIdEqualTo(itemCatId);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int insertItemParam(TbItemParam tbItemParam) {
		return tbItemParamMapper.insertSelective(tbItemParam);
		
	}

}
