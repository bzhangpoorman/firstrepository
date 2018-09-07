package com.bzhang.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.bzhang.mapper.AirportMapper;
import com.bzhang.pojo.Airport;
import com.bzhang.service.AirportService;
import com.bzhang.utils.MybatisUtil;

public class AirportServiceImpl implements AirportService {

	@Override
	public List<Airport> showTakePort() {
		SqlSession session=MybatisUtil.getSqlSession();
		AirportMapper airportMapper=session.getMapper(AirportMapper.class);
		return airportMapper.selTakePort();
	}

	@Override
	public List<Airport> showLandPort() {
		SqlSession session=MybatisUtil.getSqlSession();
		AirportMapper airportMapper=session.getMapper(AirportMapper.class);
		return airportMapper.selLandPort();
	}

}
