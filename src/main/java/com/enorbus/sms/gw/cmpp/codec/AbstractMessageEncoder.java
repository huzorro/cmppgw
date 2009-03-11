package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 消息编码器抽象类
 *
 * @author Long Zhi
 * @version $Id: AbstractMessageEncoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public abstract class AbstractMessageEncoder<T extends AbstractMessage> implements MessageEncoder<T> {
    private final int commandId;

    protected AbstractMessageEncoder(int commandId) {
        this.commandId = commandId;
    }

    public void encode(IoSession session, T message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buf = IoBuffer.allocate(Constants.HEADER_LEN);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding

        // Encode a header
        buf.putInt(message.getHeader().getTotalLength());
        buf.putInt(message.getHeader().getCommandId());
        buf.putInt(message.getHeader().getSequenceId());

        // Encode a body
        encodeBody(session, message, buf);
        buf.flip();
        out.write(buf);
    }

    protected abstract void encodeBody(IoSession session, T message, IoBuffer out);
}
