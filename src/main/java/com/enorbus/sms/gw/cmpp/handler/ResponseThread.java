package com.enorbus.sms.gw.cmpp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送响应消息的任务线程
 * 
 * @author shiwang
 * @version $Id: ResponseThread.java 1936 2009-01-14 07:11:44Z shishuo.wang $
 */
public abstract class ResponseThread extends Thread {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void run() {
		response();
	}

	protected abstract void response();
}
