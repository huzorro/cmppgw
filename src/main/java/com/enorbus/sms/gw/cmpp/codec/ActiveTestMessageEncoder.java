package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.ActiveTestMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * Á´Â·²âÊÔÏûÏ¢±àÂëÆ÷
 *
 * @author Long Zhi
 * @version $Id: ActiveTestMessageEncoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ActiveTestMessageEncoder<T extends ActiveTestMessage> extends AbstractMessageEncoder<T> {
    public ActiveTestMessageEncoder() {
        super(Constants.CMD_CMPP_ACTIVE_TEST);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        // ¿Õbody
    }
}
