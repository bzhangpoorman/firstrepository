package com.bzhang.ego.commons.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 后台商品管理页面所需返回数据，包含商品分页信息
 * @author bzhang
 *
 */
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
	}
}
