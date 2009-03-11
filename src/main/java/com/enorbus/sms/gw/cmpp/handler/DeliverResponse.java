package com.enorbus.sms.gw.cmpp.handler;

import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.DeliverRespMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;

/**
 * @author shiwang
 * @version $Id: DeliverResponse.java 1994 2009-02-04 06:19:09Z shishuo.wang $
 */
public class DeliverResponse extends ResponseThread {
	
	private IoSession session;
	
	protected MessageHeader messageHeader = new MessageHeader();
	
	private byte[] msgId;
	private int result;
	
	public DeliverResponse(IoSession session, int seqId, byte[] msgId, int result) {
		this.session = session;

		messageHeader.setTotalLength(Constants.HEADER_LEN+12);
		messageHeader.setCommandId(Constants.CMD_CMPP_DELIVER_RESP);
		messageHeader.setSequenceId(seqId);

		this.msgId = msgId;
		this.result = result;
	}

	@Override
	protected void response() {
        DeliverRespMessage m = new DeliverRespMessage();
        m.setHeader(messageHeader);
        m.setMsgId(msgId);
        m.setResult(result);
        session.write(m);
	}
}
