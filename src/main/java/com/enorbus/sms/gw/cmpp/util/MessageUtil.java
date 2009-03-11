package com.enorbus.sms.gw.cmpp.util;

/**
 * 位操作工具类
 *
 * @author Long Zhi
 * @version $Id: MessageUtil.java 2216 2009-03-05 06:06:12Z zhi.long $
 */
public class MessageUtil {
	// big endian
	public static int getInt(byte[] stream, int offset) {
		return (((stream[offset] & 0x000000FF) << 24) | ((stream[offset + 1] & 0x000000FF) << 16) |
				((stream[offset + 2] & 0x000000FF) << 8) | (stream[offset + 3] & 0x000000FF));
	}

	//	big endian
	public static void putInt(int src, byte[] dst, int offset) {
		dst[offset] = (byte) ((src & 0xFF000000) >>> 24);
		dst[offset + 1] = (byte) ((src & 0x00FF0000) >>> 16);
		dst[offset + 2] = (byte) ((src & 0x0000FF00) >>> 8);
		dst[offset + 3] = (byte) ((src & 0x000000FF));
	}

	//	big endian
	public static short getShort(byte[] stream, int offset) {
		return (short) (((stream[offset] & 0x00FF) << 8) | (stream[offset + 1] & 0x00FF));
	}

	//	big endian
	public static void putShort(short src, byte[] dst, int offset) {
		dst[offset] = (byte) ((src & 0xFF00) >>> 8);
		dst[offset + 1] = (byte) ((src & 0x00FF));
	}

	// little endian
	public static int getIntLittle(byte[] stream, int offset) {
		return (((stream[offset + 3] & 0x000000FF) << 24) | ((stream[offset + 2] & 0x000000FF) << 16) |
				((stream[offset + 1] & 0x000000FF) << 8) | (stream[offset] & 0x000000FF));
	}

	//	little endian
	public static void putIntLittle(int src, byte[] dst, int offset) {
		dst[offset + 3] = (byte) ((src & 0xFF000000) >>> 24);
		dst[offset + 2] = (byte) ((src & 0x00FF0000) >>> 16);
		dst[offset + 1] = (byte) ((src & 0x0000FF00) >>> 8);
		dst[offset] = (byte) ((src & 0x000000FF));
	}


	// little endian
	public static short getShortLittle(byte[] stream, int offset) {
		return (short) (((stream[offset + 1] & 0x00FF) << 8) | (stream[offset] & 0x00FF));
	}

	// little endian
	public static void putShortLittle(short src, byte[] dst, int offset) {
		dst[offset] = (byte) ((src & 0x00FF));
		dst[offset + 1] = (byte) ((src & 0xFF00) >>> 8);
	}

	public static byte HI4BITS(byte src) {
		return (byte) ((src & 0xF0) >>> 4);
	}

	public static byte LO4BITS(byte src) {
		return (byte) (src & 0x0F);
	}

	public static String getString(byte[] src, int offset, int len) {
		return new String(src, offset, len);
	}

	public static void putString(byte[] src, int offset, String str) {
		byte[] byteStr = str.getBytes();
		for (int i = 0; i < byteStr.length; i++)
			src[offset + i] = byteStr[i];
	}
}
