package com.enorbus.sms.gw.cmpp.handler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.SessionManager;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.message.QueryMessage;
import com.enorbus.sms.gw.cmpp.message.QueryRespMessage;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;

/**
 * 查询发送短信状态线程
 *
 * @author shiwang
 * @version $Id: QueryRequest.java 2001 2009-02-06 05:18:15Z shishuo.wang $
 */
public class QueryRequest extends RequestThread {
	
	private String time;
	private String queryCode = "";
	private byte queryType = (byte) 0;
	private String reserve = "";
	
	private QueryRespMessage respMsg;

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public void setQueryType(byte queryType) {
		this.queryType = queryType;
	}

	protected int seqId = SeqGenerator.getInstance().getSeq();
	
	private IoSession session;
	
	protected MessageHeader messageHeader = new MessageHeader();
	
	public QueryRequest(IoSession session) {
		this.session = session;

		messageHeader.setTotalLength(Constants.HEADER_LEN + 8 + 1 + 10 + 8);
		messageHeader.setCommandId(Constants.CMD_CMPP_QUERY);
		messageHeader.setSequenceId(seqId);
    }
	
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	@Override
	protected void request() {
        QueryMessage m = new QueryMessage();
        m.setHeader(messageHeader);
        m.setTime(time);
        m.setQueryCode(queryCode);
        m.setQueryType(queryType);
        m.setReserve(reserve);
        session.write(m);
	}
	
	public void noResp() {
		super.noResp();
		SessionManager.getInstance().complete(session, messageHeader);
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public synchronized void setRespMsg(QueryRespMessage respMsg) {
		this.respMsg = respMsg;
		this.notify();
	}
	
	public synchronized QueryRespMessage getRespMsg() {
		try {
			if (this.respMsg == null)
				this.wait(Config.getInstance().getQueryTimeout() * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return respMsg;
	}
}
