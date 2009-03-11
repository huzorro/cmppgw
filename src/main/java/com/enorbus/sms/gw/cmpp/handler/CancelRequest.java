package com.enorbus.sms.gw.cmpp.handler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.SessionManager;
import com.enorbus.sms.gw.cmpp.message.CancelMessage;
import com.enorbus.sms.gw.cmpp.message.CancelRespMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;

/**
 * 查询发送短信状态线程
 *
 * @author shiwang
 * @version $Id: CancelRequest.java 2001 2009-02-06 05:18:15Z shishuo.wang $
 */
public class CancelRequest extends RequestThread {
	
	private String msgId;
	
	private CancelRespMessage respMsg;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	protected int seqId = SeqGenerator.getInstance().getSeq();
	
	private IoSession session;
	
	protected MessageHeader messageHeader = new MessageHeader();
	
	public CancelRequest(IoSession session) {
		this.session = session;

		messageHeader.setTotalLength(Constants.HEADER_LEN + 8);
		messageHeader.setCommandId(Constants.CMD_CMPP_CANCEL);
		messageHeader.setSequenceId(seqId);
    }
	
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	@Override
	protected void request() {
        CancelMessage m = new CancelMessage();
        m.setHeader(messageHeader);
        m.setMsgIdStr(msgId);
        session.write(m);
	}
	
	public void noResp() {
		super.noResp();
		SessionManager.getInstance().complete(session, messageHeader);
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public synchronized void setRespMsg(CancelRespMessage respMsg) {
		this.respMsg = respMsg;
		this.notify();
	}
	
	public synchronized CancelRespMessage getRespMsg() {
		try {
			if (this.respMsg == null)
				this.wait(Config.getInstance().getQueryTimeout() * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return respMsg;
	}
}
