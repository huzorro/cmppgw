package com.enorbus.sms.gw.cmpp.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author shiwang
 * @version $Id: LongMessageUtil.java 2235 2009-03-05 18:31:17Z shishuo.wang $
 *
 */
public class LongMessageUtil {
	
	public static final int ASCII_MAXLENGTH = 159;
	public static final int LONG_MAXLENGTH = 140;
	public static final int NORMAL_MAXLENGTH = 70;
	
	public static final String ENCODING = "GBK";
	
	public static final String UCS2_ENCODING = "UnicodeBigUnmarked";

    /**
     * 按照长短信协议分割短信
     * @param msg 待分割短信
     * @param fmt 短信格式
     * @return 分割之后的字节数组列表
     */
    public static List<byte[]> splitLongMsg(String msg, int fmt) {
		int max_length = LONG_MAXLENGTH;
		if (fmt == MessageConst.MSGFMT_ASCII)
			max_length = ASCII_MAXLENGTH;
		
		List<byte[]> result = new ArrayList<byte[]>();
		
		byte[] gbMsg = null;
		try {
			gbMsg = msg.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
		}
		
		if (gbMsg.length > max_length) {
			byte r = (byte) new Random().nextInt(256);
			byte[] header = new byte[] {0x05, 0x00, 0x03, r, 0x00, 0x00};
			// 分割的长度
			int splitLen = max_length-header.length;
			
			byte[] ucs2Msg = null;
			try {
				ucs2Msg = msg.getBytes(UCS2_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int size = ucs2Msg.length/splitLen + (ucs2Msg.length%splitLen == 0 ? 0 : 1);
			for (int i=0; i < size; i++) {
				// 减去处理过的字节后，剩余的字节数
				int remainLen = ucs2Msg.length - i*splitLen;
				// 第i+1段的长度
				int len = header.length + (remainLen > splitLen ? splitLen : remainLen);
				byte[] b = new byte[len];
				
				header[header.length-2] = (byte) size;
				header[header.length-1] = (byte) (i+1);
				
				System.arraycopy(header, 0, b, 0, header.length);
				System.arraycopy(ucs2Msg, i*splitLen, b, header.length, b.length-header.length);
				result.add(b);
			}
		} else {
			result.add(gbMsg);
		}
		
		return result;
	}

        public static final int HEADER_LEN = 5; // 拆分后的短信前加入(1/5)字样，总数最多不能超过9条

    public static void main(String[] args) throws Exception {
        String orig = "GB2312 是汉字字符集和编码的代号，中文全称为“信息交换用汉字编码字符集”，由中华人民共和国" +
                "国家标准总局发布，一九八一年五月一日实施。GB 是“国标” 二字的汉语拼音缩写。GB2312 字符集 (character set) 只收录简化字汉字，以及一般常用字母和符号，主要通行于中国大陆地区和新加坡等地。";
        List<byte[]> lst = splitSimpleMsg(orig, MessageConst.MSGFMT_GB);

        for (byte[] b : lst) {
            System.out.println((new String(b, "GB2312")) + ", bytes: " + b.length);
        }
    }

    /**
     * 按照指定的字节长度分割短信，并在分割的短信内容前加上形如(1/5)的编号
     * @param msg 待分割短信
     * @param fmt 短信格式
     * @return 分割之后的字节数组列表
     */
    /*public static List<byte[]> splitSimpleMsg(String msg, int fmt) {
        int maxLen = 0;
        switch(fmt) {
            case MessageConst.MSGFMT_ASCII:
                maxLen = ASCII_MAXLENGTH;
                break;
            case MessageConst.MSGFMT_GB:
                maxLen = NORMAL_MAXLENGTH;
                break;
            default: throw new IllegalArgumentException("Unsupported msg format[" + fmt + "]");
        }

        List<byte[]> temp = new ArrayList<byte[]>();

        byte[] src = new byte[0];
        try {
            src = msg.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {}

        // 未超长，无需分割
        if (src.length <= maxLen) {
            temp.add(src);
            return temp;
        }

        int splitLen = maxLen - HEADER_LEN;
        int remainingLen = 0;
        byte[] fragment = null;
        int cur = 0; // 指向源字符串当前位置的指针
        int prev = 0; // 指向源字符串之前位置的指针

        while (true) {
            remainingLen = src.length - cur;
            cur += (splitLen > remainingLen ? remainingLen : splitLen);
            if ((src[cur - 1] & 0x000000FF) > 127) {
                // 最后一个字节是中文，进一步判断是中文字符的高字节（第一字节）还是低字节（第二字节），
                // 如果是低字节则可以正常划分，如果是高字节，则该字节应划分到下一个片断中去
                int count = 0;
                for (int i = cur - 2; i >= prev; i--) {
                    if ((src[i] & 0x000000FF) > 127)
                        count++;
                    else
                        break;
                }

                if (count % 2 == 0) {
                    // 最后一个字节是高字节，表示一个中文字符被拆成了两个，回退当前指针
                    cur--;
                }
            }
            fragment = new byte[cur - prev];
            System.arraycopy(src, prev, fragment, 0, fragment.length);
            
            temp.add(fragment);
            prev = cur;

            if (cur >= src.length) break;
        }

        // 在每一条短信内容前加入当前编号
        List<byte[]> result = new ArrayList<byte[]>();
        String prefix = null;
        for (int i = 0; i < temp.size(); i++) {
            prefix = "(" + (i + 1) + "/" + temp.size() + ")";
            result.add(ArrayUtils.addAll(prefix.getBytes(), temp.get(i)));
        }

        return result;
    }*/
    
    public static List<byte[]> splitSimpleMsg(String msg, int fmt) {
        int maxLen = 0;
        switch(fmt) {
            case MessageConst.MSGFMT_ASCII:
                maxLen = ASCII_MAXLENGTH;
                break;
            case MessageConst.MSGFMT_GB:
                maxLen = NORMAL_MAXLENGTH;
                break;
            default: throw new IllegalArgumentException("Unsupported msg format[" + fmt + "]");
        }

        List<byte[]> temp = new ArrayList<byte[]>();

        // 未超长，无需分割
        if (msg.length() <= maxLen) {
            try {
				temp.add(msg.getBytes(ENCODING));
			} catch (UnsupportedEncodingException e) {
			}
            return temp;
        }

        int splitLen = maxLen - HEADER_LEN;

        for (int i=0; true; i++) {
        	if (msg.length() > (i+1)*splitLen) {
        		try {
					temp.add( msg.substring(i*splitLen, (i+1)*splitLen).getBytes(ENCODING) );
				} catch (UnsupportedEncodingException e) {}
        	} else {
        		try {
					temp.add( msg.substring(i*splitLen).getBytes(ENCODING) );
				} catch (UnsupportedEncodingException e) {}
        		break;
        	}
        }

        // 在每一条短信内容前加入当前编号
        List<byte[]> result = new ArrayList<byte[]>();
        String prefix = null;
        for (int i = 0; i < temp.size(); i++) {
            prefix = "(" + (i + 1) + "/" + temp.size() + ")";
            result.add(ArrayUtils.addAll(prefix.getBytes(), temp.get(i)));
        }

        return result;
    }
}
