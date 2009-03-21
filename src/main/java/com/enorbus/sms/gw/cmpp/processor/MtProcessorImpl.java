package com.enorbus.sms.gw.cmpp.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.listener.AbstractJmsListeningContainer;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.SessionManager;
import com.enorbus.sms.gw.cmpp.dao.MessageDao;
import com.enorbus.sms.gw.cmpp.handler.ActiveTestRequest;
import com.enorbus.sms.gw.cmpp.handler.CancelRequest;
import com.enorbus.sms.gw.cmpp.handler.QueryRequest;
import com.enorbus.sms.gw.cmpp.handler.SubmitTask;
import com.enorbus.sms.gw.cmpp.message.ActiveTestRespMessage;
import com.enorbus.sms.gw.cmpp.message.CancelRespMessage;
import com.enorbus.sms.gw.cmpp.message.QueryRespMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitRespMessage;

/**
 * MT-ONLY登录模式
 *
 * @author Long Zhi
 * @version $Id: MtProcessorImpl.java 2242 2009-03-05 20:29:00Z shishuo.wang $
 */
public class MtProcessorImpl extends AbstractPocessor {
	
    private SessionManager sessionManager = SessionManager.getInstance();
	private MessageDao messageDao;
	
	/** MT消息监听器 */
	private AbstractJmsListeningContainer mtListenerContainer;
	
	/** 指示MT消息监听器是否已启动 */
	private AtomicBoolean started = new AtomicBoolean(false);
    
	@Autowired
	public void setMtListenerContainer(AbstractJmsListeningContainer mtListenerContainer) {
		this.mtListenerContainer = mtListenerContainer;
	}

	public AbstractJmsListeningContainer getMtListenerContainer() {
		return mtListenerContainer;
	}

	@Required
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

    private void startMtListenerContainer() {
        if (!started.getAndSet(true) && !mtListenerContainer.isRunning()) {
        	mtListenerContainer.start();
        }
    }

    private void stopMtListenerContainer() {
        if (started.getAndSet(false) && mtListenerContainer.isRunning()) {
        	mtListenerContainer.stop();
        }
    }

    protected void onLoginSuccessfully() {
        // 如果下发线程未启动，则启动之
    	startMtListenerContainer();
    }

    public boolean doLogout() {
    	stopMtListenerContainer();
        return super.doLogout();
    }

    public void onDeliver(IoSession session, Object message) {
        String msg = "onDeliver method isn't supported for MT-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onStatusReport(IoSession session, Object message) {
        String msg = "onStatusReport method isn't supported for MT-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void doKeepAlive(IoSession session) {
        logger.info("Keep alive...");
        ActiveTestRequest activeTestReq = new ActiveTestRequest(session);
        SessionManager.getInstance().wait(session, activeTestReq.getMessageHeader(), activeTestReq);
        activeTestReq.start();
    }

    public void onKeepAlive(IoSession session, Object message) {
        String msg = "onKeepAlive method isn't supported for MT-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onKeepAliveResp(IoSession session, Object message) {
        logger.debug("On CMPP_ACTIVE_TEST_RESP message");
        ActiveTestRequest actTestReq = (ActiveTestRequest) SessionManager.getInstance().
                complete(session, ((ActiveTestRespMessage) message).getHeader());
        if (actTestReq != null)
            actTestReq.respReceived();
    }

    public void onSubmitResp(IoSession session, Object message) {
        //下发返回值
        logger.debug("On CMD_CMPP_SUBMIT_RESP message");

        SubmitTask task = (SubmitTask) SessionManager.getInstance().
                complete(session, ((SubmitRespMessage) message).getHeader());
        if (task != null) {
            task.respReceived();
            //取出信息
            final SubmitMessage msg = task.getMsg();

            msg.setMsgIdStr(((SubmitRespMessage)message).getMsgIdStr());		//网关自动产生的msid

            if (msg.getPkTotal() == msg.getPkNumber()) {
                String[] toNumber = msg.getDestTerminalId().split(Constants.TERMINAL_ID_SPLITTER);
//                boolean noFeeNumber = StringUtils.isEmpty(msg.getFeeTerminalId());
        		String msgIdPrev = msg.getMsgIdStr().substring(0, 15);
        		int msgIdSeq = Integer.parseInt(msg.getMsgIdStr().substring(15));
        		
            	for (int i=0; i<toNumber.length; i++) {
//            		if (noFeeNumber)
//            			msg.setFeeTerminalId(toNumber[i]);
            		int tmpMsgIdSeq = msgIdSeq + i;
            		if (tmpMsgIdSeq > 0x0000FFFF)
            			tmpMsgIdSeq -= 0x00010000;
            		msg.setMsgIdStr(msgIdPrev + String.format("%05d", tmpMsgIdSeq));
            		
            		msg.setDestTerminalId(toNumber[i]);
	                try {
						messageDao.saveMt((SubmitMessage)BeanUtils.cloneBean(msg));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
            	}
            }
        }        
    }

    public void onLogout(IoSession session, Object message) {
        // 如果已没有MT-ONLY login mode Session，则关闭Submitter线程
        if (sessions.size() == 0)
        	stopMtListenerContainer();
        super.onLogout(session, message);
    }

    public void onSessionClosed(IoSession session) {
        super.onSessionClosed(session);
        if (sessions.size() == 0)
        	stopMtListenerContainer();
    }

    public void registerSession(IoSession session) {
        super.registerSession(session);
        sessionManager.register(session);
    }

    public void unregisterSession(IoSession session) {
        super.unregisterSession(session);
        sessionManager.cancel(session);
    }

    protected byte getLoginMode() {
        return Constants.MT_ONLY_LOGIN_MODE;
    }
    
    public QueryRespMessage doQuery(String time, String queryCode) {
        IoSession session = SessionManager.getInstance().findFreeSession();
        QueryRequest queryReq = new QueryRequest(session);
        
        queryReq.setTime(time);
        queryReq.setQueryCode(queryCode==null ? "" : queryCode.trim());
        queryReq.setQueryType((byte)(StringUtils.isEmpty(queryCode) ? 0 : 1));

        logger.debug("got query request: " + queryReq.toString());
        SessionManager.getInstance().wait(session, queryReq.getMessageHeader(), queryReq);
        queryReq.start();
        
        return queryReq.getRespMsg();
    }

    public void onQueryResp(IoSession session, Object message) {
        QueryRequest queryReq = (QueryRequest) sessionManager.
                complete(session, ((QueryRespMessage) message).getHeader());
        if (queryReq != null) {
            queryReq.respReceived();
            queryReq.setRespMsg((QueryRespMessage)message);
        }
    }

    public CancelRespMessage doCancel(String msgId) {
        IoSession session = SessionManager.getInstance().findFreeSession();
        CancelRequest cancelReq = new CancelRequest(session);

        if (StringUtils.isEmpty(msgId) || msgId.length() != 20)
            return null;

        cancelReq.setMsgId(msgId);

        logger.debug("got cancel request: " + cancelReq.toString());

        SessionManager.getInstance().wait(session, cancelReq.getMessageHeader(), cancelReq);
        cancelReq.start();

        return cancelReq.getRespMsg();
    }

    public void onCancelResp(IoSession session, Object message) {
        CancelRequest cancelReq = (CancelRequest) sessionManager.
                complete(session, ((CancelRespMessage) message).getHeader());
        if (cancelReq != null) {
            cancelReq.respReceived();
            cancelReq.setRespMsg((CancelRespMessage)message);
        }
    }
}
