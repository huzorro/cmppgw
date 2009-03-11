package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.TerminateMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 终止连接请求消息编码器
 *
 * @author Long Zhi
 * @version $Id: TerminateMessageEncoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class TerminateMessageEncoder<T extends TerminateMessage> extends AbstractMessageEncoder<T> {
    public TerminateMessageEncoder() {
        super(Constants.CMD_CMPP_TERMINATE);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        // 无消息体
    }
}
