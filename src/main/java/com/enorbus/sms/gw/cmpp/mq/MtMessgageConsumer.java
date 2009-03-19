package com.enorbus.sms.gw.cmpp.mq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.SessionManager;
import com.enorbus.sms.gw.cmpp.dao.MessageDao;
import com.enorbus.sms.gw.cmpp.domain.Service;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;
import com.enorbus.sms.gw.cmpp.task.SubmitTask;
import com.enorbus.sms.gw.cmpp.util.LongMessageUtil;
import com.enorbus.sms.gw.cmpp.util.MessageConst;
import com.enorbus.sms.gw.cmpp.util.WapPushEncode;

public class MtMessgageConsumer {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    private Config config = Config.getInstance();
    
    private MessageDao messageDao;
    
	/** MT下发线程池 */
    private ExecutorService threadPool;

	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void handleMessage(Map<String, Object> msg) {
		logger.debug("Got MT message: {}", msg);

		List<SubmitMessage> subMessage = new ArrayList<SubmitMessage>();
		String errMsg = null;
		
		if (msg.get("MSG_CONTENT") == null) {
			errMsg = "MSG_CONTENT can not be null";
			logger.error(errMsg);
			throw new NullPointerException(errMsg);
		}
		String msgContent = (String) msg.get("MSG_CONTENT");
		
		List<byte[]> msgList;
		
		int msgFmt = 0;
		if (msg.get("MSG_FMT") == null) {
			// 缺省为GB2312格式
			msgFmt = MessageConst.MSGFMT_GB;
		} else 	msgFmt = ((Integer) msg.get("MSG_FMT"));
		
		if (msgFmt == MessageConst.MSGFMT_ASCII
				|| msgFmt == MessageConst.MSGFMT_GB) {
			if (config.isSupportLongMsgProtocol()) {
				// 直接作为一条长短信发送到用户手机
				msgList = LongMessageUtil.splitLongMsg(msgContent, msgFmt);
			} else {
				// 分割长短信为多条发送到用户手机
				msgList = LongMessageUtil.splitSimpleMsg(msgContent, msgFmt);
			}
		} else if (msgFmt == MessageConst.MSGFMT_BIN) {
			// 目前二进制信息只支持WAP PUSH
			String pushContent[] = msgContent.split(",", 2);
			msgList = WapPushEncode.instance.encodePush(pushContent[0],
					pushContent[1]);
		} else {
			errMsg = "Unsupported message format[" + msgFmt + "]";
			logger.error(errMsg);
			throw new UnsupportedOperationException(errMsg);
		}

		// 计算接收手机号的数目
		if (msg.get("DEST_TERMINAL_ID") == null) {
			errMsg = "DEST_TERMINAL_ID can not be null";
			logger.error(errMsg);
			throw new NullPointerException(errMsg);	
		}
		int destUsrTl = msg.get("DEST_TERMINAL_ID").toString().split(
				Constants.TERMINAL_ID_SPLITTER).length;		
		String destTerminalId = (String) msg.get("DEST_TERMINAL_ID");
		
		Object msgLevel = msg.get("MSG_LEVEL");		
		Object serviceId = msg.get("SERVICE_ID");
		if (serviceId == null) {
			errMsg = "SERVICE_ID can not be null";
			logger.error(errMsg);
			throw new NullPointerException(errMsg);	
		}		
		
		Object feeTerminalId = msg.get("FEE_TERMINAL_ID");
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("serviceCode", (String) serviceId);
		parameterMap.put("spId", config.getSpId());		
		Service service = messageDao.getService(parameterMap);
		if (service == null) {
			errMsg = "ServiceId[" + serviceId + "] not found";
			logger.error(errMsg);
			throw new IllegalStateException(errMsg);
		}
		
		String atTime = null;
		if (msg.get("AT_TIME") != null) {
			atTime = (String) msg.get("AT_TIME");
		}
		
		// 长号码缺失时从数据库读取
		String srcId = null;
		if (msg.get("SRC_ID") != null) {
			srcId = (String) msg.get("SRC_ID");
		} else srcId = service.getProvider().getServiceNumber();

		Object linkId = msg.get("LINKID");
		Object moMsgId = msg.get("MO_MSG_ID");
		
		int smsCount = msgList.size(); // 分成的短信条数
		for (int i = 0; i < smsCount; i++) {
			SubmitMessage sm = new SubmitMessage();
			byte[] singleMsgContent = msgList.get(i); // 单条短信内容
			int smsLength = singleMsgContent.length; // 单条短信长度

			// 设置包头
			MessageHeader header = new MessageHeader();
			header.setTotalLength(Constants.HEADER_LEN + smsLength + 151 + 32
					* destUsrTl);
			header.setCommandId(Constants.CMD_CMPP_SUBMIT);
			header.setSequenceId(SeqGenerator.getInstance().getSeq());
			sm.setHeader(header);

			// 设置包体
			sm.setMsgId(0L);
			sm.setPkTotal(config.isSupportLongMsgProtocol() ? smsCount : 1);
			if (msgFmt == MessageConst.MSGFMT_BIN) // 对push总以长短信发送
				sm.setPkTotal(smsCount);

			// 设置当前为第几条短信
			sm.setPkNumber(config.isSupportLongMsgProtocol() ? i + 1 : 1);
			if (msgFmt == MessageConst.MSGFMT_BIN) // 对push总以长短信发送
				sm.setPkNumber(i + 1);
			
			sm.setRegisteredDelivery(1); // 总是需要网关返回状态报告

			if (msgLevel != null) {
				sm.setMsgLevel((Integer) msgLevel);
			}

			sm.setServiceId((String) serviceId);

			// 如果计费号码不为空，则对fee_terminal_id字段表示的号码计费，
			// 否则，对短信接收号码计费
			if (feeTerminalId != null) {
				sm.setFeeUserType(3);
				sm.setFeeTerminalId((String) feeTerminalId);
			} else {
				sm.setFeeUserType(0);
			}

			sm.setFeeTerminalType(0); // 被计费用户的号码类型，0：真实号码；1：伪码。
			sm.setTpPid(0);

			sm.setMsgFmt(msgFmt);
			if (msgFmt == MessageConst.MSGFMT_BIN) {
				// push
				sm.setTpUdhi(1);
			} else if (msgFmt == MessageConst.MSGFMT_GB
					|| msgFmt == MessageConst.MSGFMT_ASCII) {
				if (smsCount > 1 && config.isSupportLongMsgProtocol()) {
					// 长短信
					sm.setTpUdhi(1);
					sm.setMsgFmt(MessageConst.MSGFMT_UCS2);
				} else {
					sm.setTpUdhi(0);
				}
			} else {
				sm.setTpUdhi(0);
			}

			sm.setMsgSrc(config.getSpId());			
			sm.setFeeType(StringUtils.leftPad(String.valueOf(service.getFeeType()), 2, "0"));
			sm.setFeeCode(String.valueOf(service.getFeeCode()));

			// 设置有效存活期为24小时, 时间格式: YYMMDDhhmmsstnnp 
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmss");
			Calendar validTime = Calendar.getInstance();
			validTime.add(Calendar.DAY_OF_MONTH, 1);
			sm.setValidTime(sdf.format(validTime.getTime()) + "032+");

			// 设置定时时间，时间格式: YYMMDDhhmmsstnnp 
			if (atTime != null) {
				sm.setAtTime(atTime + "032+");
			}

			sm.setSrcId(srcId);
			sm.setDestUsrTl(destUsrTl);
			sm.setDestTerminalId(destTerminalId);
			sm.setDestTerminalType(0);
			sm.setMsgLength(smsLength); // 信息长度
			sm.setMsgContent(singleMsgContent); // 网关内容
			if (config.isSupportLongMsgProtocol()
					|| msgFmt == MessageConst.MSGFMT_BIN) {
				sm.setMsgContentStr(msgContent); // 记录完整内容
			} else {
				sm.setMsgContentStr(new String(singleMsgContent)); // 记录分割后的内容
			}

			if (linkId != null) {
				sm.setLinkId((String) linkId);
			} 

			sm.setMoMsgId((String) moMsgId);
			subMessage.add(sm);
		}
		
		send(subMessage);
	}
	
	private void send(List<SubmitMessage> msgs) {
		if (msgs.size() != 0){
        	for(SubmitMessage msg : msgs){
        		IoSession session = SessionManager.getInstance().findFreeSession();
        		SubmitTask task = new SubmitTask(session, msg);
        		
        		SessionManager.getInstance().wait(session, msg.getHeader(), task);
        		threadPool.execute(task);
        	}
        }
	}
}
