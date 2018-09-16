package com.bzhang.mmall.service;

import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.Product;
import com.bzhang.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface ProductService {
	ServerResponse saveOrUpdateProduct(Product product);
	
	ServerResponse setProductStatus(Integer productId,Integer status);
	
	ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
	
	ServerResponse<PageInfo> getProductList(Integer pageNum,Integer pageSize);
	
	ServerResponse<PageInfo> searchProduct(String productName,Integer productId,Integer pageNum,Integer pageSize);
	
	ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

	ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum,
			Integer pageSize, String orderBy);
	
}
