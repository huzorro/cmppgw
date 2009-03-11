package com.enorbus.sms.gw.cmpp.handler;

import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.ActiveTestMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;
import com.enorbus.sms.gw.cmpp.support.Config;

/**
 * 链路检测线程
 *
 * @author shiwang
 * @version $Id: ActiveTestRequest.java 1949 2009-01-16 07:22:11Z zhi.long $
 */
public class ActiveTestRequest extends RequestThread {
    
    protected int seqId = SeqGenerator.getInstance().getSeq();
	
	private IoSession session;
	
	protected MessageHeader messageHeader = new MessageHeader();
	
	public ActiveTestRequest(IoSession session) {
		this.session = session;

		messageHeader.setTotalLength(Constants.HEADER_LEN);
		messageHeader.setCommandId(Constants.CMD_CMPP_ACTIVE_TEST);
		messageHeader.setSequenceId(seqId);

        // Overrides default max-retry and timeout value
        Config config = Config.getInstance();
        setMaxRetry(config.getActiveTestMaxRetry());
        setTimeout(config.getActiveTestTimeout());
    }
	
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	@Override
	protected void request() {
		// session空闲时进行链路检测
        ActiveTestMessage m = new ActiveTestMessage();
        m.setHeader(messageHeader);
        session.write(m);
	}
	
	public void noResp() {
		this.session.close(true);
	}
}
