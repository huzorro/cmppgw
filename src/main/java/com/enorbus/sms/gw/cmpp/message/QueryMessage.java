package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_QUERY操作的目的是SP向ISMG查询某时间的业务统计情况，
 * 可以按总数或按业务代码查询。ISMG以CMPP_QUERY_RESP应答。
 *
 * @author Long Zhi
 * @version $Id: QueryMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class QueryMessage extends AbstractMessage {
    /**
     * 时间YYYYMMDD(精确至日)。
     * 8	Octet String
     */
    private String time;

    /**
     * 查询类别：0：总数查询；1：按业务类型查询。
     * 1	Unsigned Integer
     */
    private byte queryType;

    /**
     * 查询码。当Query_Type为0时，此项无效；当Query_Type为1时，此项填写业务类型Service_Id.。
     * 10	Octet String
     */
    private String queryCode;

    /**
     * 保留。
     * 8	Octet String	
     */
    private String reserve;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte getQueryType() {
        return queryType;
    }

    public void setQueryType(byte queryType) {
        this.queryType = queryType;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
