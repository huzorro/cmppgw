package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.ActiveTestRespMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 链路测试响应消息解码器
 *
 * @author Long Zhi
 * @version $Id: ActiveTestRespMessageDecoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ActiveTestRespMessageDecoder extends AbstractMessageDecoder {
    public ActiveTestRespMessageDecoder() {
        super(Constants.CMD_CMPP_ACTIVE_TEST_RESP);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen) {
        ActiveTestRespMessage m = new ActiveTestRespMessage();
        m.setReserved(in.get());
        return m;
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
}
