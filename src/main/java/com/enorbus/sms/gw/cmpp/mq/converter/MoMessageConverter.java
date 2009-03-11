package com.enorbus.sms.gw.cmpp.mq.converter;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.support.Config;

/**
 * MO消息转换器，实现POJO和JMS Message之间的相互转换
 * @author Long Zhi
 */
public class MoMessageConverter implements MessageConverter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 对接收到的消息进行转换
	 */
	@Override
	public Object fromMessage(Message msg) throws JMSException,
			MessageConversionException {
		if (msg instanceof MapMessage) {
			MapMessage mm = (MapMessage) msg;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("MSG_ID", mm.getString("MSG_ID"));
			map.put("DEST_ID", mm.getString("DEST_ID"));
			map.put("SERVICE_ID", mm.getString("SERVICE_ID"));
			map.put("TP_PID", mm.getInt("TP_PID"));
			map.put("TP_UDHI", mm.getInt("TP_UDHI"));
			map.put("MSG_FMT", mm.getInt("MSG_FMT"));
			map.put("SRC_TERMINAL_ID", mm.getString("SRC_TERMINAL_ID"));
			map.put("SRC_TERMINAL_TYPE", mm.getInt("SRC_TERMINAL_TYPE"));
			map.put("MSG_LENGTH", mm.getInt("MSG_LENGTH"));
			map.put("MSG_CONTENT", mm.getString("MSG_CONTENT"));
			map.put("LINKID", mm.getString("LINKID"));
			map.put("SPID", mm.getStringProperty("SPID"));
			return map;
		} else {
			String err = "Message:[" + msg + "] is not MapMessage";
			logger.warn(err);
			throw new JMSException(err);
		}
	}

	/**
	 * 对发送的消息进行转换
	 */
	@Override
	public Message toMessage(Object obj, Session session) throws JMSException,
			MessageConversionException {
		if (obj instanceof DeliverMessage) {
			DeliverMessage pojo = (DeliverMessage) obj;
			MapMessage msg = session.createMapMessage();
			msg.setString("MSG_ID", pojo.getMsgIdStr());
			msg.setString("DEST_ID", pojo.getDestId());
			msg.setString("SERVICE_ID", pojo.getServiceId());
			msg.setInt("TP_PID", pojo.getTpPid());
			msg.setInt("TP_UDHI", pojo.getTpUdhi());
			msg.setInt("MSG_FMT", pojo.getMsgFmt());
			msg.setString("SRC_TERMINAL_ID", pojo.getSrcTerminalId());
			msg.setInt("SRC_TERMINAL_TYPE", pojo.getSrcTerminalType());
			msg.setInt("MSG_LENGTH", pojo.getMsgLength());
			msg.setString("MSG_CONTENT", pojo.getMsgContent());
			msg.setString("LINKID", pojo.getLinkId());
			msg.setStringProperty("SPID", Config.getInstance().getSpId());
			
			return msg;
		} else {
			String err = "Object:[" + obj + "] is not DeliverMessage";
			logger.warn(err);
			throw new JMSException(err);
		}
	}

}
