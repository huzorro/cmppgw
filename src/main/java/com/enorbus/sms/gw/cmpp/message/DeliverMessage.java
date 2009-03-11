package com.enorbus.sms.gw.cmpp.message;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_DELIVER操作的目的是ISMG把从短信中心或其它ISMG转发来的短信送交SP，
 * SP以CMPP_DELIVER_RESP消息回应。
 *
 * @author Long Zhi
 * @version $Id: DeliverMessage.java 1994 2009-02-04 06:19:09Z shishuo.wang $
 */
public class DeliverMessage extends AbstractMessage {
	
	/**
	 * 错误代码
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
	 */
	private int errorCode = 0;
	
    public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
     * 信息标识。
     * 生成算法如下：
     * 采用64位（8字节）的整数：
     * （1）	时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中
     * bit64~bit61：月份的二进制表示；
     * bit60~bit56：日的二进制表示；
     * bit55~bit51：小时的二进制表示；
     * bit50~bit45：分的二进制表示；
     * bit44~bit39：秒的二进制表示；
     * （2）	短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中；
     * （3）	序列号：bit16~bit1，顺序增加，步长为1，循环使用。
     * 各部分如不能填满，左补零，右对齐。
     * 8	Unsigned Integer
     */
    private byte[] msgId;
    
    // 用于保存在log中的msgId字符串
    private String msgIdStr;

    public String getMsgIdStr() {
		return msgIdStr;
	}

	public void setMsgIdStr(String msgIdStr) {
		this.msgIdStr = msgIdStr;
	}

	/**
     * 目的号码。
     * SP的服务代码，或者是前缀为服务代码的长号码；该号码是手机用户短消息的被叫号码。
     * 21	Octet String
     */
    private String destId;

    /**
     * 业务标识，是数字、字母和符号的组合。
     * 10	Octet String
     */
    private String serviceId;

    /**
     * GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9。
     * 1	Unsigned Integer
     */
    private int tpPid;

    /**
     * GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23，仅使用1位，右对齐。
     * 1	Unsigned Integer
     */
    private int tpUdhi;

    /**
     * 信息格式：0：ASCII串；3：短信写卡操作；4：二进制信息；8：UCS2编码；15：含GB汉字。
     * 1	Unsigned Integer
     */
    private int msgFmt;

    /**
     * 源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码）。
     * 32	Octet String
     */
    private String srcTerminalId;

    /**
     * 源终端号码类型，0：真实号码；1：伪码。
     * 1	Unsigned Integer
     */
    private int srcTerminalType;

    /**
     * 是否为状态报告：0：非状态报告；1：状态报告。
     * 1	Unsigned Integer
     */
    private int registeredDelivery;

    /**
     * 消息长度，取值大于或等于0。
     * 1	Unsigned Integer
     */
    private int msgLength;

    /**
     * 消息内容。
     * Msg_length	Octet String
     */
    private String msgContent;

    /**
     * 点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
     * 20	Octet String
     */
    private String linkId;
    
    private String spId;
    
    
    private int gwId;
    
    private Timestamp regDate;

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public int getGwId() {
		return gwId;
	}

	public void setGwId(int gwId) {
		this.gwId = gwId;
	}

	public byte[] getMsgId() {
        return msgId;
    }

    public void setMsgId(byte[] msgId) {
        this.msgId = msgId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getSrcTerminalId() {
        return srcTerminalId;
    }

    public void setSrcTerminalId(String srcTerminalId) {
        this.srcTerminalId = srcTerminalId;
    }

    public int getSrcTerminalType() {
        return srcTerminalType;
    }

    public void setSrcTerminalType(int srcTerminalType) {
        this.srcTerminalType = srcTerminalType;
    }

    public int getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(int registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }    
}
