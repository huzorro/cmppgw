package com.enorbus.sms.gw.cmpp.codec;

import com.enorbus.sms.gw.cmpp.message.QueryMessage;
import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.util.MiscUtils;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;

import java.nio.charset.CharacterCodingException;

/**
 * 短信状态查询请求消息编码器
 *
 * @author Long Zhi
 * @version $Id: QueryMessageEncoder.java 1983 2009-01-22 03:56:46Z zhi.long $
 */
public class QueryMessageEncoder<T extends QueryMessage> extends AbstractMessageEncoder<T> {
    public QueryMessageEncoder() {
        super(Constants.CMD_CMPP_QUERY);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        try {
            out.putString(message.getTime(), 8, MiscUtils.toEncoder());
            out.put(message.getQueryType());
            out.putString(message.getQueryCode(), 10, MiscUtils.toEncoder());
            out.putString(message.getReserve(), 8, MiscUtils.toEncoder());
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
    }
}
