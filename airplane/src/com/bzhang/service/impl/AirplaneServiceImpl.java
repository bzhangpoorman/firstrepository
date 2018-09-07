package com.bzhang.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.bzhang.mapper.AirplaneMapper;
import com.bzhang.pojo.Airplane;
import com.bzhang.pojo.Airport;
import com.bzhang.service.AirplaneService;
import com.bzhang.utils.MybatisUtil;

public class AirplaneServiceImpl implements AirplaneService{

	@Override
	public List<Airplane> show(int takeid, int landid) {
		SqlSession session=MybatisUtil.getSqlSession();
		System.out.println(takeid+"************"+landid);
		List<Airplane> list=session.getMapper(AirplaneMapper.class).selByTakeidLandid(takeid, landid);
		return list;
	}

}
