package com.enorbus.sms.gw.cmpp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enorbus.sms.gw.cmpp.support.Config;

/**
 * 发送请求消息的任务线程，在未收到响应消息时，定时重试几次，
 * 若仍未收到响应则关闭连接。
 * 收到响应消息时调用respReceived()方法结束线程。
 * 
 * 子类只需实现request()方法，进行实际发送请求的动作即可。
 * 
 * @author shiwang
 * @version $Id: RequestThread.java 2035 2009-02-10 09:09:42Z shishuo.wang $
 */
public abstract class RequestThread extends Thread {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Boolean stop = false;
	
	protected boolean unlimitedRetry = false;
	
	private int maxRetry = Config.getInstance().getTransportMaxRetry();
	private int timeout = Config.getInstance().getTransportTimeout();

	protected void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	protected void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void run() {
		for (int i=0; !Thread.currentThread().isInterrupted() && !this.stop && (unlimitedRetry || i<maxRetry); i++) {
			request();
	        try {
				Thread.sleep(timeout*1000);
			} catch (InterruptedException e) {
				System.out.println("interrupt sleep: " + e.getMessage());
			}
		}
		synchronized (this.stop) {
			if (!stop) {
				this.noResp();
				stop = true;
			}
		}
	}
	
	/**
	 * 通知已经接收到响应消息，结束此线程
	 * @return
	 */
	public boolean respReceived() {
        synchronized (this.stop) {
        	System.out.println("call respReceived");
			if (!stop) {
				this.stop = true;
		        interrupt();
				return true;
			}
	        interrupt();
			return false;
		}
    }

	protected abstract void request();
	
	protected void noResp() {}
}
