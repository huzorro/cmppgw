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
        DeliverResponse deliverResp = new DeliverResponse(session, ((StatusReportMessage)message).getHeader().getSequenceId(),
                ((StatusReportMessage)message).getMsgId(), 0);
        deliverResp.start();

        //取当前的时间
        //Date statusDate = new Date();
        //SimpleDateFormat df=new SimpleDateFormat("yyMMddhhmmss");
        //String doneTime=df.format(statusDate).substring(0, 2)+((StatusReportMessage)message).getDoneTime();

        SimpleDateFormat df=new SimpleDateFormat("yyMMddhhmm");
        String doneTime=((StatusReportMessage)message).getDoneTime();

        String status=((StatusReportMessage)message).getStat();			//修改状态
        String msgId = ((StatusReportMessage)message).getMsgIdStr();	//msgid
        String toNumber = ((StatusReportMessage)message).getDestTerminalId();	//目的手机号
        Timestamp rptDate = null;
        try {
            rptDate = new Timestamp(df.parse(doneTime).getTime());		//状态报告返回日期
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("msgIdStr", msgId);
        updateMap.put("rptStatussir", status);
        updateMap.put("rptDate", rptDate);
        updateMap.put("toNumber", toNumber);

        // 更新MT状态字段
        messageDao.updateMt(updateMap);
        
        // TODO 发布状态报告主题以通知订阅者
        
    }
}
