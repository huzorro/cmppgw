package com.enorbus.sms.gw.cmpp.message;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_SUBMIT操作的目的是SP在与ISMG建立应用层连接后向ISMG提交短信。
 * ISMG以CMPP_SUBMIT_RESP消息响应。
 *
 * @author Long Zhi
 * @version $Id: SubmitMessage.java 2129 2009-02-18 09:29:28Z jinxue.liu $
 */
public class SubmitMessage extends AbstractMessage {
	private static final long serialVersionUID = -1646668733251643223L;

	/**
     * 信息标识
     * 8	Unsigned Integer
     */
    private long msgId;

    /**
     * 相同Msg_Id的信息总条数，从1开始。
     * 1	Unsigned Integer
     */
    private int pkTotal;

    /**
     * 相同Msg_Id的信息序号，从1开始
     * 1	Unsigned Integer
     */
    private int pkNumber;

    /**
     * 是否要求返回状态确认报告：0：不需要；1：需要
     * 1	Unsigned Integer
     */
    private int registeredDelivery;

    /**
     * 信息级别。
     * 1	Unsigned Integer
     */
    private int msgLevel;

    /**
     * 业务标识，是数字、字母和符号的组合。
     * 10	Octet String
     */
    private String serviceId;

    /**
     * 计费用户类型字段：
     * 0：对目的终端MSISDN计费；
     * 1：对源终端MSISDN计费；
     * 2：对SP计费；
     * 3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
     * 1	Unsigned Integer
     */
    private int feeUserType;

    /**
     * 被计费用户的号码，当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义。
     * 32	Octet String
     */
    private String feeTerminalId = "";

    /**
     * 被计费用户的号码类型，0：真实号码；1：伪码。
     * 1	Unsigned Integer
     */
    private int feeTerminalType;

    /**
     * GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.9。
     * 1	Unsigned Integer
     */
    private int tpPid;

    /**
     * GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐。
     * 1	Unsigned Integer
     */
    private int tpUdhi;

    /**
     * 信息格式：0：ASCII串；3：短信写卡操作；4：二进制信息；8：UCS2编码；15：含GB汉字。。。。。。
     * 1	Unsigned Integer
     */
    private int msgFmt;

    /**
     * 信息内容来源(SP_Id)
     * 6	Octet String
     */
    private String msgSrc = new String(new byte[6]);

    /**
     * 资费类别：
     * 01：对“计费用户号码”免费；
     * 02：对“计费用户号码”按条计信息费；
     * 03：对“计费用户号码”按包月收取信息费。
     * 2	Octet String
     */
    private String feeType = new String(new byte[2]);

    /**
     * 资费（以分为单位）。
     * 6	Octet String
     */
    private String feeCode = new String(new byte[6]);

    /**
     * 存活有效期，格式遵循SMPP3.3协议。
     * 17	Octet String
     */
    private String validTime = new String(new byte[17]);

    /**
     * 定时发送时间，格式遵循SMPP3.3协议。
     * 17	Octet String
     */
    private String atTime = new String(new byte[17]);
    
    /**
     * 源号码。SP的服务代码或前缀为服务代码的长号码,
     * 网关将该号码完整的填到SMPP协议Submit_SM消息相应的source_addr字段，
     * 该号码最终在用户手机上显示为短消息的主叫号码。
     * 21	Octet String
     */
    private String srcId = new String(new byte[21]);

    /**
     * 接收信息的用户数量(小于100个用户)。
     * 1	Unsigned Integer
     */
    private int destUsrTl;

    /**
     * 接收短信的MSISDN号码。
     * 32*DestUsr_tl	Octet String
     */
    private String destTerminalId;

    /**
     * 接收短信的用户的号码类型，0：真实号码；1：伪码。
     * 1	Unsigned Integer
     */
    private int destTerminalType;

    /**
     * 信息长度(Msg_Fmt值为0时：<160个字节；其它<=140个字节)，取值大于或等于0。
     * 1	Unsigned Integer
     */
    private int msgLength;

    /**
     * 信息内容。
     * Msg_length	Octet String
     */
    private byte[] msgContent;

	/**
     * 点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
     * 20	Octet String
     */
    private String linkId = new String(new byte[20]);
    
    /** 与此MT关联的MO的msgid */
    private String moMsgId;
    
    /** 提交时间 */
    private Date submitTime;
    
    /** 下发时间 */
    private Date doneTime;
    
    /** 下发状态码 */
    private String stat;
    
    /** 完整的短信内容 */
    private String msgContentStr;
    
    /** MSGID的字符串表示 */
    private String msgIdStr;
	
	public byte[] getMsgContent() {
		return msgContent;
	}
	
	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}
	
	public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getPkTotal() {
        return pkTotal;
    }

    public void setPkTotal(int pkTotal) {
        this.pkTotal = pkTotal;
    }

    public int getPkNumber() {
        return pkNumber;
    }

    public void setPkNumber(int pkNumber) {
        this.pkNumber = pkNumber;
    }

    public int getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(int registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public int getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(int msgLevel) {
        this.msgLevel = msgLevel;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getFeeUserType() {
        return feeUserType;
    }

    public void setFeeUserType(int feeUserType) {
        this.feeUserType = feeUserType;
    }

    public String getFeeTerminalId() {
        return feeTerminalId;
    }

    public void setFeeTerminalId(String feeTerminalId) {
        this.feeTerminalId = feeTerminalId;
    }

    public int getFeeTerminalType() {
        return feeTerminalType;
    }

    public void setFeeTerminalType(int feeTerminalType) {
        this.feeTerminalType = feeTerminalType;
    }

    public int getTpPid() {
        return tpPid;
    }

    public void setTpPid(int tpPid) {
        this.tpPid = tpPid;
    }

    public int getTpUdhi() {
        return tpUdhi;
    }

    public void setTpUdhi(int tpUdhi) {
        this.tpUdhi = tpUdhi;
    }

    public int getMsgFmt() {
        return msgFmt;
    }

    public void setMsgFmt(int msgFmt) {
        this.msgFmt = msgFmt;
    }

    public String getMsgSrc() {
        return msgSrc;
    }

    public void setMsgSrc(String msgSrc) {
        this.msgSrc = msgSrc;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getAtTime() {
        return atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public int getDestUsrTl() {
        return destUsrTl;
    }

    public void setDestUsrTl(int destUsrTl) {
        this.destUsrTl = destUsrTl;
    }

    public String getDestTerminalId() {
        return destTerminalId;
    }

    public void setDestTerminalId(String destTerminalId) {
        this.destTerminalId = destTerminalId;
    }

    public int getDestTerminalType() {
        return destTerminalType;
    }

    public void setDestTerminalType(int destTerminalType) {
        this.destTerminalType = destTerminalType;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public void setMoMsgId(String moMsgId) {
		this.moMsgId = moMsgId;
	}

	public String getMoMsgId() {
		return moMsgId;
	}

	public void setMsgContentStr(String msgContentStr) {
		this.msgContentStr = msgContentStr;
	}

	public String getMsgContentStr() {
		return msgContentStr;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(Date doneTime) {
		this.doneTime = doneTime;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public void setMsgIdStr(String msgIdStr) {
		this.msgIdStr = msgIdStr;
	}

	public String getMsgIdStr() {
		return msgIdStr;
	}

	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
