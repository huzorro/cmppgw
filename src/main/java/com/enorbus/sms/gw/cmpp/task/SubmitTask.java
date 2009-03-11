package com.enorbus.sms.gw.cmpp.task;

import org.apache.mina.core.session.IoSession;

import com.enorbus.sms.gw.cmpp.SessionManager;
import com.enorbus.sms.gw.cmpp.handler.RequestThread;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;

/**
 * 提交短信任务，往IoSession中写入SubmitMessage
 *
 * @author Long Zhi
 * @version $Id: SubmitTask.java 1995 2009-02-04 08:18:32Z shishuo.wang $
 */
public class SubmitTask extends RequestThread {
    private IoSession session;
    private SubmitMessage msg;

    public SubmitTask(IoSession session, SubmitMessage msg) {
        this.session = session;
        this.msg = msg;
    }

	public SubmitMessage getMsg() {
		return msg;
	}

	@Override
	protected void noResp() {
		super.noResp();
		SessionManager.getInstance().complete(session, msg.getHeader());
	}

	@Override
	protected void request() {
		this.session.write(msg);
	}
}
