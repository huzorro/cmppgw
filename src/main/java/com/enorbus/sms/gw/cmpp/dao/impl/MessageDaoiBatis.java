package com.enorbus.sms.gw.cmpp.dao.impl;

import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.enorbus.sms.gw.cmpp.dao.MessageDao;
import com.enorbus.sms.gw.cmpp.domain.Service;
import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;

public class MessageDaoiBatis extends SqlMapClientDaoSupport implements MessageDao {

	@Override
	public void saveMo(DeliverMessage mo) {
		getSqlMapClientTemplate().insert("saveMo", mo);
	}

	@Override
	public void saveMt(SubmitMessage mt) {
		getSqlMapClientTemplate().insert("saveMt", mt);
	}

	@Override
	public void updateMt(Map<String, Object> map) {
		int updateFaileVaule = getSqlMapClientTemplate().update("updateMt", map);
		
		for (int i=0; i<3 && updateFaileVaule == 0; i++){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateFaileVaule=getSqlMapClientTemplate().update("updateMt", map);
		}		
	}

	@Override
	public Service getService(Map parameterMap) {
		return (Service) getSqlMapClientTemplate().queryForObject("getService", parameterMap);
	}
}
