package com.enorbus.sms.gw.cmpp.mq;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.enorbus.sms.gw.cmpp.Constants;
import com.enorbus.sms.gw.cmpp.message.DeliverMessage;
import com.enorbus.sms.gw.cmpp.message.MessageHeader;
import com.enorbus.sms.gw.cmpp.message.SubmitMessage;
import com.enorbus.sms.gw.cmpp.support.Config;
import com.enorbus.sms.gw.cmpp.support.SeqGenerator;
import com.enorbus.sms.gw.cmpp.util.LongMessageUtil;
import com.enorbus.sms.gw.cmpp.util.MessageConst;
import com.enorbus.sms.gw.cmpp.util.WapPushEncode;

/**
 * Created by IntelliJ IDEA.
 *
 * @author <a href="mailto:zhi.long@enorbus.com.cn">Long Zhi</a>
 * @version $Id: MessageQueueExecutorOracleAqImpl.java 2241 2009-03-05 20:03:30Z shishuo.wang $
 */
public class MessageQueueExecutorOracleAqImpl extends SqlMapClientDaoSupport implements MessageQueueExecutor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Config config = Config.getInstance();
    
    public static final int FEETYPE_FREE = 1;                            // 对“计费用户号码”免费
    public static final int FEETYPE_ITEM_BY_ITEM = 2;                   // 对“计费用户号码”按条计信息费
    public static final int FEETYPE_BY_THE_MONTH = 3;                   // 对“计费用户号码”按包月收取信息费

    public int enqueue(DeliverMessage msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        logger.debug("put in mo data:" + msg);

        map.put("MSGID", msg.getMsgIdStr());
        map.put("GWID", config.getGwId());
        msg.setGwId(Integer.parseInt(config.getGwId()));
        map.put("SERVICECODE", msg.getServiceId());
        map.put("FROMNUMBER", msg.getSrcTerminalId());
        map.put("TONUMBER", msg.getDestId());
        map.put("MSGFMT", msg.getMsgFmt());
        map.put("TP_PID", msg.getTpPid());
        map.put("TP_UDHI", msg.getTpUdhi());
        map.put("CONTENT", msg.getMsgContent());
        map.put("MSGLEN", msg.getMsgLength());
        Date curDate = new Date();
        map.put("ENQDATE", curDate);
        msg.setRegDate(new Timestamp(curDate.getTime()));
        map.put("LINKID", msg.getLinkId());
        getSqlMapClientTemplate().queryForObject("MOQ_ENQ", map);

        return Integer.parseInt(map.get("RESULT_CODE").toString());
    }

    public List<SubmitMessage> dequeue() {
        List<SubmitMessage> subMessage = new ArrayList<SubmitMessage>();
        
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("QName", config.getQName());
            map.put("Consumer", config.getGwId());
            getSqlMapClientTemplate().queryForObject("MTQ_DEQ", map);

            String smsContent = (String) map.get("CONTENT");
            if (map.get("CONTENT") != null) {
                List<byte[]> msgList;
                int msgFmt = ((Integer) map.get("MSGFMT"));

                if (msgFmt == MessageConst.MSGFMT_ASCII || msgFmt == MessageConst.MSGFMT_GB) {
                    if (config.isSupportLongMsgProtocol()) {
                        // 直接作为一条长短信发送到用户手机
                        msgList = LongMessageUtil.splitLongMsg(smsContent, msgFmt);
                    } else {
                        // 分割长短信为多条发送到用户手机
                        msgList = LongMessageUtil.splitSimpleMsg(smsContent, msgFmt);
                    }
                } else if (msgFmt == MessageConst.MSGFMT_BIN) {
                    // 目前二进制信息只支持WAP PUSH
                    String pushContent[] = smsContent.split(",", 2);
                    msgList = WapPushEncode.instance.encodePush(pushContent[0], pushContent[1]);
                } else {
                    logger.error("Unsupported message format[{}]", msgFmt);
                    throw new UnsupportedOperationException("Unsupported message format[" + msgFmt + "]");
                }

                int smsCount = msgList.size();    //分成的短信条数

                for (int i = 0; i < smsCount; i++) {

                    MtLogMessage m = new MtLogMessage();

                    byte[] msgContent = msgList.get(i);     //短信内容
                    int smsLength = msgContent.length;        //短信长度




                    int destUsrTl = map.get("TONUMBER").toString().split(Constants.TERMINAL_ID_SPLITTER).length;
                    
                    //包头
                    MessageHeader header = new MessageHeader();
                    header.setTotalLength(Constants.HEADER_LEN + smsLength + 151 + 32 * destUsrTl);
                    header.setCommandId(Constants.CMD_CMPP_SUBMIT);
                    header.setSequenceId(SeqGenerator.getInstance().getSeq());
                    m.setHeader(header);

                    //包体
                    m.setMsgId(0L);
                    m.setPkTotal(config.isSupportLongMsgProtocol() ? smsCount : 1);
                    if (msgFmt == MessageConst.MSGFMT_BIN)       // 对push总以长短信发送
                        m.setPkTotal(smsCount);

                    //设置第几条短信
                    m.setPkNumber(config.isSupportLongMsgProtocol() ? i + 1 : 1);
                    if (msgFmt == MessageConst.MSGFMT_BIN)       // 对push总以长短信发送
                        m.setPkNumber(i + 1);
                    m.setRegisteredDelivery(1);


                    if (map.get("PRIORITY") != null) {
                        m.setMsgLevel((Integer) map.get("PRIORITY"));
                    }

                    if (map.get("SERVICECODE") != null) {
                        m.setServiceId((String) map.get("SERVICECODE"));
                    } else {
                        m.setServiceId(new String(new byte[10]));
                    }

                    //付费号码
                    if (map.get("FEENUMBER") != null) {
                        m.setFeeUserType(3);
                        m.setFeeTerminalId(map.get("FEENUMBER").toString());
                    } else {
                        m.setFeeUserType(0);
                        m.setFeeTerminalId("");
                    }

                    m.setFeeTerminalType(0);            // 被计费用户的号码类型，0：真实号码；1：伪码。
                    m.setTpPid(0);

                    if (msgFmt == MessageConst.MSGFMT_BIN) {
                        // push
                        m.setTpUdhi(1);
                        m.setMsgFmt(msgFmt);    
                    } else if (msgFmt == MessageConst.MSGFMT_GB || msgFmt == MessageConst.MSGFMT_ASCII) {
                        if (smsCount > 1 && config.isSupportLongMsgProtocol()) {
                            // 长短信
                            m.setTpUdhi(1);
                            m.setMsgFmt(MessageConst.MSGFMT_UCS2);
                        } else {
                            m.setTpUdhi(0);
                            m.setMsgFmt(msgFmt);
                        }
                    } else {
                        m.setTpUdhi(0);
                        m.setMsgFmt(msgFmt);                        
                    }

                    m.setMsgSrc(config.getSpId());
                    String feeType;
                    if (map.get("FEETYPE") != null) {
                        feeType = String.valueOf(map.get("FEETYPE"));
                    } else {
                        byte[] bytefeeType = new byte[2];
                        feeType = new String(bytefeeType);
                    }
                    m.setFeeType(StringUtils.leftPad(feeType, 2, "0"));

                    String fee;
                    if (Integer.valueOf(feeType) == FEETYPE_ITEM_BY_ITEM) {        //按条2
                        if (map.get("ITEMFEE") != null) {
                            fee = (String) map.get("ITEMFEE");
                        } else {
                            byte[] byteFee = new byte[6];
                            fee = new String(byteFee);
                        }

                    } else if (Integer.valueOf(feeType) == FEETYPE_BY_THE_MONTH) {    //包月3

                        if (map.get("MONTHFEE") != null) {
                            fee = String.valueOf( map.get("MONTHFEE"));
                        } else {
                            byte[] byteFee = new byte[6];
                            fee = new String(byteFee);
                        }
                    } else {                                                    //免费1
                        fee = String.valueOf(0);
                    }
                    m.setFeeCode(fee);

                    SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMddhhmmss");
                    Date currValidTime = new Date();

                    //validTime 时间格式化
                    String valid = dateFmt.format(new Date((currValidTime.getTime() / 1000 + Integer.valueOf(map.get("VALIDTIME").toString())) * 1000));
                    String validTime = valid + "032+";             //YYMMDDhhmmsstnnp 时间格式
                    m.setValidTime(validTime);

                    m.setValidTimeTs(String.valueOf(map.get("VALIDTIME")));      //入mtlog

                    //atTime时间格式化
                    String atDate;
                    if (map.get("ATDATE") != null) {
                        atDate = ((String) map.get("ATDATE")).substring(2) + "032+"; //YYMMDDhhmmsstnnp 时间格式
                    } else {
                        byte[] byteAtTime = new byte[17];
                        atDate = new String(byteAtTime);
                    }
                    m.setAtTime(atDate);

                    //入mtlog字段的时间
                    if (m.getAtTime().charAt(0) != 0) {
                        try {
                            m.setAtTimeTs(new Timestamp(dateFmt.parse(m.getAtTime().substring(0, 12)).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (map.get("FROMNUMBER") != null) {
                        m.setSrcId((String) map.get("FROMNUMBER"));
                    } else {
                        m.setSrcId(new String(new byte[21]));
                    }

                    /*if (map.get("USERNUM") != null) {
                        m.setDestUsrTl((Integer) map.get("USERNUM"));
                    }*/
                    m.setDestUsrTl(destUsrTl);
                    m.setDestTerminalId(map.get("TONUMBER").toString());

                    m.setDestTerminalType(0);

                    m.setMsgLength(smsLength);        //信息长度
                    m.setMsgContent(msgContent);     //网关内容
                    if (config.isSupportLongMsgProtocol() || msgFmt == MessageConst.MSGFMT_BIN) {
                        m.setFullContent(smsContent);    // 记录完整内容
                    } else {
                        m.setFullContent(new String(msgContent)); // 记录分割后的内容
                    }

                    if (map.get("LINKID") != null) {
                        m.setLinkId((String) map.get("LINKID"));
                    } else {
                        m.setLinkId(new String(new byte[20]));
                    }

                    //input MTlog user
                    if (map.get("ITEMFEE") != null) {
                        m.setItemFee((Integer) map.get("ITEMFEE"));
                    }

                    if (map.get("MONTHFEE") != null) {
                        m.setMonthFee((Integer) map.get("MONTHFEE"));
                    }

                    Date curDate = new Date();
                    m.setRegDate(new Timestamp(curDate.getTime()));

                    m.setMoMsgId((String) map.get("MSGID"));


                    if (map.get("COLUMNID") != null) {
                        m.setColumnId((Long) map.get("COLUMNID"));
                    }

                    if (map.get("FIRSTMOMT") != null) {
                        m.setFirstMoMt((Integer) map.get("FIRSTMOMT"));
                    }
                    if (map.get("CHARGEWHO") != null) {
                        m.setChargeWho((Integer) map.get("CHARGEWHO"));
                    }

                    m.setIsPlay(1);
                    m.setComeFrom((String) map.get("COMEFROM"));
                    m.setGwId((String) map.get("GWID"));

//                    logger.debug("submit data=:" + m);
                    subMessage.add(m);
                }
            }
        } catch (DataAccessException e) {
            logger.error("Error occured: ", e);
        }

        return subMessage;
    }

}
