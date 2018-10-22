package com.bzhang.ego.commons.pojo;

public class EgoResult {
	
	private int status;
	private String reason;
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
