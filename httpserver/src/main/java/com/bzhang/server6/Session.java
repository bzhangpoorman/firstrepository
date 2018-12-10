package com.bzhang.server6;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * session容器
 * @author bzhang
 *
 */
public class Session {
	//session的id
	private String id;
	//存放数据容器
	private Map<String, Object> attribute;
	//session生成时间
	private long createTime;
	//客户端最后一次访问时间
	private long lastAccessedTime;
	//session存活时间
	private long maxInactiveInterval;

	
	public Session() {
		id=UUID.randomUUID().toString();
		attribute=new HashMap<String, Object>();
		createTime=System.currentTimeMillis();
		lastAccessedTime=System.currentTimeMillis();
		maxInactiveInterval=40*1000;
		
	}
	
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}
	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Object getAttribute(String name) {
		return attribute.get(name);
	}
	public void setAttribute(String name,Object value) {
		attribute.put(name, value);
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	
}
