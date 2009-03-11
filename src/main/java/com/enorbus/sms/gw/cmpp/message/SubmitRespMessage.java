package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_SUBMIT的响应消息
 *
 * @author Long Zhi
 * @version $Id: SubmitRespMessage.java 1994 2009-02-04 06:19:09Z shishuo.wang $
 */
public class SubmitRespMessage extends AbstractMessage {
    /**
     * 信息标识，生成算法如下：
     * 采用64位（8字节）的整数：
     *  （1）	时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中
     *   bit64~bit61：月份的二进制表示；
     *   bit60~bit56：日的二进制表示；
     *   bit55~bit51：小时的二进制表示；
     *   bit50~bit45：分的二进制表示；
     *   bit44~bit39：秒的二进制表示；
     *  （2）	短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中；
     *  （3）	序列号：bit16~bit1，顺序增加，步长为1，循环使用。
     *   各部分如不能填满，左补零，右对齐。
     *  （SP根据请求和应答消息的Sequence_Id一致性就可得到CMPP_Submit消息的Msg_Id）
     * 8	Unsigned Integer
     */
    private byte[] msgId;
    
    private String msgIdStr;

    /**
     * 结果：
     * 0：正确；
     * 1：消息结构错；
     * 2：命令字错；
     * 3：消息序号重复；
     * 4：消息长度错；
     * 5：资费错；
     * 6：超过最大信息长；
     * 7：业务代码错；
     * 8：流量控制错；
     * 9：本网关不负责服务此计费号码；
     * 10：Src_Id错误；
     * 11：Msg_src错误；
     * 12：Fee_terminal_Id错误；
     * 13：Dest_terminal_Id错误；
     * ……
     * 4	Unsigned Integer
     */
    private int result;

    public String getMsgIdStr() {
		return msgIdStr;
	}

	public void setMsgIdStr(String msgIdStr) {
		this.msgIdStr = msgIdStr;
	}

	public byte[] getMsgId() {
        return msgId;
    }

    public void setMsgId(byte[] msgId) {
        this.msgId = msgId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }    
}
