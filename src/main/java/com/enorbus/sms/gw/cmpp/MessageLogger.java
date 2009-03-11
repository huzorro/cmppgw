package com.enorbus.sms.gw.cmpp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责调度写MO,MT日志的线程
 * @author shiwang
 * @version $Id: MessageLogger.java 2220 2009-03-05 10:45:00Z shishuo.wang $
 *
 */
public class MessageLogger {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ExecutorService logThreadPool;
	
	private static MessageLogger instance = new MessageLogger();
	
	private MessageLogger() {
		this.logThreadPool = Executors.newFixedThreadPool(10);
	}
	
	public static MessageLogger getInstance() {
		return instance;
	}
	
	public void logMO(Runnable r) {
		this.logThreadPool.execute(r);
	}
	
	public void logInsertMT(Runnable r) {
		this.logThreadPool.execute(r);
	}
	
	public void logUpdateMT(Runnable r) {
		this.logThreadPool.execute(r);
	}
}
