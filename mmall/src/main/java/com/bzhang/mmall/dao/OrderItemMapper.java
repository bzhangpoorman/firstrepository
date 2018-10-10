package com.bzhang.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.pojo.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
    
    List<OrderItem> selectOrderItemByUserIdAndOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);
    
    OrderItem selectByUserIdAndProductId(@Param("userId")Integer userId,@Param("productId")Integer productId);
    
    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
    
    List<OrderItem> selectByOrderNo(Long orderNo);
    
    
    
}