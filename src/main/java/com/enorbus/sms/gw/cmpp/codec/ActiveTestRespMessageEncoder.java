package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.ActiveTestRespMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 链路测试响应消息编码器
 *
 * @author Long Zhi
 * @version $Id: ActiveTestRespMessageEncoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ActiveTestRespMessageEncoder<T extends ActiveTestRespMessage> extends AbstractMessageEncoder<T> {
    public ActiveTestRespMessageEncoder() {
        super(Constants.CMD_CMPP_ACTIVE_TEST_RESP);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        out.put(message.getReserved());
    }
}
