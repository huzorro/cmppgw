package com.enorbus.sms.gw.cmpp.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.TerminateRespMessage;

/**
 * 终止连接响应消息解码器
 *
 * @author Long Zhi
 * @version $Id: TerminateRespMessageDecoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class TerminateRespMessageDecoder extends AbstractMessageDecoder {
    public TerminateRespMessageDecoder() {
        super(Constants.CMD_CMPP_TERMINATE_RESP);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen) {
        // 无消息体        
        return new TerminateRespMessage();
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
}
