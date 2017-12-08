package com.shiyi.util.jiean;


import org.apache.commons.codec.digest.DigestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 验证接口客户端演示工具类
 * @author fajor.fan
 */
public class VerifyClientUtil {
	private static final String DEFAUL_TEST_MER_MAC_KEY ="2bb6f69b9c19d8167d258ce1e5549c38";//测试
	/**
	 * 根据请求参数域的值以及MAC_KEY生成MAC_STR字段的值
	 * @param verifyRequest 请求参数
	 * @param macKey 商户MAC_KEY值
	 * @return MAC_STR值
	 */
	public static String getMacStr(VerifyRequest verifyRequest, String macKey){
		//注意，顺序连接所有请求参数，连接顺序不可打乱
		StringBuilder allInputAreaParams = new StringBuilder();
		allInputAreaParams.append(verifyRequest.getVersionId());
		allInputAreaParams.append(verifyRequest.getChrSet());
		allInputAreaParams.append(verifyRequest.getCustId());
		allInputAreaParams.append(verifyRequest.getOrdId());
		allInputAreaParams.append(verifyRequest.getTransType());
		allInputAreaParams.append(verifyRequest.getBusiType());
		allInputAreaParams.append(verifyRequest.getMerPriv());
		allInputAreaParams.append(verifyRequest.getRetUrl());
		allInputAreaParams.append(verifyRequest.getJsonStr());
		String allInputAreaParamsStr = allInputAreaParams.toString();
		
		String md5SourceText = allInputAreaParamsStr + DEFAUL_TEST_MER_MAC_KEY;
		System.out.println(allInputAreaParamsStr);
		System.out.println(DigestUtils.md5Hex(md5SourceText).toUpperCase());
		return DigestUtils.md5Hex(md5SourceText).toUpperCase();
	}
	
	/**
	 * 获取系统当前日期，格式yyyyMMdd
	 */
	public static String getCurrentDate(){
		Date nowDate = new Date();
		DateFormat sipleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return sipleDateFormat.format(nowDate);
	}
	
	/**
	 * 获取系统当前时间，格式HHmmss
	 */
	public static String getCurrentTime(){
		Date nowDate = new Date();
		DateFormat sipleDateFormat = new SimpleDateFormat("HHmmss");
		return sipleDateFormat.format(nowDate);
	}
}
