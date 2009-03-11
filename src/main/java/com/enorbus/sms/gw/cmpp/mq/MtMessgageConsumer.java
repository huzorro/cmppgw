package com.enorbus.sms.gw.cmpp.mq;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MtMessgageConsumer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void handleMessage(Map<String, Object> msg) {
		logger.debug("Got MT message: {}", msg);
		
	}
}
