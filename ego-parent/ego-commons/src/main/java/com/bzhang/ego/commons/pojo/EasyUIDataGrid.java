package com.bzhang.ego.commons.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGrid implements Serializable{
	//当前页数据
	private List<?> rows;
	//总条数
	private Long total;
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public EasyUIDataGrid() {
		// TODO Auto-generated constructor stub
	}
}
