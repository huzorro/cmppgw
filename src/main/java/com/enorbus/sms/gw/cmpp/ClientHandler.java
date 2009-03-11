package com.enorbus.sms.gw.cmpp;

import com.enorbus.sms.gw.cmpp.message.*;
import com.enorbus.sms.gw.cmpp.processor.AbstractPocessor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * IO handler
 *
 * @author Long Zhi
 * @version $Id: ClientHandler.java 2205 2009-03-03 10:00:41Z zhi.long $
 */
public class ClientHandler extends IoHandlerAdapter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected AbstractPocessor processor;

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
        session.close(true);
    }

    
    @Override
    public void messageReceived(IoSession session, Object message)  {
        // we will have to identify  its type using instanceof operator.
        logger.debug("Session recv...");
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
        } else if (message instanceof SubmitRespMessage){
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
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        processor.doKeepAlive(session);
    }

    public void test() {};
}
