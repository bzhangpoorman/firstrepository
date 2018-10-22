package com.bzhang.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bzhang.ego.dubbo.service.TbContentCategoryDubboService;
import com.bzhang.ego.mapper.TbContentCategoryMapper;
import com.bzhang.ego.pojo.TbContentCategory;
import com.bzhang.ego.pojo.TbContentCategoryExample;
import com.bzhang.ego.pojo.TbContentCategoryExample.Criteria;

public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService{
	@Resource
	private TbContentCategoryMapper tbContentCategoryMapper;

	@Override
	public List<TbContentCategory> selectAll(Long pid) {
		TbContentCategoryExample tbContentCategoryExample=new TbContentCategoryExample();
		tbContentCategoryExample.createCriteria().andParentIdEqualTo(pid).andStatusEqualTo(1);
		return tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
	}

	@Override
	public int insertAndUpdateContentCategory(TbContentCategory newContentCategory) throws Exception {
		int res = 0;
		TbContentCategoryExample example=new TbContentCategoryExample();
		example.createCriteria().andIdEqualTo(newContentCategory.getParentId()).andStatusEqualTo(1);
		
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		TbContentCategory parent=list.get(0);
		
		Boolean isParent = parent.getIsParent();
		try {
			if (!isParent) {
				parent.setIsParent(true);
				parent.setUpdated(newContentCategory.getUpdated());
				res+=tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
			}
			res+=tbContentCategoryMapper.insertSelective(newContentCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ((isParent==true&&res==1)||(isParent==false&&res==2)) {
			return 1;
		}else {
			throw new Exception("新增内容分类信息失败");
		}
	}

	@Override
	public int updateContentCategory(TbContentCategory tbContentCategory) {
		return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
	}

	@Override
	public Long selectParentIdById(Long id) {
		TbContentCategoryExample example=new TbContentCategoryExample();
		example.createCriteria().andIdEqualTo(id).andStatusEqualTo(1);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		
		return list.get(0).getParentId();
	}
	
	
}
