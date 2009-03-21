package com.enorbus.sms.gw.cmpp;

import com.enorbus.sms.gw.cmpp.message.*;
import com.enorbus.sms.gw.cmpp.processor.AbstractPocessor;
import com.enorbus.sms.gw.cmpp.support.Config;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.integration.jmx.IoSessionMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import java.net.InetSocketAddress;

/**
 * IO handler
 *
 * @author Long Zhi
 * @version $Id: ClientHandler.java 2311 2009-03-20 06:38:57Z zhi.long $
 */
public class ClientHandler extends IoHandlerAdapter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected AbstractPocessor processor;
    private MBeanServer mBeanServer;

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    @Required
    public void setMBeanServer(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    @Required
    public void setProcessor(AbstractPocessor processor) {
        this.processor = processor;
    }

    public void sessionOpened(IoSession session) throws Exception {
        logger.info("Login...");
        this.processor.doLogin(session);
    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn("Unexpected exception:", cause);
        // Close connection when unexpected exception is caught.
//        session.close(true);
    }


    @Override
    public void messageReceived(IoSession session, Object message) {
        // we will have to identify  its type using instanceof operator.
//        logger.debug("Session recv...");
        if (message instanceof ActiveTestMessage) {
            processor.onKeepAlive(session, message);
        } else if (message instanceof ActiveTestRespMessage) {
            processor.onKeepAliveResp(session, message);
        } else if (message instanceof ConnectRespMessage) {
            processor.onLoginResp(session, message);
        } else if (message instanceof TerminateMessage) {
            processor.onLogout(session, message);
        } else if (message instanceof TerminateRespMessage) {
            processor.onLogoutResp(session, message);
        } else if (message instanceof DeliverMessage) {
            processor.onDeliver(session, message);
        } else if (message instanceof StatusReportMessage) {
            processor.onStatusReport(session, message);
        } else if (message instanceof SubmitRespMessage) {
            processor.onSubmitResp(session, message);
        } else if (message instanceof QueryRespMessage) {
            processor.onQueryResp(session, message);
        } else if (message instanceof CancelRespMessage) {
            processor.onCancelResp(session, message);
        } else {
            logger.warn("Unknown message: ", message);
        }
    }

    public void sessionClosed(IoSession session) throws Exception {
        processor.onSessionClosed(session);

        // 注销MBean
        mBeanServer.unregisterMBean((ObjectName) session.getAttribute(Constants.MBEAN_OBJECT_NAME_ATTR_KEY));

        // 如果服务器端终止连接，则重连
        if (session.getAttribute(Constants.POSITIVE_TERMINATE_ATTR_KEY) == null) {
            logger.warn("Session closed, reconnecting...");

            Config conf = Config.getInstance();
            String host = conf.getIsmgHost();
            int port = conf.getIsmgPort();

            session = reconnect(host, port, (IoConnector) session.getService());

            // 注册IoSessionMBean
            ObjectName objectName = null;
            try {
                objectName = new ObjectName(session.getClass().getPackage().getName() +
                    ":type=session,name=session-" + session.getId());
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

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        processor.doKeepAlive(session);
    }

    public IoSession reconnect(String host, int port, IoConnector connector) {
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
}
