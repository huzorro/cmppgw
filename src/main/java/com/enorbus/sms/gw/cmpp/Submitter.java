package com.enorbus.sms.gw.cmpp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enorbus.sms.gw.cmpp.message.SubmitMessage;
import com.enorbus.sms.gw.cmpp.mq.MessageQueueExecutor;
import com.enorbus.sms.gw.cmpp.support.SpringBeanUtils;
import com.enorbus.sms.gw.cmpp.task.SubmitTask;

/**
 * 下行消息消费者类，从MTQ取消息发送
 *
 * @author Long Zhi
 * @version $Id: Submitter.java 2087 2009-02-17 05:55:10Z shishuo.wang $
 */
public class Submitter extends Thread {
    private final Logger logger = LoggerFactory.getLogger(Submitter.class);
//    private static final AtomicInteger created = new AtomicInteger();
    private ExecutorService threadPool;
    
    private volatile boolean stopped = false;

    public Submitter() {
//        super("SubmitterThread-" + created.incrementAndGet());
        super("SubmitterThread");
        this.threadPool = Executors.newFixedThreadPool(5);
        this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Thread terminated with exception: " + t.getName(), e);
                clean();
            }
        });
    }
    
    public void run() {
        logger.debug("submitter thread running...");

        MessageQueueExecutor messageQueueExecutor = (MessageQueueExecutor) SpringBeanUtils.getBean("messageQueueExecutor");
        List<SubmitMessage> subMsg = null;
        while(!Thread.currentThread().isInterrupted() && !stopped) {
            // 出MTQ
            subMsg = messageQueueExecutor.dequeue();
            if (subMsg.size() != 0){
            	for(SubmitMessage msg:subMsg){
            		IoSession session = SessionManager.getInstance().findFreeSession();
            		SubmitTask task = new SubmitTask(session, msg);
            		
            		SessionManager.getInstance().wait(session, msg.getHeader(), task);
            		threadPool.execute(task);
            	}
            }
        }

        // 线程被中断，关闭线程池
        clean();
    }

    public void shutdown() {
    	this.stopped = true;
        interrupt();
    }

    private void clean() {
        threadPool.shutdown();
    }
}
