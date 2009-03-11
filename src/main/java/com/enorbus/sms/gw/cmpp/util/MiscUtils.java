package com.enorbus.sms.gw.cmpp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;

/**
 * 混杂工具类
 *
 * @author Long Zhi
 * @version $Id: MiscUtils.java 2039 2009-02-11 07:45:47Z shishuo.wang $
 */
public class MiscUtils {
    public static final Logger logger = LoggerFactory.getLogger(MiscUtils.class);

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        return sdf.format(new Date());
    }

    public static byte[] md5(byte[] src) {
//        byte[] result = str.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(src);

        // now calculate the hash
        return md.digest();
    }
    
    //拆包以getString方式时的编码
    public static CharsetDecoder toDecoder() {
    	Charset charset = Charset.forName("gb2312");
		return charset.newDecoder();
	}

    // 封包以putString方式时的编码
    public static CharsetEncoder toEncoder() {
        return Charset.forName("gb2312").newEncoder();
    }
    
    //Object 转化为byte[]
    public static byte[] ObjectToByteArray(Object s) throws IOException {
    	ByteArrayOutputStream byteStream = new ByteArrayOutputStream(20000000);
		ObjectOutputStream os = new ObjectOutputStream (new BufferedOutputStream(byteStream));
		os.writeObject(s);
		os.flush();
		byte[] buf  = byteStream.toByteArray();
		os.close();
		return buf;
	}
    
    // 字节数组转换为16进制的字符串，用于调试输出
    public static final String bytesToHexString(byte[] bArray) {
    	StringBuffer sb = new StringBuffer(bArray.length);
    	String sTemp;
    	for (int i = 0; i < bArray.length; i++) {
    		sTemp = Integer.toHexString(0xFF & bArray[i]);
    		if (sTemp.length() < 2)
    			sb.append(0);
    		sb.append(sTemp.toUpperCase());
    		}
    	return sb.toString();
    }
}
