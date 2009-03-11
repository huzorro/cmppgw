package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.DeliverRespMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 短信下发（MO）应答消息编码器
 *
 * @author Long Zhi
 * @version $Id: DeliverRespMessageEncoder.java 1994 2009-02-04 06:19:09Z shishuo.wang $
 */
public class DeliverRespMessageEncoder<T extends DeliverRespMessage> extends AbstractMessageEncoder<T> {
    public DeliverRespMessageEncoder() {
        super(Constants.CMD_CMPP_DELIVER_RESP);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
    	out.put(message.getMsgId());
        out.putInt(message.getResult());
        
    }
}
