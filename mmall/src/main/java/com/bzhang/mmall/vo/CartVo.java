package com.bzhang.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
	private List<CartProductVo> CartProductVolist;
	private BigDecimal cartTotalPrice;
	private boolean isAllChecked;
	private String imageHost;
	private int productCount;
	public int getCheckedCount() {
		return checkedCount;
	}
	public void setCheckedCount(int checkedCount) {
		this.checkedCount = checkedCount;
	}
	private int checkedCount;
	
	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public List<CartProductVo> getCartProductVolist() {
		return CartProductVolist;
	}
	public void setCartProductVolist(List<CartProductVo> cartProductVolist) {
		CartProductVolist = cartProductVolist;
	}
	public BigDecimal getCartTotalPrice() {
		return cartTotalPrice;
	}
	public void setCartTotalPrice(BigDecimal cartTotalPrice) {
		this.cartTotalPrice = cartTotalPrice;
	}
	public boolean isAllChecked() {
		return isAllChecked;
	}
	public void setAllChecked(boolean isAllChecked) {
		this.isAllChecked = isAllChecked;
	}
	public String getImageHost() {
		return imageHost;
	}
	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}
	
	
}
