package com.enorbus.sms.gw.cmpp.handler;

import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.message.TerminateRespMessage;

/**
 * 终止连接应答处理线程
 * @author shiwang
 * @version $Id: TerminateResponse.java 1951 2009-01-16 09:59:16Z zhi.long $
 */
public class TerminateResponse extends ResponseThread {
	
	private IoSession session;
	
	protected MessageHeader messageHeader = new MessageHeader();
	
	public TerminateResponse(IoSession session, int seqId) {
		this.session = session;

		messageHeader.setTotalLength(Constants.HEADER_LEN);
		messageHeader.setCommandId(Constants.CMD_CMPP_TERMINATE_RESP);
		messageHeader.setSequenceId(seqId);
	}

	@Override
	protected void response() {
        TerminateRespMessage m = new TerminateRespMessage();
        m.setHeader(messageHeader);
        session.write(m);
	}
}
