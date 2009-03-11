package com.enorbus.sms.gw.cmpp.codec;

import java.nio.charset.CharacterCodingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.AbstractMessage;
import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.message.StatusReportMessage;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.util.MessageUtil;
import com.enorbus.sms.gw.cmpp.util.MiscUtils;

/**
 * 短信下发（MO）消息解码器
 *
 * @author Long Zhi
 * @version $Id: DeliverMessageDecoder.java 2224 2009-03-05 16:11:38Z shishuo.wang $
 */
public class DeliverMessageDecoder extends AbstractMessageDecoder {
    public DeliverMessageDecoder() {
        super(Constants.CMD_CMPP_DELIVER);
    }

    protected AbstractMessage decodeBody(IoSession session, IoBuffer in, int bodyLen) {
    	DeliverMessage m =new DeliverMessage();
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
    	
    	try {
			m.setDestId(in.getString(21, MiscUtils.toDecoder()));
			m.setServiceId(in.getString(10, MiscUtils.toDecoder()));
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		
		m.setTpPid(in.get());
		m.setTpUdhi(in.get());
		m.setMsgFmt(in.get());
		
		try {
			m.setSrcTerminalId(in.getString(32, MiscUtils.toDecoder()));
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		
		m.setSrcTerminalType(in.get());
		m.setRegisteredDelivery(in.get());
		m.setMsgLength(in.get());// TODO FIXME：消息长度可能超出byte类型所能表示的最大值127，应用in.getUnsigned()方法取代in.get()方法 
		
		if (m.getRegisteredDelivery() == 0) { // 如果是MO消息
			try {
				if (m.getMsgLength() != bodyLen-97)
					m.setErrorCode(4);
				m.setMsgContent(in.getString(bodyLen-97, MiscUtils.toDecoder()));
				m.setLinkId(in.getString(20, MiscUtils.toDecoder()));
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
			
	        return m;
		} else { // 如果是状态报告
			StatusReportMessage statusRepMsg = new StatusReportMessage();
			statusRepMsg.setDeliverMsgId(m.getMsgId());
			
			b = new byte[8];
	    	in.get(b);
	    	
	    	System.arraycopy(b, 0, msgId, 0, 8);
	    	statusRepMsg.setMsgId(msgId);
	    	
	    	if (Config.getInstance().isMsgIdReverse())
	    		ArrayUtils.reverse(b);
	    	// 解析msgId
	    	month = b[0] >>> 4;
	    	date  = (((b[0] << 4) & 0x000000FF) >> 3) | (b[1] & 0x000000FF) >>> 7;
	    	hour  = ((b[1] << 1) & 0x000000FF) >>> 3;
	    	minute =  ((b[1]<<6) & 0x000000FF) >>> 2 | (b[2] & 0x000000FF) >>> 4;
	    	second = ((b[2]<<4) & 0x000000FF) >>> 2 | (b[3] & 0x000000FF) >>> 6;
	    	tb = new byte[4];
	    	tb[1] = (byte) (b[3] & (byte)63);
	    	tb[2] = b[4];
	    	tb[3] = b[5];
	    	ismgId = MessageUtil.getInt(tb, 0);
	    	
	    	tb = new byte[4];
	    	tb[2] = b[6];
	    	tb[3] = b[7];
	    	seqId = MessageUtil.getInt(tb, 0);
	    	
	    	msgIdStr = String.format("%02d", month)
	    					+ String.format("%02d", date)
	    					+ String.format("%02d", hour)
	    					+ String.format("%02d", minute)
	    					+ String.format("%02d", second)
	    					+ String.format("%05d", ismgId)
	    					+ String.format("%05d", seqId);
	    	
	    	statusRepMsg.setMsgIdStr(msgIdStr);
	    	try {
	    		statusRepMsg.setStat(in.getString(7, MiscUtils.toDecoder()));
	    		statusRepMsg.setSubmitTime(in.getString(10, MiscUtils.toDecoder()));
	    		statusRepMsg.setDoneTime(in.getString(10, MiscUtils.toDecoder()));
	    		statusRepMsg.setDestTerminalId(in.getString(32, MiscUtils.toDecoder()));
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
			statusRepMsg.setSmscSequence(in.getInt());
			
			try {
				in.getString(20, MiscUtils.toDecoder());
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
			
			return statusRepMsg;
		}
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }
}
