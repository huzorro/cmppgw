package com.enorbus.sms.gw.cmpp.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.enorbus.sms.gw.cmpp.message.ActiveTestMessage;
import com.enorbus.sms.gw.cmpp.message.ActiveTestRespMessage;
import com.enorbus.sms.gw.cmpp.message.CancelMessage;
import com.enorbus.sms.gw.cmpp.message.ConnectMessage;
import com.enorbus.sms.gw.cmpp.message.DeliverRespMessage;
import com.enorbus.sms.gw.cmpp.message.QueryMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;
import com.enorbus.sms.gw.cmpp.message.TerminateMessage;
import com.enorbus.sms.gw.cmpp.message.TerminateRespMessage;

/**
 * CMPP协议编解码器工厂
 *
 * @author Long Zhi
 * @version $Id: CmppProtocolCodecFactory.java 2205 2009-03-03 10:00:41Z zhi.long $
 */
public class CmppProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public CmppProtocolCodecFactory() {
        addMessageEncoder(ConnectMessage.class, ConnectMessageEncoder.class);
        addMessageEncoder(ActiveTestMessage.class, ActiveTestMessageEncoder.class);
        addMessageEncoder(ActiveTestRespMessage.class, ActiveTestRespMessageEncoder.class);
        addMessageEncoder(TerminateMessage.class, TerminateMessageEncoder.class);
        addMessageEncoder(TerminateRespMessage.class, TerminateRespMessageEncoder.class);
        addMessageEncoder(DeliverRespMessage.class, DeliverRespMessageEncoder.class);
        addMessageEncoder(SubmitMessage.class, SubmitMessageEncoder.class);
        addMessageEncoder(QueryMessage.class, QueryMessageEncoder.class);
        addMessageEncoder(CancelMessage.class, CancelMessageEncoder.class);
        
        addMessageDecoder(ConnectRespMessageDecoder.class);
        addMessageDecoder(ActiveTestMessageDecoder.class);
        addMessageDecoder(ActiveTestRespMessageDecoder.class);
        addMessageDecoder(DeliverMessageDecoder.class);
        addMessageDecoder(SubmitRespMessageDecoder.class);
        addMessageDecoder(TerminateMessageDecoder.class);
        addMessageDecoder(TerminateRespMessageDecoder.class);
        addMessageDecoder(QueryRespMessageDecoder.class);
        addMessageDecoder(CancelRespMessageDecoder.class);

        // 在这里添加其他的编解码器
    }
}
