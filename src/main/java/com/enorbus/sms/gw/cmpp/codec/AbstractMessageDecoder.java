package com.enorbus.sms.gw.cmpp.codec;

import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;
import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.Constants;

/**
 * 消息解码器抽象类
 *
 * @author Long Zhi
 * @version $Id: AbstractMessageDecoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public abstract class AbstractMessageDecoder implements MessageDecoder {
    private final int commandId;

    private MessageHeader header;

    private boolean readHeader;

    protected AbstractMessageDecoder(int commandId) {
        this.commandId = commandId;
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        if (in.remaining() < Constants.HEADER_LEN) {
            return MessageDecoderResult.NEED_DATA;
        }

        // Return OK if type and bodyLength matches.
        in.skip(Constants.TOTAL_LENGTH_LEN); // skip total length
        if (commandId == in.getInt()) {
            return MessageDecoderResult.OK;
        }

        // Return NOT_OK if not matches.
        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to skip header if not read.
        if (!readHeader) {
            header = new MessageHeader();
            header.setTotalLength(in.getInt());
            header.setCommandId(commandId);
            in.getInt(); // Skip 'commandId'.
            header.setSequenceId(in.getInt());
            readHeader = true;
        }

        // Try to decode body
        int bodyLen = header.getTotalLength() - Constants.HEADER_LEN;
        // Return NEED_DATA if the body is not fully read.
        if (in.remaining() < bodyLen) {
            return MessageDecoderResult.NEED_DATA;
        }
        
        AbstractMessage m = decodeBody(session, in, bodyLen);
        
        readHeader = false; // reset readHeader for the next decode
        m.setHeader(header);
        out.write(m);

        return MessageDecoderResult.OK;
    }

    /**
     * @return <tt>null</tt> if the whole body is not read yet
     */
    protected abstract AbstractMessage decodeBody(IoSession session,
                                                  IoBuffer in, int bodyLen);
}
