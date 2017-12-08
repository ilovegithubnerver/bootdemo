package com.shiyi.util.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class SmsUtils {
	public static String sn = "8SDK-EMY-6699-RKQQP";// 软件序列号,请通过亿美销售人员获取
	public static String key = "259798";// 序列号首次激活时自己设定
	public static String password = "259798";// 密码,请通过亿美销售人员获取
	public static String baseUrl = "http://219.239.91.114:8080/sdkproxy/";
	
	public static Map newSms(Map map){
		try{
			String mdn = (String) map.get("tel");
			String message = String.valueOf(map.get("content"));
			StringBuffer param =new StringBuffer("") ;
			String code = "888";
			long seqId = System.currentTimeMillis();
			if (null!=map.get("content")) {
				message = URLEncoder.encode(message, "UTF-8");
				param.append("cdkey=").append(sn).append( "&password=").append(key).append("&phone=").append(mdn)
				.append("&message=").append(message).append("&addserial=").append(code).append("&seqid=").append(seqId);
			}else {
				message = URLEncoder.encode("尊敬的用户，您的本次验证码为:"+map.get("code"), "UTF-8");
				param.append("cdkey=").append(sn).append( "&password=").append(key).append("&phone=").append(mdn)
				.append("&message=").append(message).append("&addserial=").append(code).append("&seqid=").append(seqId);
			}
			
			String url = baseUrl + "sendsms.action";
			String ret = SDKHttpClient.sendSMS(url, param.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return null;
		  
		
	}
	
	
	public static Map newRegitSms(Map map){
		try{
		
			StringBuffer param =new StringBuffer("") ;
			param.append("cdkey=").append(sn).append( "&password=").append(key);
			
			
			String url = baseUrl + "regist.action";
			String ret = SDKHttpClient.sendSMS(url, param.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
		  
		
	}

}
