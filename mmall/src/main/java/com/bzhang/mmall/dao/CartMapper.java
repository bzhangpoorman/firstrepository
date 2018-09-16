package com.bzhang.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.pojo.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    
    Cart selectByUserIdAndProductId(@Param("userId")Integer userId,@Param("productId")Integer productId);
    
    List<Cart> selectCartByUserId(Integer userId);
    
    int deleteByUserIdAndProductId(@Param("userId")Integer userId,@Param("pIdsList")List<String> pIdsList);
    
    int updateCheckedByUserId(@Param("userId")Integer userId,@Param("checked")Integer checked,@Param("productId")Integer productId);
    

}