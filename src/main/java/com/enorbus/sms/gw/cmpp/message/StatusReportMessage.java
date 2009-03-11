package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author shiwang
 * @version $Id: StatusReportMessage.java 1994 2009-02-04 06:19:09Z shishuo.wang $
 *
 */
public class StatusReportMessage extends AbstractMessage {

	/**
	 * 信息标识。SP提交短信（CMPP_SUBMIT）操作时，与SP相连的ISMG产生的Msg_Id。
	 * 8	Unsigned Integer
	 */
	private byte[] msgId;
	
	// 用于DELIVER_RESP消息的msgId
	private byte[] deliverMsgId;
	
	// 用于保存在log中的msgId字符串
	private String msgIdStr;
	
	/**
	 * 发送短信的应答结果，含义详见表一。SP根据该字段确定CMPP_SUBMIT消息的处理状态。
	 * 7   Octet String
	 */
	private String stat;
	
	/**
	 * YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）。
	 * 10   Octet String
	 */
	private String submitTime;
	
	/**
	 * YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）。
	 * 10   Octet String
	 */
	private String doneTime;
	
	/**
	 * 目的终端MSISDN号码(SP发送CMPP_SUBMIT消息的目标终端)。
	 * 32   Octet String
	 */
	private String destTerminalId;
	
	/**
	 * 取自SMSC发送状态报告的消息体中的消息标识。
	 * 4   Unsigned Integer
	 */
	private int smscSequence;

	public byte[] getDeliverMsgId() {
		return deliverMsgId;
	}

	public void setDeliverMsgId(byte[] deliverMsgId) {
		this.deliverMsgId = deliverMsgId;
	}

	public byte[] getMsgId() {
		return msgId;
	}

	public void setMsgId(byte[] msgId) {
		this.msgId = msgId;
	}

	public String getMsgIdStr() {
		return msgIdStr;
	}

	public void setMsgIdStr(String msgIdStr) {
		this.msgIdStr = msgIdStr;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getDoneTime() {
		return doneTime;
	}

	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}

	public String getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String destTerminalId) {
		this.destTerminalId = destTerminalId;
	}

	public int getSmscSequence() {
		return smscSequence;
	}

	public void setSmscSequence(int smscSequence) {
		this.smscSequence = smscSequence;
	}

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
