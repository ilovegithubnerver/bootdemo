package com.shiyi.controller;


import com.shiyi.service.AuthService;
import com.shiyi.service.XlzxAuthService;
import com.shiyi.util.JsonUtil;
import com.shiyi.util.PageData;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/XlzxAuth4")
public class XlzxAuth4Controller extends BaseController {

	@Autowired
	private XlzxAuthService xlzxAuthService;

	@Autowired
	private AuthService authService;


	/**
	 * @throws IOException
	 * @throws ClientProtocolException
	 * 
	 * @Title: 认证xlzx银行卡鉴权
	 * @Description: 费用
	 * @author: ljb;
	 * @date: 2017年9月7日 上午14:28:54
	 * @return void 返回类型
	 * @throws
	 * 
	 */
	@RequestMapping(value = "/bankcardAuth.do", produces = "text/html;charset=utf-8")
	@ResponseBody
	public void Auth4(HttpServletRequest request, HttpServletResponse response)
			throws ClientProtocolException, IOException {
		PageData pd = new PageData();
		pd = this.getPageData();
		logger.info("4要素认证发送信息--------" + pd);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> mgs = new HashMap<String, Object>();
		
		//String username = pd.get("tel")+"";
		String username =pd.get("tel")==null?"":pd.get("tel")+"";
		//String bank_crad_no = pd.get("bank_crad_no")+"";// 银行卡
		String bank_crad_no=pd.get("bank_crad_no")==null?"":pd.get("bank_crad_no")+"";
		//String card_user_name = pd.get("card_user_name")+"";// 银行卡用户姓名
		String card_user_name=pd.get("card_user_name")==null?"":pd.get("card_user_name")+"";
		//String id_card_no = pd.get("id_card_no")+"";// 身份证号
		String id_card_no=pd.get("id_card_no")==null?"":pd.get("id_card_no")+"";
		System.out.println("-->长度："+username.length()+id_card_no.length()+card_user_name.length());
		mgs.put("tel", username);
		mgs.put("bank_crad_no",bank_crad_no );
		mgs.put("card_user_name",card_user_name );
		mgs.put("id_card_no",id_card_no );
		returnMap.put("mgs", mgs);
		String chauth = (String) pd.get("chauth");
		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String ch_Time = format.format(date);
		pd.put("ch_Time", ch_Time);
		pd.put("user_crad_name", card_user_name);
		pd.put("username", username);
		pd.put("bank_crad_no", bank_crad_no);
		pd.put("id_card_no", id_card_no);
		String platform_code = (String) pd.get("platform_code");// 平台码
		String sign = (String) pd.get("sign");// 签名
		try {
			if(bank_crad_no==null){
				result.put("response_code","113");
				result.put("message","请求失败");
				returnMap.put("auth_code", "01");
				returnMap.put("bank_crad_no", "200");
				returnMap.put("auth_message", "银行卡号不能为空");
				result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}
			result.put("response_code","00");
			result.put("message","请求成功");
			PageData platform_message = authService
					.getplatform_message(platform_code);
			String platform_key = platform_message.get("platform_key") + "";
			int Code = (int) platform_message.get("service_code");//平台服務等級碼
			
			// 组织签名
			StringBuffer sb = new StringBuffer();
			sb.append("bank_crad_no=");
			sb.append(bank_crad_no);
	/*		sb.append("&ch_Time=");
			sb.append(ch_Time);*/
			sb.append("&platform_code=");
			sb.append(platform_code);
			sb.append("&key=");
			sb.append(platform_key);
			String Splice = sb.toString();
			String MD5 = DigestUtils.md5Hex(Splice).toUpperCase();
			if (!MD5.equals(sign)) {
//			if (false) {
				result.put("response_code","113");
				result.put("message","验签失败");
				returnMap.put("auth_code", "01");
				//returnMap.put("bank_ret_code", "01");
				returnMap.put("auth_message", "验签失败");
				result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}
			String surplus_auth4_num = platform_message
					.get("surplus_auth4_num") + "";
			int num = Integer.parseInt(surplus_auth4_num);
			if (num <= 10) {
				result.put("response_code","112");
				result.put("message","余额不足");
				returnMap.put("auth_code", "01");
				//returnMap.put("bank_ret_code", "01");
				returnMap.put("auth_message", "认证失败");
				result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}

			authService.updateplatform_messageauth4(platform_code);
			authService.saveplatform_message(pd);
			if(card_user_name.length()>0||username.length()>0||id_card_no.length()>0){		
				if ("0".equals(chauth)) {
					Map<String, Object> auth4message= senior_auth4message(pd) ;
					returnMap.put("auth_code", auth4message.get("auth_code") + "");
					returnMap.put("auth_message", "4要素认证,"+auth4message.get("auth_message"));
				}else{
					result.put("response_code","112");
					result.put("message","非实名认证，请求失败");
					returnMap.put("auth_code", "01");
					returnMap.put("auth_message", "认证失败");
					result.put("data",returnMap);
					this.doResponse(response, JsonUtil.toJson(result));
					return;
				}
			}else {
				result.put("response_code","194");
				result.put("message","输入参数不满足要求，请检查请求参数");
				returnMap.put("auth_code", "01");
				returnMap.put("auth_message", "认证失败");
				result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}

			result.put("data",returnMap);
			this.doResponse(response, JsonUtil.toJson(result));
			return;

		} catch (NullPointerException ne) {
			result.put("response_code","194");
			result.put("message","请检查请求参数");
			returnMap.put("auth_code", "01");
			returnMap.put("auth_message", "认证失败");
			result.put("data",returnMap);
			this.doResponse(response, JsonUtil.toJson(result));
			return;
		} catch (Exception e) {
			logger.info(username + "4要素认证异常--------" + getTrace(e));
			result.put("response_code","198");
			result.put("message","请求异常");
			returnMap.put("auth_code", "01");
			returnMap.put("auth_message", "认证失败");
			result.put("data",returnMap);
			this.doResponse(response, JsonUtil.toJson(result));
			return;
		}

	}
	
