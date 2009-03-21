package com.enorbus.sms.gw.cmpp;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.*;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.integration.jmx.IoServiceMBean;
import org.apache.mina.integration.jmx.IoSessionMBean;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.enorbus.sms.gw.cmpp.processor.Processor;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SpringBeanUtils;

/**
 * CMPP客户端程序入口
 *
 * @author <a href="mailto:zhi.long@enorbu.som.cn">Long Zhi</a>
 * @version $Id: ClientService.java 2311 2009-03-20 06:38:57Z zhi.long $
 */
public class ClientService implements Service {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Processor moProcessor, mtProcessor;

    public ClientService() {}

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    @Required
    public void setMBeanServer(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    private MBeanServer mBeanServer;

    @Required
    public void setMoProcessor(Processor moProcessor) {
        this.moProcessor = moProcessor;
    }

    @Required
    public void setMtProcessor(Processor mtProcessor) {
        this.mtProcessor = mtProcessor;
    }

    /**
     * 创建连接
     *
     * @param host 主机名
     * @param port 端口
     * @param mode 登陆模式
     * @param num 连接数
     */
    public void createConnection(String host, int port, byte mode, int num) {
        String beanName;
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

            // 注册IoServiceMBean
            try {
                IoServiceMBean connectorMBean = new IoServiceMBean(connector);
                ObjectName connectorName = new ObjectName(connector.getClass().getPackage().getName() +
                        ":type=connector,name=" + beanName + "-" + i);
                mBeanServer.registerMBean(connectorMBean, connectorName);
            } catch(Exception e) {
                logger.warn("Error occured while registering IoServiceMBean: ", e);
            }

            IoSession session = connect(host, port, connector);

            // 注册IoSessionMBean
            ObjectName objectName = null;
            try {
                objectName = new ObjectName(session.getClass().getPackage().getName() +
                    ":type=session,name=" + beanName + "-session" + i + "-" + session.getId());
                IoSessionMBean sessionMbean = new IoSessionMBean(session);
                mBeanServer.registerMBean(sessionMbean, objectName);
            } catch (Exception e) {
                logger.warn("Error occured while registering IoSessionMBean: ", e);
            }

            session.setAttribute(Constants.MBEAN_OBJECT_NAME_ATTR_KEY, objectName);
            
            // wait until the summation is done
            session.getCloseFuture().awaitUninterruptibly();
        }
    }

    public IoSession connect(String host, int port, IoConnector connector) {
        IoSession session;
        for (; ;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                logger.error("Failed to connect: ", e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) { /** ignore */ }
            } catch(Exception e) {
                logger.warn("Error occured while registering IoSessionMBean: ", e);
            }
        }
        return session;
    }

    /**
     * 启动系统
     */
    public void start() {
        Config conf = Config.getInstance();
        String host = conf.getIsmgHost();
        int port = conf.getIsmgPort();

        // 建立MO连接
        createConnection(host, port, Constants.MO_ONLY_LOGIN_MODE, conf.getMoConnectionNumber());
        
        // 建立MT连接
        createConnection(host, port, Constants.MT_ONLY_LOGIN_MODE, conf.getMtConnectionNumber());
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
        System.exit(0);
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
