package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_QUERY的响应消息
 *
 * @author Long Zhi
 * @version $Id: QueryRespMessage.java 1998 2009-02-05 06:00:03Z shishuo.wang $
 */
public class QueryRespMessage extends AbstractMessage {
    /**
     * 时间(精确至日)。
     * 8	Octet String
     */
    private String time;

    /**
     * 查询类别：0：总数查询；1：按业务类型查询。
     * 1	Unsigned Integer	
     */
    private int queryType;

    /**
     * 查询码。
     * 10	Octet String
     */
    private String queryCode;

    /**
     * 从SP接收信息总数。
     * 4	Unsigned Integer
     */
     private int mtTlMsg;

    /**
     * 从SP接收用户总数。
     * 4	Unsigned Integer
     */
    private int mtTlUsr;

    /**
     * 成功转发数量。
     * 4	Unsigned Integer
     */
    private int mtScs;

    /**
     * 待转发数量。
     * 4	Unsigned Integer
     */
    private int mtWt;

    /**
     * 转发失败数量。
     * 4	Unsigned Integer
     */
    private int mtFl;

    /**
     * 向SP成功送达数量。
     * 4	Unsigned Integer
     */
    private int moScs;

    /**
     * 向SP待送达数量。
     * 4	Unsigned Integer
     */
    private int moWt;

    /**
     * 向SP送达失败数量。
     * 4	Unsigned Integer
     */
    private int moFl;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public int getMtTlMsg() {
        return mtTlMsg;
    }

    public void setMtTlMsg(int mtTlMsg) {
        this.mtTlMsg = mtTlMsg;
    }

    public int getMtTlUsr() {
        return mtTlUsr;
    }

    public void setMtTlUsr(int mtTlUsr) {
        this.mtTlUsr = mtTlUsr;
    }

    public int getMtScs() {
        return mtScs;
    }

    public void setMtScs(int mtScs) {
        this.mtScs = mtScs;
    }

    public int getMtWt() {
        return mtWt;
    }

    public void setMtWt(int mtWt) {
        this.mtWt = mtWt;
    }

    public int getMtFl() {
        return mtFl;
    }

    public void setMtFl(int mtFl) {
        this.mtFl = mtFl;
    }

    public int getMoScs() {
        return moScs;
    }

    public void setMoScs(int moScs) {
        this.moScs = moScs;
    }

    public int getMoWt() {
        return moWt;
    }

    public void setMoWt(int moWt) {
        this.moWt = moWt;
    }

    public int getMoFl() {
        return moFl;
    }

    public void setMoFl(int moFl) {
        this.moFl = moFl;
    }

    public String toString() {
    	StringBuffer b = new StringBuffer();
        b.append(this.time).append(",")
         .append(this.queryType).append(",")
         .append(this.queryCode).append(",")
         .append(this.mtTlMsg).append(",")
         .append(this.mtTlUsr).append(",")
         .append(this.mtScs).append(",")
         .append(this.mtWt).append(",")
         .append(this.mtFl).append(",")
         .append(this.moScs).append(",")
         .append(this.moWt).append(",")
         .append(this.moFl);
        return b.toString();
    }    
}
