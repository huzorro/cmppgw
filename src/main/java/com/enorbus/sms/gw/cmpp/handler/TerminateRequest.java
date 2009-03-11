package com.enorbus.sms.gw.cmpp.handler;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.message.TerminateMessage;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;
import org.apache.mina.core.session.IoSession;

/**
 * 终止连接处理线程.
 *
 * @author Long Zhi
 * @version $Id: TerminateRequest.java 1953 2009-01-16 10:54:45Z zhi.long $
 */
public class TerminateRequest extends RequestThread {

	private IoSession session;

	public TerminateRequest(IoSession session) {
		this.session = session;
	}

    protected void request() {
        // 注销ISMG
        MessageHeader header = new MessageHeader();
        header.setTotalLength(Constants.HEADER_LEN);
        header.setCommandId(Constants.CMD_CMPP_TERMINATE);
        header.setSequenceId(SeqGenerator.getInstance().getSeq());
        TerminateMessage m = new TerminateMessage();
        m.setHeader(header);
        session.write(m);
    }
}
