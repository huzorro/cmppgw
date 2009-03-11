package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 消息头
 *
 * @author Long Zhi
 * @version $Id: MessageHeader.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class MessageHeader {
    /**
     * 消息总长度(含消息头及消息体)
     * 4	Unsigned Integer
     */
    private int totalLength;

    /**
     * 命令或响应类型
     * 4	Unsigned Integer
     */
    private int commandId;

    /**
     * 消息流水号,顺序累加,步长为1,循环使用（一对请求和应答消息的流水号必须相同）
     * 4	Unsigned Integer
     */
    private int sequenceId;

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
        return new ToStringBuilder(this)
                .append("totalLength", this.totalLength)
                .append("commandId", Integer.toHexString(this.commandId))
                .append("sequenceId", this.sequenceId).toString();
    }
}
