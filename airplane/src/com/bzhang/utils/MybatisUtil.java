package com.bzhang.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {
	private static SqlSessionFactory sessionFactory;
	private static ThreadLocal<SqlSession> tlLocal=new ThreadLocal<>();
	static {
		try {
			InputStream is=Resources.getResourceAsStream("mybatis.xml");
			sessionFactory=new SqlSessionFactoryBuilder().build(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SqlSession getSqlSession() {
		if (tlLocal.get()==null) {
			tlLocal.set(sessionFactory.openSession());
		}
		return tlLocal.get();
	}
	public static void closeSqlSession() {
		SqlSession sqlSession=tlLocal.get();
		if (sqlSession!=null) {
			sqlSession.close();
		}
		tlLocal.set(null);
	}
}
