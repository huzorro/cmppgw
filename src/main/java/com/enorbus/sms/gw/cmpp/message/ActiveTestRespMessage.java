package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_ACTIVE_TEST的响应消息
 *
 * @author Long Zhi
 * @version $Id: ActiveTestRespMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ActiveTestRespMessage extends AbstractMessage {
    /**
     * 保留字段，1字节
     */
    private byte reserved;

    public byte getReserved() {
        return reserved;
    }

    public void setReserved(byte reserved) {
        this.reserved = reserved;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }    
}
