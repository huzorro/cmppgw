package com.enorbus.sms.gw.cmpp.mq;

import java.util.List;

import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;

/**
 * 消息队列执行器，负责MO入queue和MT出queue等操作
 *
 * @author Long Zhi
 * @version $Id: MessageQueueExecutor.java 2055 2009-02-12 10:00:49Z jinxue.liu $
 */
public interface MessageQueueExecutor {
    /**
     * MO入Quque
     * @param msg MO消息
     * @return t_moq_in的返回值
     */
    int enqueue(DeliverMessage msg);

    /**
     * MT出Queue
     * @return SubmitMessage 下发消息
     */
    public List<SubmitMessage> dequeue();
    
}
