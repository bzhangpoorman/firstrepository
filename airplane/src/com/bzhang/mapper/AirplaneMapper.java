package com.bzhang.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bzhang.pojo.Airplane;
import com.bzhang.pojo.Airport;

public interface AirplaneMapper {
	List<Airplane> selByTakeidLandid(@Param("takeid")int takeid,@Param("landid")int landid);
}
