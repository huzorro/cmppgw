/**
 * 
 */
package com.enorbus.sms.gw.cmpp.mq;

import java.sql.Timestamp;

import com.enorbus.sms.gw.cmpp.message.SubmitMessage;

/**
 * @author jxliu
 * @version $Id: MtLogMessage.java 2129 2009-02-18 09:29:28Z jinxue.liu $
 *
 */
public class MtLogMessage extends SubmitMessage {
	private int itemFee;
	private int monthFee;
    private Timestamp regDate;
    private int status;
	private int giveFee;
	private int firstMoMt;
	private int chargeWho;
	private String moMsgId;
	private int gwStatus;
	private Long columnId;
	private int isPlay;
	private String comeFrom;
	private String gwId;
	private String rptStatussir;
	private Timestamp gwDate;
	private Timestamp rptDate;
	private String fullContent;
	private String validTimeTs;
	private Timestamp atTimeTs;
	private String msgIdStr;
	
	public String getMsgIdStr() {
		return msgIdStr;
	}
	public void setMsgIdStr(String msgIdStr) {
		this.msgIdStr = msgIdStr;
	}
	public int getItemFee() {
		return itemFee;
	}
	public void setItemFee(int itemFee) {
		this.itemFee = itemFee;
	}
	public int getMonthFee() {
		return monthFee;
	}
	public void setMonthFee(int monthFee) {
		this.monthFee = monthFee;
	}
	public Timestamp getRegDate() {
		return regDate;
	}
	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getGiveFee() {
		return giveFee;
	}
	public void setGiveFee(int giveFee) {
		this.giveFee = giveFee;
	}
	public int getFirstMoMt() {
		return firstMoMt;
	}
	public void setFirstMoMt(int firstMoMt) {
		this.firstMoMt = firstMoMt;
	}
	public int getChargeWho() {
		return chargeWho;
	}
	public void setChargeWho(int chargeWho) {
		this.chargeWho = chargeWho;
	}
	public String getMoMsgId() {
		return moMsgId;
	}
	public void setMoMsgId(String moMsgId) {
		this.moMsgId = moMsgId;
	}
	public int getGwStatus() {
		return gwStatus;
	}
	public void setGwStatus(int gwStatus) {
		this.gwStatus = gwStatus;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public int getIsPlay() {
		return isPlay;
	}
	public void setIsPlay(int isPlay) {
		this.isPlay = isPlay;
	}
	public String getComeFrom() {
		return comeFrom;
	}
	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}
	public String getGwId() {
		return gwId;
	}
	public void setGwId(String gwId) {
		this.gwId = gwId;
	}
	public String getRptStatussir() {
		return rptStatussir;
	}
	public void setRptStatussir(String rptStatussir) {
		this.rptStatussir = rptStatussir;
	}
	public Timestamp getGwDate() {
		return gwDate;
	}
	public void setGwDate(Timestamp gwDate) {
		this.gwDate = gwDate;
	}
	public Timestamp getRptDate() {
		return rptDate;
	}
	public void setRptDate(Timestamp rptDate) {
		this.rptDate = rptDate;
	}
	public String getFullContent() {
		return fullContent;
	}
	public void setFullContent(String fullContent) {
		this.fullContent = fullContent;
	}
	public String getValidTimeTs() {
		return validTimeTs;
	}
	public void setValidTimeTs(String validTimeTs) {
		this.validTimeTs = validTimeTs;
	}
	public Timestamp getAtTimeTs() {
		return atTimeTs;
	}
	public void setAtTimeTs(Timestamp atTimeTs) {
		this.atTimeTs = atTimeTs;
	}
	

}
