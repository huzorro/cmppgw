package com.enorbus.sms.gw.cmpp.processor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Required;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.dao.MessageDao;
import com.enorbus.sms.gw.cmpp.handler.ActiveTestResponse;
import com.enorbus.sms.gw.cmpp.handler.DeliverResponse;
import com.enorbus.sms.gw.cmpp.message.ActiveTestMessage;
import com.enorbus.sms.gw.cmpp.message.CancelRespMessage;
import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.message.QueryRespMessage;
import com.enorbus.sms.gw.cmpp.message.StatusReportMessage;
import com.enorbus.sms.gw.cmpp.mq.MoMessageProducer;
import com.enorbus.sms.gw.cmpp.support.Config;

/**
 * MO-ONLY登录模式
 *
 * @author Long Zhi
 * @version $Id: MoProcessorImpl.java 2225 2009-03-05 16:39:22Z shishuo.wang $
 */
public class MoProcessorImpl extends AbstractPocessor {
	private MoMessageProducer moMessageProducer;
	private MessageDao messageDao;
    
	@Required
	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Required
    public void setMoMessageProducer(MoMessageProducer moMessageProducer) {
		this.moMessageProducer = moMessageProducer;
	}

	protected byte getLoginMode() {
        return Constants.MO_ONLY_LOGIN_MODE;
    }

    public void doKeepAlive(IoSession session) {
        String msg = "doKeepAlive method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onSubmitResp(IoSession session, Object message) {
        String msg = "onSubmitResp method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onKeepAlive(IoSession session, Object message) {
        ActiveTestResponse actTestResponse = new ActiveTestResponse(session, ((ActiveTestMessage) message).getHeader().getSequenceId());
        actTestResponse.start();
    }

    public void onKeepAliveResp(IoSession session, Object message) {
        String msg = "onKeepAliveResp method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    protected void onLoginSuccessfully() {
        // Do nothing
    }

    public QueryRespMessage doQuery(String time, String queryCode) {
        String msg = "doQuery method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onQueryResp(IoSession session, Object message) {
        String msg = "onQueryResp method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public CancelRespMessage doCancel(String msgId) {
        String msg = "doCancel method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onCancelResp(IoSession session, Object message) {
        String msg = "onCancelResp method isn't supported for MO-ONLY login mode";
        logger.warn(msg);
        throw new UnsupportedOperationException(msg);
    }

    public void onDeliver(IoSession session, Object message) {
        DeliverMessage deliverMsg = (DeliverMessage) message;
        deliverMsg.setSpId(Config.getInstance().getSpId());
        
        DeliverResponse deliverResp = new DeliverResponse(session, deliverMsg.getHeader().getSequenceId(),
                deliverMsg.getMsgId(), deliverMsg.getErrorCode());
        deliverResp.start();

        // TODO 此处应进行事物管理
        // 发送MO消息到MO消息队列
        moMessageProducer.send(deliverMsg);
        
        // 记录MO日志
        messageDao.saveMo(deliverMsg);
    }

    public void onStatusReport(IoSession session, Object message) {
        //状态报告
    	StatusReportMessage drm = (StatusReportMessage) message;
        DeliverResponse deliverResp = new DeliverResponse(session, ((StatusReportMessage)message).getHeader().getSequenceId(),
                drm.getMsgId(), 0);
        deliverResp.start();

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("msgIdStr", drm.getMsgIdStr());
        parameterMap.put("stat", drm.getStat());
        parameterMap.put("submitTime", drm.getSubmitTime());
        parameterMap.put("doneTime", drm.getDoneTime());
        parameterMap.put("destTerminalId", drm.getDestTerminalId());

        // 更新MT状态字段
        messageDao.updateMt(parameterMap);
        
        // TODO 发布状态报告主题以通知订阅者
        
    }
}
