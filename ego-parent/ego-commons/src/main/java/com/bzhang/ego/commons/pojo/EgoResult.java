package com.bzhang.ego.commons.pojo;

public class EgoResult {
	
	private int status;
	private String msg;
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String reason) {
		this.msg = reason;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public EgoResult() {
		// TODO Auto-generated constructor stub
	}
}
