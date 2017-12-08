package com.shiyi.util;

import java.util.HashMap;
import java.util.Map;

public class HandResponseBuilder {
	
	public static Map<String, Object> builderSuccess(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "00");
		result.put("message", "");
		return result;
	}
	public static Map<String, Object> builderUpdate(){
		Map<String, Object> result = new HashMap<String, Object>();
				String rs="非常抱歉，系统维护，预计今日12时恢复";
		result.put("response_code", "0909");
		result.put("message", rs);
		return result;
	}
	public static Map<String, Object> builderCheckCodeError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "81");
		result.put("message", "短信验证码错误");
		return result;
	}
	
	public static Map<String, Object> builderLoginError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "82");
		result.put("message", "密码错误");
		return result;
	}
	
	public static Map<String, Object> buildeRegistedError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "83");
		result.put("message", "手机号已注册");
		return result;
	}
	
	public static Map<String, Object> builderUnLoginError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "84");
		result.put("message", "未登录");
		return result;
	}
	
	public static Map<String, Object> builderLessbalanceError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "85");
		result.put("message", "余额不足");
		return result;
	}
	
	public static Map<String, Object> builderUnTrueName(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "86");
		result.put("message", "未实名认证");
		return result;
	}
	
	public static Map<String, Object> builderWithdrawOnlyError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "87");
		result.put("message", "同一个时间只能提现或即时取一次");
		return result;
	}
	
	public static Map<String, Object> builderGetCheckCode(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "88");
		result.put("message", "刷验证码呐");
		return result;
	}
	
	public static Map<String, Object> builderSignError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "89");
		result.put("message", "签名错误");
		return result;
	}
	public static Map<String, Object> buildeAccountExsitError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "90");
		result.put("message", "账号不存在，请注册");
		return result;
	}
	public static Map<String,Object> builderAccessLimitBi(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "99");
		result.put("message", "即时取单笔超限");
		return result;
	}
	
	public static Map<String,Object> builderAccessLimitDay(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "100");
		result.put("message", "即时取当日超限");
		return result;
	}
	
	public static Map<String,Object> builderTransferLimitBi(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "100");
		result.put("message", "转账单笔超限");
		return result;
	}
	public static Map<String,Object> builderTransferLimitCount(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "100");
		result.put("message", "转账当日次数超限");
		return result;
	}
	public static Map<String,Object> builderTransferLimitDay(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "100");
		result.put("message", "转账当日超限");
		return result;
	}
	public static Map<String,Object> builderNotAllowTrans(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "07");
		result.put("message", "当前时间不允许交易");
		return result;
	}
	public static Map<String,Object> builderSysError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("response_code", "999");
		result.put("message", "系统繁忙，请稍后再试");
		return result;
	}
	
	public static Map<String,Object> builderRetCode(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ret_code", "00");
		result.put("rsp_dsc", "成功");
		return result;
	}
	public static Map<String,Object> builderFailRetCode(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ret_code", "99");
		result.put("rsp_dsc", "失败");
		return result;
	}
	public static Map<String,Object> builderActiveError(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("ret_code", "91");
		result.put("rsp_dsc", "账号未激活");
		return result;
	}
}
