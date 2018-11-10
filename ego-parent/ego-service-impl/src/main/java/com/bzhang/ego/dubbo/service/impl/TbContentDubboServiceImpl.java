package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.dubbo.service.TbContentDubboService;
import com.bzhang.ego.mapper.TbContentMapper;
import com.bzhang.ego.pojo.TbContent;
import com.bzhang.ego.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbContentDubboServiceImpl implements TbContentDubboService{
	@Resource
	private TbContentMapper tbContentMapper;

	@Override
	public EasyUIDataGrid showContent(Integer pageNum,Integer pageSize,Long categoryId) {
		PageHelper.startPage(pageNum,pageSize);
		TbContentExample example=new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		PageInfo<TbContent> pageInfo= new PageInfo<>(list);
		EasyUIDataGrid easyUIDataGrid=new EasyUIDataGrid();
		easyUIDataGrid.setRows(pageInfo.getList());
		easyUIDataGrid.setTotal(pageInfo.getTotal());
		return easyUIDataGrid;
		
		
	}

	@Override
	public int insertContent(TbContent tbContent) {
		return tbContentMapper.insertSelective(tbContent);
	}

	@Override
	public int updateContent(TbContent tbContent) {
		return tbContentMapper.updateByPrimaryKeySelective(tbContent);
	}

	@Override
	public int deleteContents(List<Long> ids) throws Exception {
		int res=0;
		try {
			for (Long id : ids) {
				res+=tbContentMapper.deleteByPrimaryKey(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res==ids.size()) {
			return res;
		}else {
			throw new Exception("删除数据失败");
		}
	}

	@Override
	public List<TbContent> selectByCount(Integer count, Boolean isSort) {
		TbContentExample example =new TbContentExample();
		if (isSort) {
			example.setOrderByClause("updated desc");
		}
		
		if (count>0) {
			//查询最新的6个数据
			PageHelper.startPage(1, 6);
			List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
			PageInfo<TbContent> pageInfo=new PageInfo<>(list);
			return pageInfo.getList();
		}else {
			//查询全部
			return tbContentMapper.selectByExampleWithBLOBs(example);
		}
		
		
	}
	
}
