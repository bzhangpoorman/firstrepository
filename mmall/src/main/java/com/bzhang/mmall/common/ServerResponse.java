package com.bzhang.mmall.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * service类的返回类型，带有返回结果
 * @author bzhang
 *
 * @param <T>
 */
@JsonSerialize(include =JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
	private int status;
	private String msg;
	private T data;
	private ServerResponse(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	private ServerResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	private ServerResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}
	private ServerResponse(int status) {
		this.status = status;
	}
	
	@JsonIgnore
	public boolean isSuccess() {
		return this.status==ResponseCode.SUCCESS.getCode();
	}
	public int getStatus() {
		return status;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public T getData() {
		return data;
	}
	
	public static <T> ServerResponse<T> createBySuccess(){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
	}
	public static <T> ServerResponse<T> createBySuccessMsg(String msg){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
	}
	public static <T> ServerResponse<T> createBySuccess(T data){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
	}
	public static <T> ServerResponse<T> createBySuccess(String msg,T data){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
	}
	public static <T> ServerResponse<T> createByError(){
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
	}
	public static <T> ServerResponse<T> createByErrorMsg(String errorMsg){
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
	}
	public static <T> ServerResponse<T> createByErrorCodeMsg(int errorCode,String errorMsg){
		return new ServerResponse<T>(errorCode,errorMsg);
	}
}
