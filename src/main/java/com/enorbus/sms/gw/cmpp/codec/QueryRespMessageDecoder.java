package com.enorbus.sms.gw.cmpp.codec;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.QueryRespMessage;
import com.enorbus.sms.gw.cmpp.util.MiscUtils;
import com.enorbus.sms.gw.cmpp.Constants;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 短信状态查询应答消息解码器
 *
 * @author Long Zhi
 * @version $Id: QueryRespMessageDecoder.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class QueryRespMessageDecoder extends AbstractMessageDecoder {
    public QueryRespMessageDecoder() {
        super(Constants.CMD_CMPP_QUERY_RESP);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen)  {
    	QueryRespMessage m = new QueryRespMessage();

    	try {
			m.setTime(in.getString(8, MiscUtils.toDecoder())); 		//时间
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
    	m.setQueryType(in.get());
    	try {
			m.setQueryCode(in.getString(10, MiscUtils.toDecoder()));	//查询代码	
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
    	m.setMtTlMsg(in.getInt());
    	m.setMtTlUsr(in.getInt());
    	m.setMtScs(in.getInt());
    	m.setMtWt(in.getInt());
    	m.setMtFl(in.getInt());
    	m.setMoScs(in.getInt());
    	m.setMoWt(in.getInt());
    	m.setMoFl(in.getInt());
        return m;
    }

   

	public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
    
   
}
