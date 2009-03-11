package com.enorbus.sms.gw.cmpp.message;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * CMPP_CONNECT请求的响应消息
 *
 * @author Long Zhi
 * @version $Id: ConnectRespMessage.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class ConnectRespMessage extends AbstractMessage {
    /**
     * 状态：0：正确 1：消息结构错 2：非法源地址 3：认证错 4：版本太高 5~ ：其他错误
     * 4	Unsigned Integer
     */
    private int status;

    /**
     * ISMG认证码，用于鉴别ISMG。
     * 其值通过单向MD5 hash计算得出，表示如下：
     * AuthenticatorISMG = MD5（Status+AuthenticatorSource+shared secret），
     * Shared secret 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。
     * 认证出错时，此项为空。
     * 16字节 Octet String
     */
    private byte[] authenticatorIsmg;

    /**
     * 服务器支持的最高版本号，对于3.0的版本，高4bit为3，低4位为0
     * 1字节 Unsigned Integer
     */
    private byte version;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte[] getAuthenticatorIsmg() {
        return authenticatorIsmg;
    }

    public void setAuthenticatorIsmg(byte[] authenticatorIsmg) {
        this.authenticatorIsmg = authenticatorIsmg;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
