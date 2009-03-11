package com.enorbus.sms.gw.cmpp.codec;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.buffer.IoBuffer;
import com.enorbus.sms.gw.cmpp.message.CancelMessage;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.util.MessageUtil;
import com.enorbus.sms.gw.cmpp.Constants;

/**
 * É¾³ý¶ÌÐÅÇëÇóÏûÏ¢±àÂëÆ÷
 *
 * @author Long Zhi
 * @version $Id: CancelMessageEncoder.java 2013 2009-02-06 07:57:03Z shishuo.wang $
 */
public class CancelMessageEncoder<T extends CancelMessage> extends AbstractMessageEncoder<T> {
    public CancelMessageEncoder() {
        super(Constants.CMD_CMPP_CANCEL);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
    	String msgId = message.getMsgIdStr();
    	
    	int month = Integer.parseInt(msgId.substring(0, 2));
    	int date  = Integer.parseInt(msgId.substring(2, 4));
    	int hour  = Integer.parseInt(msgId.substring(4, 6));
    	int minute= Integer.parseInt(msgId.substring(6, 8));
    	int second= Integer.parseInt(msgId.substring(8, 10));
    	int ismgId= Integer.parseInt(msgId.substring(10, 15));
    	int seqId = Integer.parseInt(msgId.substring(15, 20));
    	
    	byte[] b = new byte[8];
    	
    	MessageUtil.putInt(seqId, b, 4);
    	MessageUtil.putInt(ismgId, b, 2);
    	
    	b[3] = (byte)(((second & 0x000000FF) << 6) | b[3]);
    	b[2] = (byte)(((minute & 0x000000FF) << 4) | ((second & 0x000000FF) >> 2));
    	b[1] = (byte)(((date & 0x000000FF) << 7) | ((hour & 0x000000FF) << 2) | ((minute & 0x000000FF) >> 4));
    	b[0] = (byte)(((month & 0x000000FF) << 4) | ((date & 0x000000FF) >> 1));
    	
    	if (Config.getInstance().isMsgIdReverse())
    		ArrayUtils.reverse(b);
    	
    	out.put(b);
    }
}
