package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_CANCEL的响应消息
 *
 * @author Long Zhi
 * @version $Id: CancelRespMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class CancelRespMessage extends AbstractMessage {
    /**
     * 成功标识。0：成功；1：失败。
     * 4	Unsigned Integer
     */
    private int successId;

    public int getSuccessId() {
        return successId;
    }

    public void setSuccessId(int successId) {
        this.successId = successId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }    
}
