package com.shiyi.service.impl;


import com.shiyi.service.AuthService;
import com.shiyi.service.InformationService;
import com.shiyi.util.HttpUtil;
import com.shiyi.util.PageData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service("informationService")
@PropertySource(  "classpath:sms.properties")
public class InformationServiceImpl implements InformationService {
	
	private static Logger logger = Logger.getLogger(InformationServiceImpl.class);

	@Autowired
	private AuthService authService;

	@Value("${baseUrl}")
	private String baseUrl;

	@Value("${sn}")
	private String sn;

	@Value("${key}")
	private String key;

	@Value("${password}")
	private String password;

	@Value("${code}")
	private String code;

	@Override
	public boolean send(String phone_no, String message_content, boolean isCode) {
		try {
			String message = "";
			long seqId = System.currentTimeMillis();
			if (!isCode) {
				message = message_content;
			} else {
				message = "尊敬的用户，您的本次验证码为:" + message_content;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cdkey", key);
			params.put("password", password);
			params.put("phone", phone_no);
			params.put("message", message);
			params.put("addserial", code);
			params.put("seqid", seqId);
			String url = baseUrl + "sendsms.action";
			HttpUtil.sendToServer_object(url, params, "utf-8");
		} catch (Exception e) {
			logger.error("短信出错 "+e);
			return false;
		}
		return true;
	}


	@Override
	public PageData findShortMessage(String platform_code) throws Exception {
		return authService.findShortMessage(platform_code);
	}

	@Override
	public void saveShortMessage(PageData pd) throws Exception {
		authService.saveShortMessage(pd);
	}




}
