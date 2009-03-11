package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_CANCEL操作的目的是SP通过此操作可以将已经提交给ISMG的短信删除，
 * ISMG将以CMPP_CANCEL_RESP回应删除操作的结果。
 *
 * @author Long Zhi
 * @version $Id: CancelMessage.java 1998 2009-02-05 06:00:03Z shishuo.wang $
 */
public class CancelMessage extends AbstractMessage {
    /**
     * 信息标识（SP想要删除的信息标识）。
     * 8	Unsigned Integer
     */
    private String msgIdStr;
    
    public String getMsgIdStr() {
        return msgIdStr;
    }

    public void setMsgIdStr(String msgIdStr) {
        this.msgIdStr = msgIdStr;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
