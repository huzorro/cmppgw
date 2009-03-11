package com.enorbus.sms.gw.cmpp.handler;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.ConnectMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;
import com.enorbus.sms.gw.cmpp.util.MiscUtils;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * @author shiwang
 * @version $Id: ConnectRequest.java 2205 2009-03-03 10:00:41Z zhi.long $
 */
public class ConnectRequest extends RequestThread {
	
	protected int seqId = SeqGenerator.getInstance().getSeq();

	private IoSession session;

    /** µÇÂ¼Ä£Ê½ */
    private byte loginMode;

    public ConnectRequest(IoSession session, byte loginMode) {
		this.unlimitedRetry = true;
		this.session = session;
        this.loginMode = loginMode;
    }

	@Override
	protected void request() {
		// send connect requests
        // µÇÂ¼ISMG
        MessageHeader header = new MessageHeader();
        header.setTotalLength(Constants.HEADER_LEN + 6 + 16 + 1 + 4);
        header.setCommandId(Constants.CMD_CMPP_CONNECT);
        header.setSequenceId(seqId);
        ConnectMessage m = new ConnectMessage();
        m.setHeader(header);
        Config config = Config.getInstance();
        m.setSourceAddr(config.getSpId());
        m.setVersion(this.loginMode);
        String timestamp = MiscUtils.getCurrentTimeStamp();

        byte[] spId = config.getSpId().getBytes();
        byte[] sharedSecret = config.getSharedSecret().getBytes();

        byte auth[] = new byte[sharedSecret.length + 25];
        System.arraycopy(spId, 0, auth, 0, 6);
        System.arraycopy(sharedSecret, 0, auth, 15, sharedSecret.length);
        System.arraycopy(timestamp.getBytes(), 0, auth, 15 + sharedSecret.length,
                         timestamp.length());

        m.setAuthenticatorSource(MiscUtils.md5(auth));
        
        session.setAttribute(Constants.AUTHSOURCE_ATTR_KEY, m.getAuthenticatorSource());
        
        m.setTimeStamp(Integer.parseInt(timestamp));
        session.write(m);
	}
	
}
