package com.bzhang.ego.commons.pojo;

import java.io.Serializable;

/**
 * 商品类目目录数据传递格式
 * @author bzhang
 *
 */
public class EasyUITree implements Serializable{
	private Long id;
	
	private String text;
	
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public EasyUITree() {
		// TODO Auto-generated constructor stub
	}
}
