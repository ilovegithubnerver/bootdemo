package com.shiyi.controller;


import com.shiyi.service.AuthService;
import com.shiyi.service.InformationService;
import com.shiyi.util.JsonUtil;
import com.shiyi.util.PageData;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/shortmsg")

public class SendInformationController extends BaseController {
	@Resource(name = "informationService")
	private InformationService informationService;

	@Autowired
	private AuthService authService;

	@RequestMapping(value = "/sendInformation.do", produces = "text/html;charset=utf-8")
	@ResponseBody
	public void sendInformation(HttpServletRequest request, HttpServletResponse response) {
		PageData pd = new PageData();
		pd = this.getPageData();

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> msg = new HashMap<String, Object>();

		String code = (String) pd.get("isCode");
		boolean isCode;
		if (code.equals("true")) {
			isCode = true;
		} else {
			isCode = false;
		}
		String phone_no = (String) pd.get("phone_no");// 电话号码
		String message_content = (String) pd.get("message_content");// 短信内容
		String platform_code = (String) pd.get("platform_code");// 平台码
		// String platform_key = (String) pd.get("platform_key");//秘钥
		String sign = (String) pd.get("sign");// 签名

		try {
			PageData umsg = authService.getplatform_message(platform_code);
			String platform_key = umsg.get("platform_key") + "";
			StringBuffer sb = new StringBuffer();
			sb.append("phone_no=");
			sb.append(phone_no);
			sb.append("&platform_code=");
			sb.append(platform_code);
			sb.append("&key=");
			sb.append(platform_key);
			String Splice = sb.toString();		
			String MD5 = DigestUtils.md5Hex(Splice).toUpperCase();
			System.out.println(sign);
			System.out.println(MD5);
			if (!MD5.equals(sign)) {//TODO
				msg.put("response_code", "-1");
				msg.put("message", "验签失败");
				resultMap.put("msg", msg);
				result.put("data", resultMap);
				result.put("success", "false");
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}
			String surplus_auth4_num = umsg
					.get("surplus_auth4_num") + "";
			int num = Integer.parseInt(surplus_auth4_num);
			if (num <= 10) {
				msg.put("response_code","-1");
				msg.put("message","余额不足");
				resultMap.put("msg", msg);
				result.put("data",resultMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}
			authService.updateplatform_messageauth4(platform_code);
			boolean flag = informationService.send(phone_no, message_content, isCode);
			if (flag) {
				 informationService.saveShortMessage(pd);				
					msg.put("response_code", "0");
					msg.put("isCode", isCode);
					msg.put("phone_no", phone_no);
					msg.put("message_content", message_content);
					msg.put("platform_code", platform_code);
					resultMap.put("msg", msg);
					result.put("success", flag);			
			} else {
				msg.put("response_code", "-1");
				msg.put("message", "短信发送失败");
				resultMap.put("msg", msg);
				result.put("success", flag);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}
			result.put("data", resultMap);
			this.doResponse(response, JsonUtil.toJson(result));
			return;

		} catch (Exception e) {
			logger.info(platform_code + "短信认证异常--------" + getMessage(e));
			msg.put("response_code", "-1");
			msg.put("message", "失敗！");
			resultMap.put("msg", msg);
			result.put("data", resultMap);			
			result.put("success", "false");
			this.doResponse(response, JsonUtil.toJson(result));
		}

	}

	public static String getMessage(Throwable t) {// 输出异常到LOG方法
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}

}
