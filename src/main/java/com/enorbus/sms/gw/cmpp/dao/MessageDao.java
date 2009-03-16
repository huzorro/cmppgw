package com.enorbus.sms.gw.cmpp.dao;

import java.util.Map;

import com.enorbus.sms.gw.cmpp.domain.Service;
import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;

public interface MessageDao {
	void saveMo(final DeliverMessage mo);
	void saveMt(final SubmitMessage mt);
	void updateMt(final Map<String, Object> map);
	Service getService(final Map parameterMap);
}
