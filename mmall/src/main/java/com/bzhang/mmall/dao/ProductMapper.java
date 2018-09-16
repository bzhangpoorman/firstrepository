package com.bzhang.mmall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bzhang.mmall.pojo.Product;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    
    int updateStatusById(@Param("id")Integer id,@Param("status")Integer status);
    
    List<Product> selectAllProduct();
    
    List<Product> selectProductByNameOrId(@Param("productName")String name,@Param("productId")Integer id);
    
    List<Product> selectProductByKeywordCategoryId(@Param("keyword")String keyword,
    		@Param("orderByName")String orderByName,@Param("orderByType")String orderByType,
    		@Param("categoryIdList")List<Integer> categoryIdList);
    
    int selectStockById(Integer id);
    
}