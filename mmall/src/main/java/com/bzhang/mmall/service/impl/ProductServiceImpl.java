package com.bzhang.mmall.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ResponseCode;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.dao.CategoryMapper;
import com.bzhang.mmall.dao.ProductMapper;
import com.bzhang.mmall.pojo.Category;
import com.bzhang.mmall.pojo.Product;
import com.bzhang.mmall.service.CategoryService;
import com.bzhang.mmall.service.ProductService;
import com.bzhang.mmall.util.DateTimeUtil;
import com.bzhang.mmall.util.PropertiesUtil;
import com.bzhang.mmall.vo.ProductDetailVo;
import com.bzhang.mmall.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

@Service("productService")
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public ServerResponse saveOrUpdateProduct(Product product) {
		
		if (product!=null) {
			if (StringUtils.isNotBlank(product.getSubImages())) {
				String[] subImages = product.getSubImages().split(",");
				product.setMainImage(subImages[0]);
			}
			if (product.getId()!=null) {
				int count = productMapper.updateByPrimaryKeySelective(product);
				if (count>0) {
					return ServerResponse.createBySuccessMsg("更新商品成功！");
				}
				
				return ServerResponse.createByErrorMsg("更新失败，商品不存在！");
			}else {
				int insert = productMapper.insert(product);
				if (insert>0) {
					return ServerResponse.createBySuccessMsg("增加商品成功！");
				}
				
				return ServerResponse.createByErrorMsg("增加商品失败！");
			}
			
		
			
		}
		return ServerResponse.createByErrorMsg("参数错误，无法更新或增加商品！");
	}

	@Override
	public ServerResponse setProductStatus(Integer productId,Integer status) {
		
		if (productId==null||status==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			
		}
		int count = productMapper.updateStatusById(productId, status);
		if (count>0) {
			return ServerResponse.createBySuccessMsg("修改商品销售状态成功！");
		}
		
		return ServerResponse.createByErrorMsg("商品销售状态修改失败！");
	}

	@Override
	public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {

		if (productId==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			
		}

		Product product = productMapper.selectByPrimaryKey(productId);
		if (product!=null) {
			ProductDetailVo productDetailVo = assembleProductDetailVo(product);
			return ServerResponse.createBySuccess(productDetailVo);
		}
		
		return ServerResponse.createByErrorMsg("该商品不存在！");
	}
	
	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo=new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setName(product.getName());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setSubImages(product.getSubImages());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setStock(product.getStock());
		productDetailVo.setStatus(product.getStatus());
		
		
		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		
		productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
		
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if (category==null) {
			productDetailVo.setCategoryId(0);
		}
		productDetailVo.setParentCategoryId(category.getParentId());
		return productDetailVo;
		
	}

	@Override
	public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Product> list=productMapper.selectAllProduct();
		List<ProductListVo> productListVos=Lists.newArrayList();
		for (Product product : list) {
			productListVos.add(assembleProductListVo(product));
		}
		PageInfo pageInfo=new PageInfo<>(list);
		pageInfo.setList(productListVos);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	private ProductListVo assembleProductListVo(Product product) {
		ProductListVo productListVo=new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setName(product.getName());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setMainImage(product.getMainImage());
		productListVo.setPrice(product.getPrice());
		productListVo.setStatus(product.getStatus());
		
		productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
		
		return productListVo;
		
	}

	@Override
	public ServerResponse<PageInfo> searchProduct(String productName, Integer productId,Integer pageNum,Integer pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		if (productName==null) {
			productName="";
		}
		List<Product> list=productMapper.selectProductByNameOrId(productName, productId);
		List<ProductListVo> productListVos=Lists.newArrayList();
		for (Product product : list) {
			productListVos.add(assembleProductListVo(product));
		}
		PageInfo pageInfo=new PageInfo<>(list);
		pageInfo.setList(productListVos);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
		if (productId==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
			
		}

		Product product = productMapper.selectByPrimaryKey(productId);
		if (product!=null&&product.getStatus()==Const.PRODUCT_SALE) {
			ProductDetailVo productDetailVo = assembleProductDetailVo(product);
			return ServerResponse.createBySuccess(productDetailVo);
		}
		
		return ServerResponse.createByErrorMsg("该商品已下架或不存在！");
	}

	@Override
	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, Integer pageNum,
			Integer pageSize,String orderBy) {

		if (StringUtils.isBlank(keyword)&&categoryId==null) {
			return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		List<Integer> categoryIdList =Lists.newArrayList();
		if (categoryId!=null) {
			categoryIdList = categoryService.getCategoryAndDeepChildren(categoryId).getData();

		}
		if (categoryIdList.size()==0&&StringUtils.isBlank(keyword)) {
			PageHelper.startPage(pageNum, pageSize);
			List<ProductListVo> productListVos=Lists.newArrayList();
			PageInfo pageInfo=new PageInfo<>(productListVos);
			
			return ServerResponse.createBySuccess(pageInfo);
		}
		PageHelper.startPage(pageNum, pageSize);
		//PageHelper.orderBy(keyword);
		if (StringUtils.isBlank(orderBy)) {
			orderBy=Const.ProductListOrderBy.ORDER_BY.get(0);
		}
		if (Const.ProductListOrderBy.ORDER_BY.contains(orderBy)) {
			String[] order=orderBy.split("_");
			List<Product> productList=productMapper.selectProductByKeywordCategoryId(
					StringUtils.isBlank(keyword)?"":keyword, order[0],order[1], 
							categoryIdList.size()==0?null:categoryIdList);
			
			List<ProductListVo> productListVos=Lists.newArrayList();
			for (Product product : productList) {
				System.out.println(product.getPrice());
				productListVos.add(assembleProductListVo(product));
			}
			PageInfo pageInfo=new PageInfo<>(productList);
			pageInfo.setList(productListVos);
			return ServerResponse.createBySuccess(pageInfo);
		}
		return ServerResponse.createByErrorMsg("排序参数不合法");
	}
}
