package com.enorbus.sms.gw.cmpp;

/**
 * 常量定义
 *
 * @author <a href="mailto:zhi.long@enorbus.com.cn">Long Zhi</a>
 * @version $Id: Constants.java 2217 2009-03-05 07:50:20Z shishuo.wang $
 */
public class Constants {
    // 避免初始化常量类
    private Constants() {}

    public static final int TOTAL_LENGTH_LEN = 4;

    public static final int COMMAND_ID_LEN = 4;
    
    public static final int SEQUENCE_ID_LEN = 4;

    public static final int HEADER_LEN = TOTAL_LENGTH_LEN + COMMAND_ID_LEN + SEQUENCE_ID_LEN;
    
    /** 请求连接 */
    public static final int CMD_CMPP_CONNECT = 0x00000001;

    /** 请求连接应答 */
    public static final int CMD_CMPP_CONNECT_RESP = 0x80000001;

    /** 终止连接 */
    public static final int CMD_CMPP_TERMINATE = 0x00000002;

    /** 终止连接应答 */
    public static final int CMD_CMPP_TERMINATE_RESP = 0x80000002;

    /** 提交短信 */
    public static final int CMD_CMPP_SUBMIT = 0x00000004;

    /** 提交短信应答 */
    public static final int CMD_CMPP_SUBMIT_RESP = 0x80000004;

    /** 短信下发 */
    public static final int CMD_CMPP_DELIVER = 0x00000005;

    /** 下发短信应答 */
    public static final int CMD_CMPP_DELIVER_RESP = 0x80000005;

    /** 发送短信状态查询 */
    public static final int CMD_CMPP_QUERY = 0x00000006;

    /** 发送短信状态查询应答 */
    public static final int CMD_CMPP_QUERY_RESP = 0x80000006;

    /** 删除短信 */
    public static final int CMD_CMPP_CANCEL = 0x00000007;

    /** 删除短信应答 */
    public static final int CMD_CMPP_CANCEL_RESP = 0x80000007;

    /** 激活测试 */
    public static final int CMD_CMPP_ACTIVE_TEST = 0x00000008;

    /** 激活测试应答 */
    public static final int CMD_CMPP_ACTIVE_TEST_RESP = 0x80000008;

    /** 协议版本号 */
    public static final byte PROTOCOL_VERSION = 0x30;

    /** MT-ONLY登录模式 */
    public static final byte MT_ONLY_LOGIN_MODE = 0;

    /** MO-ONLY登录模式 */
    public static final byte MO_ONLY_LOGIN_MODE = 1;

    /** BOTH MT AND MO登录模式，暂未使用 */
    public static final byte BOTH_LOGIN_MODE = PROTOCOL_VERSION;
    
    /** 通过响应id取得请求id的按位与 */
    public static final int CMD_AND = 0x0000000F;

    /** 无模式 */
    public static final int NONE_CONN_MODE = 2;

    public final static String CONNECT_REQ_ATTR_KEY = "connectReq";

    public static final String TERMINATE_REQ_ATTR_KEY = "terminateReq";

    public static final String AUTHSOURCE_ATTR_KEY = "AuthenticatorSource";
    
    public static final String TERMINAL_ID_SPLITTER = ",";
}
