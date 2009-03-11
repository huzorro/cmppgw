package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 本操作仅适用于通信双方采用长连接通信方式时用于保持连接。
 *
 * @author Long Zhi
 * @version $Id: ActiveTestMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ActiveTestMessage extends AbstractMessage {
    // 无消息体
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
