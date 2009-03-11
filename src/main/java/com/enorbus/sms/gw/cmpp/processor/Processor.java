package com.enorbus.sms.gw.cmpp.processor;

import org.apache.mina.core.session.IoSession;
import com.enorbus.sms.gw.cmpp.util.Counter;
import com.enorbus.sms.gw.cmpp.message.QueryRespMessage;
import com.enorbus.sms.gw.cmpp.message.CancelRespMessage;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: Processor.java 2205 2009-03-03 10:00:41Z zhi.long $
 */
public interface Processor {
    /**
     * 登录CMPP网关
     * @param session
     */
    void doLogin(IoSession session);
    
    /**
     * 响应登录应答
     */
    void onLoginResp(IoSession session, Object message);

    /**
     * 注销登录
     * @param session
     */
    void doLogout(IoSession session);

    /**
     * 注销登录（对所有session）
     */
    boolean doLogout();

    /**
     * 响应注销消息
     * @param session
     * @param message
     */    
    void onLogout(IoSession session, Object message);
    
    /**
     * 响应注销登录应答
     */
    void onLogoutResp(IoSession session, Object message);

    /**
     * 响应deliver消息
     */
    void onDeliver(IoSession session, Object message);

    /**
     * 响应状态报告消息
     */
    void onStatusReport(IoSession session, Object message);

    /**
     * 响应下发应答
     */
    void onSubmitResp(IoSession session, Object message);
    
    /**
     * 向ISMG查询某时间的业务统计情况，可以按总数或按业务代码查询
     */
    QueryRespMessage doQuery(String time, String queryCode);
    
    /**
     * 处理查询应答
     */
    void onQueryResp(IoSession session, Object message);
    
    /**
     * 删除已经提交给ISMG的短信
     */
    CancelRespMessage doCancel(String msgId);

    /**
     * 处理查询应答
     */
    void onCancelResp(IoSession session, Object message);

    /**
     * 维持连接
     */
    void doKeepAlive(IoSession session);

    /**
     * 维持连接应答
     * @param session
     */
    void onKeepAlive(IoSession session, Object message);

    /**
     * 处理维持连接应答
     */
    void onKeepAliveResp(IoSession session, Object message);

    /**
     * 清理Session
     * @param session
     */
    void onSessionClosed(IoSession session);

    /**
     * 取得Session计数器
     * @return session计数器
     */
    Counter getSessionCounter();

    /**
     * 注册Session
     * @param session 待注册session实例
     */
    void registerSession(IoSession session);

    /**
     * 注销Session
     * @param session 待注销session实例
     */
    void unregisterSession(IoSession session);
}
