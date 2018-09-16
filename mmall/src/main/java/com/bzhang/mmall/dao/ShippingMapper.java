package com.bzhang.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.pojo.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    List<Shipping> selectShippingByUserId(Integer userId);

	int deleteByUserIdAndShippingId(@Param("userId")Integer userId, @Param("shippingId")Integer shippingId);

	Shipping selectByUserIdAndShippingId(@Param("userId")Integer userId, @Param("shippingId")Integer shippingId);
}