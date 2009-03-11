package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_DELIVER的响应消息
 *
 * @author Long Zhi
 * @version $Id: DeliverRespMessage.java 1994 2009-02-04 06:19:09Z shishuo.wang $
 */
public class DeliverRespMessage extends AbstractMessage {
    /**
     * 信息标识（CMPP_DELIVER中的Msg_Id字段）。
     * 8	Unsigned Integer
     */
    private byte[] msgId;

    /**
     * 结果：
     * 0：正确；
     * 1：消息结构错；
     * 2：命令字错；
     * 3：消息序号重复；
     * 4：消息长度错；
     * 5：资费错；
     * 6：超过最大信息长；
     * 7：业务代码错；
     * 8: 流量控制错；
     * 9~ ：其他错误。
     * 4	Unsigned Integer
     */
    private int result;

    public byte[] getMsgId() {
        return msgId;
    }

    public void setMsgId(byte[] msgId) {
        this.msgId = msgId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
