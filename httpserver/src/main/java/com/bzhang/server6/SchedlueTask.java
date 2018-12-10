package com.bzhang.server6;

import java.util.Collection;
import java.util.Map;

/**
 * 定时任务，每个一定时间t清除过期session
 * @author bzhang
 *
 */
public class SchedlueTask implements Runnable{
	
	private Map<String, Session> map;
	
	public SchedlueTask(Map<String, Session> map) {
		super();
		this.map = map;
	}
	
	/**
	 * 清除过期session
	 */
	private void clearSession() {
		if (map!=null&&map.size()>0) {
			System.out.println("sessionMap:"+map);
			Collection<Session> values = map.values();
			for (Session session : values) {
				long live=session.getLastAccessedTime()+session.getMaxInactiveInterval();
				if (live<System.currentTimeMillis()) {
					map.remove(session.getId());
				}
			}
		}
	}
	

	@Override
	public void run() {
		clearSession();
		
	}

}
