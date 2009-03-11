package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.ConnectRespMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 请求连接应答消息解码器
 *
 * @author Long Zhi
 * @version $Id: ConnectRespMessageDecoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ConnectRespMessageDecoder extends AbstractMessageDecoder {
    public ConnectRespMessageDecoder() {
        super(Constants.CMD_CMPP_CONNECT_RESP);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen) {
        ConnectRespMessage m = new ConnectRespMessage();
        m.setStatus(in.getInt());
        byte[] auth = new byte[16];
        in.get(auth);
        m.setAuthenticatorIsmg(auth);
        m.setVersion(in.get());
        return m;
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
}
