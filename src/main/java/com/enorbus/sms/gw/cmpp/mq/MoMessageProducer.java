package com.enorbus.sms.gw.cmpp.mq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Queue;

import org.springframework.jms.core.JmsTemplate;

import com.enorbus.sms.gw.cmpp.message.DeliverMessage;

/**
 * 发送MO消息到MO队列
 * @author Long Zhi
 *
 */
public class MoMessageProducer {
	private JmsTemplate jmsTemplate;
	private Queue queue;
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void setQueue(Queue queue) {
		this.queue = queue;
	}
	
	public void send(DeliverMessage msg) {
		jmsTemplate.convertAndSend(queue, msg);
	}
}
