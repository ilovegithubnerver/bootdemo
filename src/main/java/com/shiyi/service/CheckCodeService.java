package com.shiyi.service;

/**
 * 验证码保存工具服务
 * @author heqingqi
 * */
public interface CheckCodeService {
	/*
	 * 获取ip地址调用的次数，每天清空一次
	 */
	public int incrementAndGet(String ip) throws Exception;



	public  Boolean sendSMSWithContent(String appType, String tel, String content)throws Exception;
}
