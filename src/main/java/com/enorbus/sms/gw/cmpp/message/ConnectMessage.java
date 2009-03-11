package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_CONNECT操作的目的是SP向ISMG注册作为一个合法SP身份，
 * 若注册成功后即建立了应用层的连接，此后SP可以通过此ISMG接收和发送短信。
 * ISMG以CMPP_CONNECT_RESP消息响应SP的请求。
 *
 * @author Long Zhi
 * @version $Id: ConnectMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ConnectMessage extends AbstractMessage {
    /**
     * 源地址，此处为SP_Id，即SP的企业代码。
     * 6	Octet String
     */
    private String sourceAddr;

    /**
     * 用于鉴别源地址。其值通过单向MD5 hash计算得出，表示如下：
     * AuthenticatorSource = MD5（Source_Addr+9 字节的0 +shared secret+timestamp）
     * Shared secret 由中国移动与源地址实体事先商定，timestamp格式为：MMDDHHMMSS，即月日时分秒，10位。
     * 16	Octet String
     */
    private byte[] authenticatorSource;

    /**
     * 双方协商的版本号(高位4bit表示主版本号,低位4bit表示次版本号)，对于3.0的版本，高4bit为3，低4位为0
     * 1	Unsigned Integer
     */
    private byte version;

    /**
     * 时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐
     * 4	Unsigned Integer
     */
    private int timeStamp;

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public byte[] getAuthenticatorSource() {
        return authenticatorSource;
    }

    public void setAuthenticatorSource(byte[] authenticatorSource) {
        this.authenticatorSource = authenticatorSource;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
