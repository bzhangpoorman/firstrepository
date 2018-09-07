package com.bzhang.pojo;

public class Airplane {
	private int id;
	private String airNo;
	private int time;
	private double price;
	private int takeId;
	private int landId;
	
	private Airport takePort;
	private Airport landPort;
	
	public Airport getTakePort() {
		return takePort;
	}
	public void setTakePort(Airport takePort) {
		this.takePort = takePort;
	}
	public Airport getLandPort() {
		return landPort;
	}
	public void setLandPort(Airport landPort) {
		this.landPort = landPort;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAirNo() {
		return airNo;
	}
	public void setAirNo(String airNo) {
		this.airNo = airNo;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getTakeId() {
		return takeId;
	}
	public void setTakeId(int takeId) {
		this.takeId = takeId;
	}
	public int getLandId() {
		return landId;
	}
	public void setLandId(int landId) {
		this.landId = landId;
	}
	
	@Override
	public String toString() {
		return "Airplane [id=" + id + ", airNo=" + airNo + ", time=" + time + ", price=" + price + ", takeId=" + takeId
				+ ", landId=" + landId + ", takePort=" + takePort + ", landPort=" + landPort + "]";
	}
	public Airplane() {
		// TODO Auto-generated constructor stub
	}
}
