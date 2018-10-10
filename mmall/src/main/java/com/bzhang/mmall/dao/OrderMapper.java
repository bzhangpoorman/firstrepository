package com.bzhang.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    Order selectOrderByOrderNoAndUserId(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);
    
    Order selectOrderByOrderNo(Long orderNo);
    
    List<Order> selectByuserId(Integer userId);
    
    List<Order> selectAll();
    
}