	public Map<String, Object> senior_auth4message(PageData pd)
			throws ClientProtocolException, IOException {
		logger.info("4要素认证senior_auth4message--------" +pd);
           try {
				Map<String, Object> result = xlzxAuthService.xlzxAuth(pd);
				String rspCode=(String) result.get("rspCode");
				String rspMsg=(String) result.get("rspMsg");
				String validateStatus=result.get("validateStatus")+"";
				Date afterDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String transId = sdf.format(afterDate);
				pd.put("trans_id", transId.toString());
				pd.put("create_time", transId.toString());
					if ("000000".equals(rspCode)){					
						if("01".equals(validateStatus)){
							authService.AuthCreditCardPre(pd);
							result.put("auth_code", "00");
						    result.put("auth_message", "认证一致，认证成功");
						}else if("02".equals(validateStatus)){
							    result.put("auth_code",validateStatus);
								result.put("auth_message", "认证不一致，认证信息不符");
							}else{
								result.put("auth_code", validateStatus);
								result.put("auth_message", "认证不确定或认证失败，请重新输入认证信息");
							}			
					}else{
						result.put("auth_code", rspCode);
						if(rspMsg.length()>0){
						result.put("auth_message", "认证出现错误,错误码为："+rspCode+" "+"可参考错误信息："+rspMsg);
						}else{
						result.put("auth_message", "认证出现错误,错误码为："+rspCode);
						}
						logger.info(result + ":000000之外的返回结果--------" );
					}					
				return result;
			} catch (Exception e) {
				logger.info(pd + "4要素认证异常--------" + getTrace(e));
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("auth_code", "198");
				result.put("auth_message", "认证失败，认证信息不符，请核对再试");
				return result;
			}
	
	}
	
		
	public static String getTrace(Throwable t) {// 输出异常到LOG方法
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}
}

