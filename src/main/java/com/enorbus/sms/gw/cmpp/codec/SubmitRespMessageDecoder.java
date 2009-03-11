package com.enorbus.sms.gw.cmpp.codec;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitRespMessage;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.util.MessageUtil;

/**
 * 提交短信（MT）应答消息解码器
 *
 * @author Long Zhi
 * @version $Id: SubmitRespMessageDecoder.java 2013 2009-02-06 07:57:03Z shishuo.wang $
 */
public class SubmitRespMessageDecoder extends AbstractMessageDecoder {
    public SubmitRespMessageDecoder() {
        super(Constants.CMD_CMPP_SUBMIT_RESP);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen) {
    	SubmitRespMessage m = new SubmitRespMessage();
    	
    	byte[] b = new byte[8];
    	in.get(b);
    	
    	byte[] msgId = new byte[8];
    	System.arraycopy(b, 0, msgId, 0, 8);
    	m.setMsgId(msgId);
    	
    	if (Config.getInstance().isMsgIdReverse())
    		ArrayUtils.reverse(b);
    	// 解析msgId
    	int month = b[0] >>> 4;
    	int date  = (((b[0] << 4) & 0x000000FF) >> 3) | (b[1] & 0x000000FF) >>> 7;
    	int hour  = ((b[1] << 1) & 0x000000FF) >>> 3;
    	int minute =  ((b[1]<<6) & 0x000000FF) >>> 2 | (b[2] & 0x000000FF) >>> 4;
    	int second = ((b[2]<<4) & 0x000000FF) >>> 2 | (b[3] & 0x000000FF) >>> 6;
    	byte[] tb = new byte[4];
    	tb[1] = (byte) (b[3] & (byte)63);
    	tb[2] = b[4];
    	tb[3] = b[5];
    	int ismgId = MessageUtil.getInt(tb, 0);
    	
    	tb = new byte[4];
    	tb[2] = b[6];
    	tb[3] = b[7];
    	int seqId = MessageUtil.getInt(tb, 0);
    	
    	String msgIdStr = String.format("%02d", month)
    					+ String.format("%02d", date)
    					+ String.format("%02d", hour)
    					+ String.format("%02d", minute)
    					+ String.format("%02d", second)
    					+ String.format("%05d", ismgId)
    					+ String.format("%05d", seqId);
    	
    	m.setMsgIdStr(msgIdStr);
    	
    	m.setResult(in.getInt());
    	
        return m;
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
}
