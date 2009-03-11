package com.enorbus.sms.gw.cmpp.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;
import com.enorbus.sms.gw.cmpp.util.MessageUtil;

/**
 * 提交短信（MT）请求消息编码器
 *
 * @author Long Zhi
 * @version $Id: SubmitMessageEncoder.java 2217 2009-03-05 07:50:20Z shishuo.wang $
 */
public class SubmitMessageEncoder<T extends SubmitMessage> extends AbstractMessageEncoder<T> {
    public SubmitMessageEncoder() {
        super(Constants.CMD_CMPP_SUBMIT);
    }

    protected void encodeBody(IoSession session, T message, IoBuffer out) {
       out.putLong(0L);
       out.put((byte) message.getPkTotal());
       out.put((byte) message.getPkNumber());
       out.put((byte) message.getRegisteredDelivery());
       out.put((byte) message.getMsgLevel());
       
       byte[] byteServiceId = new byte[10];
       MessageUtil.putString(byteServiceId, 0, message.getServiceId());
       out.put(byteServiceId);
       
       out.put((byte) message.getFeeUserType());
       
       byte[] byteFeeTerminalId = new byte[32];
       MessageUtil.putString(byteFeeTerminalId, 0, message.getFeeTerminalId());
       out.put(byteFeeTerminalId);
             
       out.put((byte) message.getFeeTerminalType());
       out.put((byte) message.getTpPid());
       out.put((byte) message.getTpUdhi());
       out.put((byte) message.getMsgFmt());
       
       byte[] byteMsgSrc = new byte[6];
       MessageUtil.putString(byteMsgSrc, 0, message.getMsgSrc());
       out.put(byteMsgSrc);       
       
       byte[] byteFeeType = new byte[2];
       MessageUtil.putString(byteFeeType, 0, message.getFeeType());
       out.put(byteFeeType);
       
       byte[] byteFeeCode = new byte[6];
       MessageUtil.putString(byteFeeCode, 0,message.getFeeCode());
       out.put(byteFeeCode);
       
       byte[] byteValidTime = new byte[17];
       MessageUtil.putString(byteValidTime, 0,message.getValidTime());
       //byteValidTime[15] = '+';
       //byteValidTime[14] = '0';
       //byteValidTime[13] = '0';
       //byteValidTime[12] = '0';
       out.put(byteValidTime);
       
       byte[] byteAtTime = new byte[17];
       MessageUtil.putString(byteAtTime, 0,message.getAtTime());
       //byteValidTime[15] = '+';
       //byteValidTime[14] = '0';
       //byteValidTime[13] = '0';
       //byteValidTime[12] = '0';
       out.put(byteAtTime);
       
       byte[] byteSrcId = new byte[21];
       MessageUtil.putString(byteSrcId , 0,message.getSrcId());
       out.put(byteSrcId );

       String[] toNumber = message.getDestTerminalId().split(Constants.TERMINAL_ID_SPLITTER);
       out.put((byte) toNumber.length);
       
       byte[] byteDestTerminalId = new byte[32 * toNumber.length];
       for (int i=0; i<toNumber.length; i++)
    	   MessageUtil.putString(byteDestTerminalId, 32*i, toNumber[i]);
       out.put(byteDestTerminalId);
              
       out.put((byte) message.getDestTerminalType());
       //out.put((byte) message.getMsgContent().getBytes().length);
       out.put((byte) message.getMsgContent().length);
            
       //byte[] byteMsgContent = new byte[message.getMsgLength()];
       //MessageUtil.putString(byteMsgContent, 0,message.getMsgContent());
       out.put(message.getMsgContent());
      
       byte[] byteLinkId = new byte[20];
       MessageUtil.putString(byteLinkId, 0,message.getLinkId());
       out.put(byteLinkId);
    }
}
