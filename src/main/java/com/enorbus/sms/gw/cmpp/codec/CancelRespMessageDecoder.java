package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.ActiveTestRespMessage;
import com.enorbus.sms.gw.cmpp.message.CancelRespMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 删除短信应答消息解码器
 *
 * @author Long Zhi
 * @version $Id: CancelRespMessageDecoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class CancelRespMessageDecoder extends AbstractMessageDecoder {
    public CancelRespMessageDecoder() {
        super(Constants.CMD_CMPP_CANCEL_RESP);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen) {
    	CancelRespMessage  m = new CancelRespMessage();
        m.setSuccessId(in.getInt());
        return m;
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
}
