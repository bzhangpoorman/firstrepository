package com.bzhang.ego.redis.dao;

public interface JedisDao {
	
	/**
	 * key是否存在
	 * @param key
	 * @return
	 */
	Boolean exists(String key);
	
	/**
	 * 删除key
	 * @param key
	 * @return
	 */
	Long del(String key);
	
	/**
	 * 新增数据
	 * @param key
	 * @param value
	 * @return
	 */
	String set(String key,String value);
	
	/**
	 * 查询值
	 * @param key
	 * @return
	 */
	String get(String key);
	/**
	 * 设置key过期时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	Long expire(String key,int seconds);
	
}
