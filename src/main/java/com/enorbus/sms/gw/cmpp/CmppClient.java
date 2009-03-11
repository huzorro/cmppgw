package com.enorbus.sms.gw.cmpp;

import com.enorbus.sms.gw.cmpp.processor.Processor;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SpringBeanUtils;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * CMPP客户端程序入口
 *
 * @author <a href="mailto:zhi.long@enorbu.som.cn">Long Zhi</a>
 * @version $Id: CmppClient.java 2205 2009-03-03 10:00:41Z zhi.long $ 
 */
public class CmppClient implements Manageable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Processor moProcessor, mtProcessor;

    public CmppClient() {}
    
    @Required
    public void setMoProcessor(Processor moProcessor) {
        this.moProcessor = moProcessor;
    }

    @Required
    public void setMtProcessor(Processor mtProcessor) {
        this.mtProcessor = mtProcessor;
    }

    /**
     * 创建
     * @param mode
     * @param num
     */
    public void createConnection(String host, int port, byte mode, int num) {
        String beanName = null;
        if (mode == Constants.MO_ONLY_LOGIN_MODE) {
            beanName = "moConnector";
        } else {
            beanName = "mtConnector";
        }
        
        for (int i = 0; i < num; i++) {
            NioSocketConnector connector = (NioSocketConnector) SpringBeanUtils.getBean(beanName);

            // 对于MT-ONLY登陆模式，需要主动进行链路检测，而MO-ONLY模式不需要主动检测
            if (beanName.equals("mtConnector"))
                connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, Config.getInstance().getActiveTestInterval());
            
            IoSession session;
            for (;;) {
                try {
                    ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
                    future.awaitUninterruptibly();
                    session = future.getSession();
                    break;
                } catch (RuntimeIoException e) {
                    logger.error("Failed to connect: ", e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {}
                }
            }

            // wait until the summation is done
            session.getCloseFuture().awaitUninterruptibly();
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[] {
                        "beans/applicationContext.xml",
                        "beans/applicationContext-resources.xml"
                });
        ((AbstractApplicationContext) ctx).registerShutdownHook();
        SpringBeanUtils.setApplicationContext(ctx);
        CmppClient cmppClient = (CmppClient) SpringBeanUtils.getBean("cmppClient");
        cmppClient.start();
    }

    /**
     * 启动系统
     */
    public void start() {
        Config conf = Config.getInstance();
        String host = conf.getIsmgHost();
        int port = conf.getIsmgPort();

        // 建立MT连接
        createConnection(host, port, Constants.MT_ONLY_LOGIN_MODE, conf.getMtConnectionNumber());

        // 建立MO连接
        createConnection(host, port, Constants.MO_ONLY_LOGIN_MODE, conf.getMoConnectionNumber());
    }

    /**
     * 停止系统，发送停止消息给相关线程和线程池做收尾工作
     */
    public void stop() {
    	// 注销登录（向ISMG发送终止连接消息）
        if (!mtProcessor.doLogout()) {
             logger.debug("MT session logout didn't finish in 5 seconds");
        }

        if (!moProcessor.doLogout()) {
             logger.debug("MO session logout didn't finish in 5 seconds");
        }
        
        if (!destroy()) {
            logger.debug("destroy action did not finish in 10 seconds");
        }
    }
    
    public void shutdown() {
        stop();
        System.exit(1);
    }

    /**
     * 系统退出时的清理工作
     */
    private boolean destroy() {
        // 关闭ExecutorFilter的线程池
        ExecutorFilter executorFilter = (ExecutorFilter) SpringBeanUtils.getBean("executorFilter");
        Object executor = executorFilter.getExecutor();
        boolean result = false;

        if (executor instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) executor).shutdown();
            try {
                result = ((ThreadPoolExecutor) executor).awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.info("Thread was interrupted: ", e);
            }
        }

        return result;
    }
}
