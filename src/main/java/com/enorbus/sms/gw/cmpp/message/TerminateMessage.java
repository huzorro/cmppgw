package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_TERMINATE操作的目的是SP或ISMG基于某些原因决定拆除当前的应用层连接而发起的操作。
 * 此操作完成后SP与ISMG之间的应用层连接被释放，
 * 此后SP若再要与ISMG通信时应发起CMPP_CONNECT操作。
 * ISMG或SP以CMPP_TERMINATE_RESP消息响应请求。
 *
 * @author Long Zhi
 * @version $Id: TerminateMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class TerminateMessage extends AbstractMessage {
    // 无消息体
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
