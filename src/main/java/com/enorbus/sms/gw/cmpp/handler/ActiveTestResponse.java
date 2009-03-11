package com.enorbus.sms.gw.cmpp.handler;

import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.ActiveTestRespMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;

/**
 * 链路检测应答处理线程
 * @author shiwang
 * @version $Id: ActiveTestResponse.java 2179 2009-02-24 20:22:43Z shishuo.wang $
 */
public class ActiveTestResponse extends ResponseThread {
	
	private IoSession session;
	
	protected MessageHeader messageHeader = new MessageHeader();
	
	public ActiveTestResponse(IoSession session, int seqId) {
		this.session = session;

		messageHeader.setTotalLength(Constants.HEADER_LEN+1);
		messageHeader.setCommandId(Constants.CMD_CMPP_ACTIVE_TEST_RESP);
		messageHeader.setSequenceId(seqId);
	}

	@Override
	protected void response() {
        ActiveTestRespMessage m = new ActiveTestRespMessage();
        m.setHeader(messageHeader);
        session.write(m);
	}
}
