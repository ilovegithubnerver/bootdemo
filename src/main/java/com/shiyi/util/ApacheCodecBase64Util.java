package com.shiyi.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @Description 基于apache codec的 base64编码
 * 
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/7/21
 */
public class ApacheCodecBase64Util {
	
	public static String encodeStrintToString(String encodeStr)
			throws UnsupportedEncodingException {
		byte[] b = encodeStr.getBytes("UTF-8");
		Base64 base64 = new Base64();
		b = base64.encode(b);
		return new String(b, "UTF-8");
	}
	
	public static String encodeByteToString(byte[] encodeByte)
			throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		byte[] b = base64.encode(encodeByte);
		return new String(b, "UTF-8");
	}
	
	public static byte[] encodeStringToByte(String encodeStr)
			throws UnsupportedEncodingException {
		byte[] b = encodeStr.getBytes("UTF-8");
		Base64 base64 = new Base64();
		return base64.encode(b);
	}
	
	public static byte[] encodeByteToByte(byte[] encodeByte)
			throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		return base64.encode(encodeByte);
	}

	public static String decodeStringToString(String decodeStr) throws UnsupportedEncodingException {
		byte[] b = decodeStr.getBytes("UTF-8");
		Base64 base64 = new Base64();
		b = base64.decode(b);
		String s = new String(b, "UTF-8");
		return s;
	}
	
	public static String decodeByteToString(byte[] decodeByte) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		byte[] b = base64.decode(decodeByte);
		return new String(b, "UTF-8");
	}
	
	public static byte[] decodeStringToByte(String decodeStr) throws UnsupportedEncodingException {
		byte[] b = decodeStr.getBytes("UTF-8");
		Base64 base64 = new Base64();
		return base64.decode(b);
	}
	
	public static byte[] decodeByteToByte(byte[] decodeByte) throws UnsupportedEncodingException {
		Base64 base64 = new Base64();
		return base64.decode(decodeByte);
	}
}