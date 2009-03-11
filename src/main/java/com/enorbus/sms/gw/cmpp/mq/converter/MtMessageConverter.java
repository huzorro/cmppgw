package com.enorbus.sms.gw.cmpp.mq.converter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * MT消息转换器，实现POJO和JMS Message之间的相互转换
 * @author Long Zhi
 */
public class MtMessageConverter implements MessageConverter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 对接收到的Map消息进行转换
	 */
	@Override
	public Object fromMessage(Message msg) throws JMSException,
			MessageConversionException {
		// TODO 
		logger.error("Not implement yet!");
		return null;
	}

	/**
	 * 对发送的消息进行转换
	 */
	@Override
	public Message toMessage(Object obj, Session session) throws JMSException,
			MessageConversionException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implements yet!");
	}

}
