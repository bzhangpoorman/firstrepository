package com.bzhang.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.pojo.User;
import com.bzhang.mmall.service.ProductService;
import com.bzhang.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/product/")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping("product_detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVo> productDetail(Integer productId) {
			return productService.getProductDetail(productId);
	}
	

	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> seacrhProduct(
			@RequestParam(value="keyword",required=false)String keyword,
			@RequestParam(value="categoryId",required=false)Integer categoryId,
			@RequestParam(value="pageNum",defaultValue="1")Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,
			@RequestParam(value="orderBy",defaultValue="")String orderBy) {
		
		return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize,orderBy);
	}
}
