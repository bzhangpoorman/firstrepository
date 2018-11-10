package com.bzhang.ego.vo;

import com.bzhang.ego.pojo.TbItem;

public class TbItemVo extends TbItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String [] images;
	private Integer stock;
	private Boolean enough;

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Boolean getEnough() {
		return enough;
	}

	public void setEnough(Boolean enough) {
		this.enough = enough;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}
	
}
