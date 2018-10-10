package com.bzhang.mmall.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzhang.mmall.common.Const;
import com.bzhang.mmall.common.ServerResponse;
import com.bzhang.mmall.dao.CartMapper;
import com.bzhang.mmall.dao.ProductMapper;
import com.bzhang.mmall.pojo.Cart;
import com.bzhang.mmall.pojo.Product;
import com.bzhang.mmall.service.CartService;
import com.bzhang.mmall.service.ProductService;
import com.bzhang.mmall.util.BigDecimalUtil;
import com.bzhang.mmall.util.PropertiesUtil;
import com.bzhang.mmall.vo.CartProductVo;
import com.bzhang.mmall.vo.CartVo;
import com.bzhang.mmall.vo.ProductDetailVo;
import com.google.common.collect.Lists;

@Service("cartService")
public class CartServiceImpl implements CartService{
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ProductService productService;

	@Override
	public ServerResponse<CartVo> addProduct(Integer userId,Integer productId, Integer count) {
		if (productId==null||count<=0) {
			return ServerResponse.createByErrorMsg("参数错误！");
		}
		Cart resCart = cartMapper.selectByUserIdAndProductId(userId, productId);
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product!=null&&product.getStatus()==Const.PRODUCT_SALE) {
			if (resCart!=null) {
				count=count+resCart.getQuantity();
				resCart.setQuantity(count);
				int updcount = cartMapper.updateByPrimaryKeySelective(resCart);
				if (updcount>0) {
					CartVo cartVo=this.getCartVoLimit(userId);
					return ServerResponse.createBySuccess("更新购物车成功",cartVo);
				}
				return ServerResponse.createByErrorMsg("更新失败！");
			}else {
				count=checkStock(productId, count);
				resCart =new Cart();
				resCart.setUserId(userId);
				resCart.setChecked(Const.Cart.DEFAULT_CART_CHECKED);
				resCart.setQuantity(count);
				resCart.setProductId(productId);
				int insert = cartMapper.insert(resCart);
				if (insert>0) {
					CartVo cartVo=this.getCartVoLimit(userId);
					return ServerResponse.createBySuccess("添加到购物车成功",cartVo);
				}
				return ServerResponse.createByErrorMsg("添加失败！");
			}
		}
		return ServerResponse.createByErrorMsg("商品不存在或已下架！");
		
	}
	
	private int checkStock(Integer productId,Integer count) {
		int stock = productMapper.selectStockById(productId);
		return (count>stock)?stock:count;
	}
	
	private CartVo getCartVoLimit(Integer userId) {
		List<Cart> cartList=cartMapper.selectCartByUserId(userId);
		CartVo cartVo=new CartVo();
		cartVo.setProductCount(0);
		cartVo.setCheckedCount(0);
		cartVo.setCartTotalPrice(new BigDecimal("0"));
		boolean isAllChecked=true;
		List<CartProductVo> cartProductVoList=Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(cartList)) {
			for (Cart cart : cartList) {
				
				ProductDetailVo productDetailVo = productService.getProductDetail(cart.getProductId()).getData();
				System.out.println(cart.getProductId());
				if (productDetailVo!=null&&productDetailVo.getStatus()==Const.PRODUCT_SALE) {
					CartProductVo cartProductVo=new CartProductVo();
					cartProductVo.setId(cart.getId());
					cartProductVo.setUserId(cart.getUserId());
					cartProductVo.setChecked(cart.getChecked());
					
					cartProductVo.setProductId(productDetailVo.getId());
					cartProductVo.setProductName(productDetailVo.getName());
					cartProductVo.setProductSubtitle(productDetailVo.getSubtitle());
					cartProductVo.setProductMainImage(productDetailVo.getMainImage());
					cartProductVo.setProductPrice(productDetailVo.getPrice());
					cartProductVo.setProductStatus(productDetailVo.getStatus());
					cartProductVo.setProductStock(productDetailVo.getStock());
					
					if (cart.getQuantity()>productDetailVo.getStock()) {
						cartProductVo.setQuantity(productDetailVo.getStock());
						cart.setQuantity(productDetailVo.getStock());
						cartMapper.updateByPrimaryKeySelective(cart);
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAILED);
					}else {
						cartProductVo.setQuantity(cart.getQuantity());
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
					}
					
					BigDecimal productTotalPrice = BigDecimalUtil.mul(cartProductVo.getQuantity().doubleValue(), cartProductVo.getProductPrice().doubleValue());
					cartProductVo.setProductTotalPrice(productTotalPrice);
					
					//计算选中商品总价
					if (cart.getChecked()==Const.Cart.CART_CHECKED) {
						System.out.println(cartProductVo.getProductTotalPrice().doubleValue());
						System.out.println(cartVo.getCartTotalPrice().doubleValue());
						BigDecimal totalPrice= BigDecimalUtil.add(cartVo.getCartTotalPrice().doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
						cartVo.setCartTotalPrice(totalPrice);
						cartVo.setCheckedCount(cartVo.getCheckedCount()+cart.getQuantity());
					}
					//是否全选
					if (cartProductVo.getChecked()==Const.Cart.CART_UNCHECKED) {
						isAllChecked=false;
					}
					cartVo.setProductCount(cartVo.getProductCount()+cart.getQuantity());
					cartProductVoList.add(cartProductVo);
				}else {
					cartMapper.deleteByPrimaryKey(cart.getId());
				}
				
			}
		}
		cartVo.setCartProductVolist(cartProductVoList);
		cartVo.setAllChecked(isAllChecked);
		cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		return cartVo;
		
		
	}

	@Override
	public ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count) {
		if (productId==null||count<=0) {
			return ServerResponse.createByErrorMsg("参数错误！");
		}
		Cart resCart = cartMapper.selectByUserIdAndProductId(userId, productId);
		if (resCart!=null) {
			resCart.setQuantity(count);
			int updcount = cartMapper.updateByPrimaryKeySelective(resCart);
			if (updcount>0) {
				CartVo cartVo=this.getCartVoLimit(userId);
				return ServerResponse.createBySuccess("更新购物车成功",cartVo);
			}
			return ServerResponse.createByErrorMsg("更新失败！");
		}
		return ServerResponse.createByErrorMsg("商品不存在！");
		
	}


	@Override
	public ServerResponse<CartVo> deleteCart(Integer userId, String  productIds) {
		if (StringUtils.isBlank(productIds)) {
			return ServerResponse.createByErrorMsg("参数错误！");
		}
		String[] pIdsArr=productIds.split(",");
		List<String> pIdsList=Arrays.asList(pIdsArr);
		System.out.println(pIdsList.toString());
		int res = cartMapper.deleteByUserIdAndProductId(userId, pIdsList);
		if (res>0) {
			CartVo cartVo=this.getCartVoLimit(userId);
			return ServerResponse.createBySuccess("删除商品成功",cartVo);
		}
		return ServerResponse.createByErrorMsg("删除失败！，购物车中不存在该商品");
	}

	@Override
	public ServerResponse<CartVo> listCart(Integer userId) {
		CartVo cartVo=this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}

	@Override
	public ServerResponse<CartVo> selectAllOrNot(Integer userId, int cartChecked,Integer productId) {
		cartMapper.updateCheckedByUserId(userId, cartChecked,productId);
		return listCart(userId);
	}

	@Override
	public ServerResponse<Integer> getCartProductNum(Integer userId) {
		int productCount = listCart(userId).getData().getProductCount();
		return ServerResponse.createBySuccess(productCount);
	}

}
