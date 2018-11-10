package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.bzhang.ego.commons.pojo.EasyUIDataGrid;
import com.bzhang.ego.dubbo.service.TbItemDubboService;
import com.bzhang.ego.mapper.TbItemDescMapper;
import com.bzhang.ego.mapper.TbItemMapper;
import com.bzhang.ego.mapper.TbItemParamItemMapper;
import com.bzhang.ego.pojo.TbItem;
import com.bzhang.ego.pojo.TbItemDesc;
import com.bzhang.ego.pojo.TbItemExample;
import com.bzhang.ego.pojo.TbItemParamItem;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemDubboServiceImpl implements TbItemDubboService{
	@Resource
	private TbItemMapper tbItemMapper;
	
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	
	@Override
	public EasyUIDataGrid show(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<TbItem> list = tbItemMapper.selectByExample(new TbItemExample());
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGrid easyUIDataGrid=new EasyUIDataGrid();
		easyUIDataGrid.setRows(pageInfo.getList());
		easyUIDataGrid.setTotal(pageInfo.getTotal());
		
		return easyUIDataGrid;
	}


	@Override
	public int updateItemStatus(TbItem tbItem) {
		return tbItemMapper.updateByPrimaryKeySelective(tbItem);
	}


	@Override
	public int insertItem(TbItem tbItem) {
		return tbItemMapper.insert(tbItem);
	}


	@Override
	public int insertItemAndItemDesc(TbItem tbItem, TbItemDesc tbItemDesc) throws Exception {
		int res = 0;
		try {
			res=tbItemMapper.insertSelective(tbItem);
			res+=tbItemDescMapper.insert(tbItemDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res==2) {
			return 1;
		}else {
			throw new Exception("新增商品及描述失败！");
		}
			
	}


	@Override
	public int insertItemAndDescAndParamItem(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParamItem)
			throws Exception {
		int res = 0;
		try {
			res=tbItemMapper.insertSelective(tbItem);
			res+=tbItemDescMapper.insert(tbItemDesc);
			res+=tbItemParamItemMapper.insertSelective(tbItemParamItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res==3) {
			return 1;
		}else {
			throw new Exception("新增商品,描述及规格参数失败！");
		}
	}


	@Override
	public List<TbItem> selectAllByStatus(Byte status) {
		TbItemExample example= new TbItemExample();
		example.createCriteria().andStatusEqualTo(status);
		
		return tbItemMapper.selectByExample(example);
	}


	@Override
	public TbItem selectById(Long id) {
		TbItemExample example=new TbItemExample();
		example.createCriteria().andIdEqualTo(id).andStatusEqualTo((byte)1);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}


	

}
