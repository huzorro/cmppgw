package com.enorbus.sms.gw.cmpp.util;



import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: tyliu
 * Date: 2008-7-15
 * Time: 14:33:38
 */
public final class WapPushEncode {
    private final static Log logger = LogFactory.getLog(WapPushEncode.class);
    private final static byte[] MULTI_PUSH_HEADER = new byte[]{(byte) 0x0B, (byte) 0x05, (byte) 0x04, (byte) 0x0B, (byte) 0x84, (byte) 0x23, (byte) 0xF0, (byte) 0x00, (byte) 0x03};
    private final static byte[] SINGLE_PUSH_HEADER = new byte[]{0x06, 0x05, (byte) 0x04, (byte) 0x0B, (byte) 0x84, (byte) 0x23, (byte) 0xF0};
    private final static byte[] PUSH_HEADER2 = new byte[]{0x01, 0x06, (byte) 0x04, (byte) 0x03, (byte) 0xAE, (byte) 0x81, (byte) 0xEA};
    private final static byte[] PUSH_INDICATOR = new byte[]{(byte) 0x02, (byte) 0x05, (byte) 0x6A, (byte) 0x00, (byte) 0x45, (byte) 0xC6, (byte) 0x0C, (byte) 0x03};
    private final static byte[] TEXT_HEADER = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x03};
    private final static byte[] PUSH_END = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x01};
    private static int id = 0;

    public final static WapPushEncode instance = new WapPushEncode();

    //单例类
    private WapPushEncode() {

    }

    private int getSiLength(byte[] url, byte[] content) {
        return PUSH_HEADER2.length + PUSH_INDICATOR.length + url.length + TEXT_HEADER.length + content.length + PUSH_END.length;
    }

    private int getSmsId() {
        if (id < 255) {
            id = id + 1;
        } else {
            id = 0;
        }
        return id;
    }

    private byte[] hex(String url) {

        byte[] b = new byte[url.getBytes().length];
        for (int i = 0; i < url.length(); i++) {
            int ch = (int) url.charAt(i);

            b[i] = (byte) ch;

        }
        return b;
    }


    private byte[] hexUnicode(String content) {
        // 根据默认编码获取字节数组
        byte[] bytes = new byte[0];
        try {
            bytes = content.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            logger.error(e, e);
        }
        return bytes;
    }


    public List<byte[]> encodePush(String url, String title) {
        List<byte[]> result = new ArrayList<byte[]>();
        if (StringUtils.isBlank(url) || StringUtils.isBlank(title)) {
//            List result=new ArrayList();
            result.add(new byte[0]);
            return result;
        }
        int single_si_length = 140 - SINGLE_PUSH_HEADER.length;
        byte[] burl = this.hex(url);//URL字节码
        byte[] bcontent = this.hexUnicode(title);//内容的UTF-8字节码
        int si_content_length = this.getSiLength(burl, bcontent);
        byte[] si_byte = ArrayUtils.addAll(PUSH_HEADER2, PUSH_INDICATOR);
        si_byte = ArrayUtils.addAll(si_byte, burl);
        si_byte = ArrayUtils.addAll(si_byte, TEXT_HEADER);
        si_byte = ArrayUtils.addAll(si_byte, bcontent);
        si_byte = ArrayUtils.addAll(si_byte, PUSH_END);

        if (si_content_length > single_si_length) {
            int per_sms_length = 140 - MULTI_PUSH_HEADER.length - 3;
            //如果待发PUSH的字节长度大于一条PUSH的可用长度，那么就使用分条发送
            int num = (si_content_length / per_sms_length) + 1;//待发短信的条数
            int smsId = this.getSmsId();
            for (int i = 1; i <= num; i++) {
                byte[] per_sms_byte = ArrayUtils.subarray(si_byte, (i - 1) * per_sms_length, i * per_sms_length);
                byte[] rbyte = new byte[0];
                rbyte = ArrayUtils.addAll(MULTI_PUSH_HEADER, new byte[]{(byte) smsId, (byte) num, (byte) i});
                rbyte = ArrayUtils.addAll(rbyte, per_sms_byte);
                result.add(rbyte);
            }
        } else {
            //单条短信发送PUSH
            byte[] rbyte = new byte[0];
            rbyte = ArrayUtils.addAll(SINGLE_PUSH_HEADER, PUSH_HEADER2);
            rbyte = ArrayUtils.addAll(rbyte, PUSH_INDICATOR);

            rbyte = ArrayUtils.addAll(rbyte, burl);
            rbyte = ArrayUtils.addAll(rbyte, TEXT_HEADER);

            rbyte = ArrayUtils.addAll(rbyte, bcontent);
            rbyte = ArrayUtils.addAll(rbyte, PUSH_END);
            result.add(rbyte);
        }
        return result;
    }

    public static void main(String[] args) {
        List<byte[]> result = WapPushEncode.instance.encodePush("wap.disney.com.cn", "欢迎来到迪士尼！");
        System.out.println(result.size());
    }
}
