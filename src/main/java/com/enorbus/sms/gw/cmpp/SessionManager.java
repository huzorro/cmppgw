package com.enorbus.sms.gw.cmpp;

import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ConcurrentHashSet;

import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.support.Config;

/**
 * session管理
 *
 * @author shiwang
 * @version $Id: SessionManager.java 2296 2009-03-16 10:11:50Z shishuo.wang $
 */
public class SessionManager {
	public static final String WAITING_FOR_RESP = "waiting_for_response";
    public static final String SEMAPHORE = "semaphore";

    private static SessionManager instance = new SessionManager();
    private int trafficControl;
	
    private SessionManager() {
		trafficControl = Config.getInstance().getTrafficControl();
	}
	
	public static SessionManager getInstance() {
		return instance;
	}
	
	public int aliveSessionCount() {
		return sessions.size();
	}

    public Object[] getAliveSessions() {
        return sessions.toArray();
	}

    private Set<IoSession> sessions = new ConcurrentHashSet<IoSession>();
    
	
	/**
	 * 向管理器注册一个session
	 * @param session
	 */
	public void register( IoSession session ) {
        if (sessions.contains(session))
			return;

        session.setAttribute(WAITING_FOR_RESP, new Hashtable<String, Object>());

        // 对session设置信号量
        session.setAttribute(SEMAPHORE, new Semaphore(trafficControl));
        sessions.add(session);
	}
	
	/**
	 * 注销一个session
	 * @param session
	 */
	public void cancel( IoSession session ) {
		sessions.remove(session);
	}
	
	/**
	 * 取得一个滑动窗口流量未达到上限的session
	 */
	public IoSession findFreeSession() {
        Semaphore sem = null;

        while (true) {
            for (IoSession session : sessions) {
                sem = (Semaphore) session.getAttribute(SEMAPHORE);
                // 尝试获取许可
                if (sem.tryAcquire()) {
                    return session;
                }
            }
        }
    }
	
	/**
	 * 添加等待任务
	 * @param session
	 * @param header
	 * @param obj
	 */
	public void wait(IoSession session, MessageHeader header, Object obj) {
		Hashtable<String, Object> waitingResps = (Hashtable) session.getAttribute(WAITING_FOR_RESP);
		waitingResps.put(header.getCommandId()+"_"+header.getSequenceId(), obj);
	}
	
	/**
	 * 通知任务完成
	 * @param session
	 * @param header
	 * @return
	 */
	public Object complete(IoSession session, MessageHeader header) {
		Hashtable<String, Object> waitingResps = (Hashtable) session.getAttribute(WAITING_FOR_RESP);
		Object obj = waitingResps.remove((header.getCommandId()&Constants.CMD_AND)+"_"+header.getSequenceId());

        // 释放信号量
        Semaphore sem = (Semaphore) session.getAttribute(SEMAPHORE);
        sem.release();

		return obj;
	}
}
