package com.shiyi.controller;



import com.shiyi.service.AuthService;
import com.shiyi.util.*;
import com.shiyi.util.jiean.VerifyClientUtil;
import com.shiyi.util.jiean.VerifyRequest;
import com.shiyi.util.jiean.VerifyResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/app")
public class ItfController extends BaseController {

    @Autowired
	private AuthService authService;
	private static final String DEFAUL_TEST_MER_MAC_KEY = "​2bb6f69b9c19d8167d258ce1e5549c38";// 测试
	private static final String URL = "http://api.jieandata.com/vpre/ccmn/verify";

	private final Logger logger = LoggerFactory.getLogger(ItfController.class);

	/**
	 * @throws IOException
	 * @throws
	 * 
	 * @Title: 认证4要素
	 * @Description: 费用
	 * @author: zzz
	 * @date: 2016年4月24日 下午8:05:54
	 * @return void 返回类型
	 * @throws
	 * 
	 */
	@RequestMapping(value = "/Auth4.do", produces = "text/html;charset=utf-8")
	@ResponseBody
	public void Auth4(@RequestParam("sign")String sign, @RequestParam("chauth")String chauth, @RequestParam("tel")String tel,
                      @RequestParam("bank_crad_no")String bank_crad_no, @RequestParam("id_card_no")String id_card_no,
                      @RequestParam("platform_code")String platform_code, @RequestParam("card_user_name")String card_user_name, HttpServletResponse response)
			{

		PageData pd = new PageData();
		logger.info("4要素认证发送信息--------" + pd);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> mgs = new HashMap<String, Object>();
		String username = tel;
		pd.put("bank_crad_no",bank_crad_no);
		pd.put("card_user_name",card_user_name);
		pd.put("id_card_no",id_card_no);
		pd.put("tel",tel);
		pd.put("platform_code",platform_code);
		mgs.put("username", tel);
		mgs.put("bank_crad_no",bank_crad_no );
		mgs.put("card_user_name",card_user_name );
		mgs.put("id_card_no",id_card_no );
		returnMap.put("mgs", mgs);
		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String ch_Time = format.format(date);
		pd.put("ch_Time", ch_Time);
		pd.put("user_crad_name", card_user_name);
		try {
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
			sb.append("&ch_Time=");
			sb.append(ch_Time);
			sb.append("&platform_code=");
			sb.append(platform_code);
			sb.append("&key=");
			sb.append(platform_key);
			String Splice = sb.toString();
			String MD5 = DigestUtils.md5Hex(Splice).toUpperCase();
			logger.info("Splice--------------"+Splice);
			logger.info("serverSign--------------"+MD5);
			logger.info("sign--------------"+sign);
			if (!MD5.equalsIgnoreCase(sign)) {
//			if (false) {
				result.put("response_code","113");
				result.put("message","验签失败");
				returnMap.put("auth_code", "01");
				returnMap.put("bank_ret_code", "01");
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
				returnMap.put("bank_ret_code", "01");
				returnMap.put("auth_message", "认证失败");
				result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
			}


			 if (!(card_user_name.length()>1)||(username.length()!=11&&StringUtils.isNotBlank(username))) {
				returnMap.put("auth_code", "200");
				returnMap.put("auth_message", "姓名或者手机格式错误");
			}else {
			if ("0".equals(chauth)) {
				if (StringUtils.isBlank(username)) {
					pd.put("prod_id", "CARD3CN");
					
				}else{
					pd.put("prod_id", "CARD4");
				}
				Map<String, Object> auth4message ;
				pd.put("order_id",GenerateOidUtil.get());
				PageData temp=pd;
				if (Code==30) {
					auth4message = senior_auth4message(pd);
				}else if (Code==20){
					auth4message = intermediate_auth4message(pd);
				}else{
					
					auth4message = auth4message(pd);
					String reslut= (String) auth4message.get("auth_code");
					if(!"00".equals(reslut)){
						logger.info("huaishi faile  to jie an "+auth4message);
						auth4message=senior_auth4message(temp);
						logger.info("huaishi faile  to jie an "+auth4message);
					}
				}

				// 四要素认证
				// 银行卡类别{cardtype=借记卡, bank_ret_code=00}
				returnMap.put("auth_code", auth4message.get("auth_code") + "");
				returnMap.put("auth_message", auth4message.get("auth_message")
						+ "");

				if(30==Code){
					if("00".equals(String.valueOf(auth4message.get("auth_code")))||"042".equals(String.valueOf(auth4message.get("auth_code")))){
						authService.updateplatform_message(platform_code);

						authService.saveplatform_message(pd);
					}

				}else{
					authService.updateplatform_message(platform_code);
					authService.saveplatform_message(pd);
				}
			}
			}
			returnMap.put("order_id",pd.get("order_id"));
			result.put("data",returnMap);
			this.doResponse(response, JsonUtil.toJson(result));


		} catch (NullPointerException ne) {
			ne.printStackTrace();
			logger.error("参数异常：----------"+ne);
			logger.info(username + "4要素认证异常--------" + getTrace(ne));
			result.put("response_code","194");
			result.put("message","请检查请求参数");
			returnMap.put("auth_code", "01");
			returnMap.put("auth_message", "认证失败");
			result.put("data",returnMap);
			this.doResponse(response, JsonUtil.toJson(result));
		} catch (Exception e) {
			logger.info("4要素认证异常-"+getTrace(e));
			logger.info(username + "4要素认证异常--------" + getTrace(e));
			result.put("response_code","198");
			result.put("message","请求异常");
			returnMap.put("auth_code", "01");
			returnMap.put("auth_message", "认证失败");
			result.put("data",returnMap);
			this.doResponse(response, JsonUtil.toJson(result));
		}

	}

	public Map<String, Object> auth4message(PageData pd)
			throws Exception {
		logger.info("4要素认证auth4message--------" +pd);
		String username = (String) pd.get("tel"); // 电话号码
		String bank_crad_no = (String) pd.get("bank_crad_no");// 银行卡
		String card_user_name = (String) pd.get("user_crad_name");// 银行卡用户姓名
		String id_card_no = (String) pd.get("id_card_no");// 身份证号
		if (bank_crad_no != null & card_user_name != null) {
			try {
				pd.put("user_crad_name",
						new String(card_user_name.getBytes("UTF-8"), "UTF-8"));
				pd.put("qerFrom", 100000);
				pd.put("username", username);
				Date afterDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String transId = sdf.format(afterDate);
				pd.put("trans_id", transId.toString());
				pd.put("create_time", transId.toString());
				if (id_card_no.indexOf("X") > 0 || id_card_no.indexOf("x") > 0) {
					id_card_no = id_card_no.substring(0,
							id_card_no.length() - 1);
					pd.put("id_card_no", id_card_no.toString());
				}

				String field39 = "96";
				// logger.info("pd============"+pd);
				pd.put("id", "99");
				pd.put("card_type", "");
				pd.put("qerFrom", "");
//				曹连
				
				String params="username="+username+"&bank_crad_no="+bank_crad_no+"&user_crad_name="+card_user_name+"&id_card_no="+id_card_no+"&check_channel=100000";
				String rsp = HSURLConnection.sendHttpsPost("https://pay.sirui-cn.com:9335/app/handbrush_Pay_AuthCreditCardPre.do", params);

				
//				
//				String rsp = HttpUtil
//						.sendToServer(
//								"http://114.80.232.3:9235/app/handbrush_Pay_smallPosAuth.do",
//								pd, "UTF-8");
				System.out.println(rsp);
				JSONObject checkResponse = JSONObject.fromObject(rsp);
				String response_code = (String) checkResponse
						.get("response_code");// 响应成功
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("rsp_dsc", "认证成功");
				if ("00".equals(response_code)) {
					JSONObject body = checkResponse.getJSONObject("data");
					field39 = (String) body.get("ret_code");// 消费结果
					if (!"00".equals(field39)) {
						result.put("auth_code", field39);
						result.put("auth_message", "认证失败，认证信息不符，请核对再试");
					} else {
						authService.AuthCreditCardPre(pd);
						result.put("auth_code", field39);
						result.put("auth_message", "认证成功");
					}
				} else {
					result.put("auth_code", "197");
					result.put("auth_message", "返回失败");
				}

				return result;
			} catch (Exception e) {
				logger.info("4要素认证异常-"+getTrace(e));
				logger.info(username + "4要素认证异常--------" + getTrace(e));
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("auth_code", "198");
				result.put("auth_message", "认证失败，认证信息不符，请核对再试");
				return result;
			}
		} else {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("auth_code", "199");
			result.put("auth_message", "认证失败，认证信息不足");
			return result;
		}
	}

	
	public Map<String, Object> intermediate_auth4message(PageData pd)
			throws Exception {
		logger.info("4要素认证intermediate_auth4message--------" +pd);
		String username = (String) pd.get("tel"); // 电话号码
		String bank_crad_no = (String) pd.get("bank_crad_no");// 银行卡
		String card_user_name = (String) pd.get("user_crad_name");// 银行卡用户姓名
		String id_card_no = (String) pd.get("id_card_no");// 身份证号
		if (bank_crad_no != null & card_user_name != null) {
			try {
				pd.put("user_crad_name",
						new String(card_user_name.getBytes("UTF-8"), "UTF-8"));
				pd.put("qerFrom", 100000);
				pd.put("username", username);
				Date afterDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String transId = sdf.format(afterDate);
				pd.put("trans_id", transId.toString());
				pd.put("create_time", transId.toString());
				if (id_card_no.indexOf("X") > 0 || id_card_no.indexOf("x") > 0) {
					id_card_no = id_card_no.substring(0,
							id_card_no.length() - 1);
					pd.put("id_card_no", id_card_no.toString());
				}

				String field39 = "96";
				// logger.info("pd============"+pd);
				pd.put("id", "99");
				pd.put("card_type", "");
				pd.put("qerFrom", "");
//				曹连

				String params="username="+username+"&bank_crad_no="+bank_crad_no+"&user_crad_name="+card_user_name+"&id_card_no="+id_card_no+"&check_channel=100000";
				String rsp = HSURLConnection.sendHttpsPost("https://pay.sirui-cn.com:9335/app/handbrush_Pay_AuthCreditCardPre.do", params);

				System.out.println(rsp);
				JSONObject checkResponse = JSONObject.fromObject(rsp);
				String response_code = (String) checkResponse
						.get("response_code");// 响应成功
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("rsp_dsc", "认证成功");
				if ("00".equals(response_code)) {
					JSONObject body = checkResponse.getJSONObject("data");
					field39 = (String) body.get("ret_code");// 消费结果
					if (!"00".equals(field39)) {
						
						logger.info("4要素认证message_jiean--------" +pd);
						
						
						Map<String, Object> message_jiean = auth4message_jiean(pd);
						String respCode=message_jiean.get("respCode")+"";
						String respDesc=message_jiean.get("respDesc")+"";
							if (!"000".equals(respCode)) {
								result.put("auth_code", respCode);
								result.put("auth_message", "认证失败，认证信息不符，请核对再试");
							} else {
								authService.AuthCreditCardPre(pd);
								result.put("auth_code", "00");
								result.put("auth_message", "认证成功");
							}
					} else {
						authService.AuthCreditCardPre(pd);
						result.put("auth_code", field39);
						result.put("auth_message", "认证成功");
					}
				} else {
					result.put("auth_code", "197");
					result.put("auth_message", "返回失败");
				}

				return result;
			} catch (Exception e) {
				logger.info("4要素认证异常-"+getTrace(e));
				logger.info(username + "4要素认证异常--------" + getTrace(e));
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("auth_code", "198");
				result.put("auth_message", "认证失败，认证信息不符，请核对再试");
				return result;
			}
		} else {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("auth_code", "199");
			result.put("auth_message", "认证失败，认证信息不足");
			return result;
		}
	}
	
	public Map<String, Object> senior_auth4message(PageData pd)
			throws Exception {
		logger.info("4要素认证senior_auth4message--------" +pd);
           try {
				Map<String, Object> result = auth4message_jiean(pd);
				String respCode=result.get("respCode")+"";
				String respDesc=result.get("respDesc")+"";
					if (!"000".equals(respCode)) {
						result.put("auth_code", respCode);
						result.put("auth_message", "认证失败，认证信息不符，请核对再试");
					} else {
						authService.AuthCreditCardPre(pd);
						result.put("auth_code", "00");
						result.put("auth_message", "认证成功");
					}
				return result;
			} catch (Exception e) {
			   logger.info("4要素认证异常-"+getTrace(e));
				logger.info(pd.get("tel") + "4要素认证异常--------" + getTrace(e));
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("auth_code", "198");
				result.put("auth_message", "认证失败，认证信息不符，请核对再试");
				return result;
			}
	
	}
	
	public Map<String, Object> auth4message_jiean(PageData pd) throws Exception {

		String username = (String) pd.get("tel"); // 电话号码
		String bank_crad_no = (String) pd.get("bank_crad_no");// 银行卡
		String card_user_name = (String) pd.get("user_crad_name");// 银行卡用户姓名
		String id_card_no = (String) pd.get("id_card_no");// 身份证号
		String prod = (String) pd.get("prod_id");// 身份证号

		Map<String, Object> nvpss = new LinkedHashMap<String, Object>();
		nvpss.put("CARD_ID", bank_crad_no);
		nvpss.put("CERT_ID", id_card_no);
		nvpss.put("CERT_NAME", card_user_name);
		nvpss.put("MP", username);
		nvpss.put("PROD_ID", prod);
		// nvpss.put("CARD_ID", "6228480688740762374");
		// nvpss.put("CERT_ID", "35052119920809801X");
		// nvpss.put("CERT_NAME", "曾铮铮");
		// nvpss.put("MP", "15906023501");
		// nvpss.put("PROD_ID", "CARD4");
		VerifyRequest verifyRequest = new VerifyRequest();
		verifyRequest.setVersionId("01");
		verifyRequest.setChrSet("UTF-8");
		verifyRequest.setCustId("6000001156"); // 商户号
		verifyRequest.setOrdId("JIEAN" + GenerateOidUtil.get());
		verifyRequest.setTransType("STD_VERI");
		verifyRequest.setBusiType("");
		verifyRequest.setMerPriv("");
		verifyRequest.setRetUrl("");
		verifyRequest.setJsonStr(JSONObject.fromObject(nvpss).toString());
		verifyRequest.setMacStr(VerifyClientUtil.getMacStr(verifyRequest,
				DEFAUL_TEST_MER_MAC_KEY));
		Map<String, String> nvps = new HashMap<String, String>();
		nvps.put("versionId", verifyRequest.getVersionId());
		nvps.put("chrSet", verifyRequest.getChrSet());
		nvps.put("custId", verifyRequest.getCustId());
		nvps.put("ordId", verifyRequest.getOrdId());
		nvps.put("transType", verifyRequest.getTransType());
		nvps.put("busiType", verifyRequest.getBusiType());
		nvps.put("merPriv", verifyRequest.getMerPriv());
		nvps.put("retUrl", verifyRequest.getRetUrl());
		nvps.put("jsonStr", verifyRequest.getJsonStr());
		nvps.put("macStr", verifyRequest.getMacStr());
		String sendToServer = HttpUtil.sendToServer(URL, nvps, "UTF-8");
		System.out.println(sendToServer);
		JAXBContext jaxbContent = JAXBContext.newInstance(VerifyResponse.class);
		Unmarshaller unmarshaller = jaxbContent.createUnmarshaller();
		StringReader stringReader = new StringReader(sendToServer.toString());
		VerifyResponse verifyResponse = (VerifyResponse) unmarshaller
				.unmarshal(stringReader);
		String JsonStr = verifyResponse.getJsonStr();
		String respCode = verifyResponse.getRespCode();
		String respDesc = verifyResponse.getRespDesc();
		System.out.println("JsonStr=" + JsonStr);
		System.out.println("respCode=" + respCode);
		System.out.println("respDesc=" + respDesc );
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("respCode", respCode);
		result.put("respDesc", respDesc);
		return result;

	}
	
	/**
	 * TODO
	 * @throws IOException 
	 * @throws
	 * 
	 * @Title: 认证4要素和银行卡类别
	 * @Description: 费用
	 * @author: cjp;
	 * @date: 2016年4月24日 下午8:05:54
	 * @return void    返回类型
	 * @throws
	 *
	 */
	@RequestMapping(value="/AuthCreditCardPre.do", produces = "text/html;charset=utf-8")
	@ResponseBody
	public void auth(HttpServletRequest request, HttpServletResponse response) throws Exception, IOException{
		PageData pd = new PageData();
		pd = this.getPageData();
		logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("pd"+pd);
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>");
	   Map<String, Object> result = new HashMap<String, Object>();
	    Map<String, Object> returnMap= new  HashMap<String, Object>();
    	String username =(String) pd.get("tel"); 
    	String chcardtype =(String) pd.get("chcardtype"); 
    	String chauth =(String) pd.get("chauth"); 
		String bank_crad_no=(String) pd.get("bank_crad_no");//银行卡
		String card_user_name=(String) pd.get("card_user_name");//银行卡用户姓名
		//String card_user_name="刘培聪";//银行卡用户姓名
		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String ch_Time = format.format(date);
		pd.put("ch_Time", ch_Time);
		pd.put("user_crad_name", card_user_name);
		String id_card_no=(String) pd.get("id_card_no");//身份证号
		String platform_code=(String) pd.get("platform_code");//平台码
		String sign=(String) pd.get("sign");//签名

		  try{		
				 result.put("response_code","00");
				 result.put("message","请求成功");
			     PageData  platform_message = authService.getplatform_message(platform_code);
			     String platform_key = platform_message.get("platform_key")+"";
			     int Code = (int) platform_message.get("service_code");//平台服務等級碼
			     //组织签名
					StringBuffer sb = new StringBuffer();
					sb.append("bank_crad_no=");
					sb.append(bank_crad_no);
					sb.append("&ch_Time=");
					sb.append(ch_Time);
					sb.append("&platform_code=");
					sb.append(platform_code);
					sb.append("&key=");
					sb.append(platform_key);
					String Splice = sb.toString();
					String MD5 = DigestUtils.md5Hex(Splice).toUpperCase();
			  logger.info("Splice--------------"+Splice);
			  logger.info("serverSign--------------"+MD5);
			  logger.info("sign--------------"+sign);
			  if (!MD5.equalsIgnoreCase(sign)) {
						result.put("response_code","113");
						result.put("message","验签失败");
						returnMap.put("auth_code", "01");
						returnMap.put("bank_ret_code", "01");
		    			returnMap.put("auth_message", "认证失败");  
						result.put("data",returnMap);
						this.doResponse(response,JsonUtil.toJson(result));
						return;
					}
			        String surplus_num =  platform_message.get("surplus_num")+"";
			        int num = Integer.parseInt(surplus_num);
				    if (num <=10) {
				    	 result.put("response_code","112");
						 result.put("message","余额不足");
						 returnMap.put("auth_code", "01");
						 returnMap.put("bank_ret_code", "01");
			    		 returnMap.put("auth_message", "认证失败");
						 result.put("data",returnMap);
						this.doResponse(response,JsonUtil.toJson(result));
						return;
					}
			  		

			    
			    
			    if (!(card_user_name.length()>1)||(username.length()!=11&&StringUtils.isNotBlank(username))) {
					returnMap.put("auth_code", "200");
					returnMap.put("auth_message", "姓名或者手机格式错误");
				}else {
				
			    
			    
			    
			    if ("0".equals(chcardtype)) {
			    	//银行卡认证
			    	 Map<String, Object> card_typeresult =banktype(pd);
					 returnMap.put("bank_ret_code", card_typeresult.get("bank_ret_code")+"");
					 returnMap.put("cardtype", card_typeresult.get("cardtype")+"");
			    }
			    if ("0".equals(chauth)) {
			    	 //四要素认证
			    	
						if (StringUtils.isBlank(username)) {
							pd.put("prod_id", "CARD3CN");
							
						}else{
							pd.put("prod_id", "CARD4");
						}
						PageData temp=pd;
						Map<String, Object> auth4message ;
					    pd.put("order_id",GenerateOidUtil.get());
						if (Code==30) {
							auth4message = senior_auth4message(pd);
						}else if (Code==20){
							auth4message = intermediate_auth4message(pd);
						}else{

							auth4message = auth4message(pd);
							String reslut= (String) auth4message.get("auth_code");
							if(!"00".equals(reslut)){
								auth4message=senior_auth4message(temp);
							}
						}
//				    Map<String, Object> auth4message =auth4message(pd);
				    //银行卡类别{cardtype=借记卡, bank_ret_code=00}
			    	returnMap.put("order_id", pd.get("order_id")+"");
					returnMap.put("auth_code", auth4message.get("auth_code")+"");
			    	returnMap.put("auth_message", auth4message.get("auth_message")+"");
			    	if(30==Code){
			    		if("00".equals(String.valueOf(auth4message.get("auth_code")))||"042".equals(String.valueOf(auth4message.get("auth_code")))){
							authService.updateplatform_message(platform_code);
							authService.saveplatform_message(pd);
						}

					}else{
						authService.updateplatform_message(platform_code);
						authService.saveplatform_message(pd);
					}
			    }
				}
			    result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));

			}catch(NullPointerException ne){
				result.put("response_code","194");
				result.put("message","请检查请求参数");
				returnMap.put("auth_code", "01");
    			returnMap.put("auth_message", "认证失败");
				result.put("data",returnMap);
				this.doResponse(response,JsonUtil.toJson(result));
			}
		  catch(Exception e){
			    logger.info("4要素认证异常-"+getTrace(e));
				logger.info(username+"4要素认证异常--------"+getTrace(e));
				result.put("response_code","198");
				result.put("message","请求异常");
				returnMap.put("auth_code", "01");
    			returnMap.put("auth_message", "认证失败");
				result.put("data",returnMap);
				this.doResponse(response,JsonUtil.toJson(result));
			}
		
		
		
	}

	/**
	 * @throws IOException 
	 * @throws Exception
	 * 
	 * @Title: 银行卡类别
	 * @Description: 费用
	 * @author: cjp;
	 * @date: 2016年4月24日 下午8:05:54
	 * @return void    返回类型
	 * @throws
	 *
	 */
	@RequestMapping(value="/CreditCardPre.do", produces = "text/html;charset=utf-8")
	@ResponseBody
	public void CreditCardPre(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
	   Map<String, Object> result = new HashMap<String, Object>();
	    Map returnMap= new  HashMap<String, Object>();
    	String username =(String) pd.get("tel"); 
    	String chcardtype =(String) pd.get("chcardtype"); 
    	String chauth =(String) pd.get("chauth"); 
		String bank_crad_no=(String) pd.get("bank_crad_no");//银行卡
		String card_user_name=(String) pd.get("card_user_name");//银行卡用户姓名
		//String card_user_name="刘培聪";//银行卡用户姓名
		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String ch_Time = format.format(date);
		pd.put("ch_Time", ch_Time);
		pd.put("user_crad_name", card_user_name);
		String id_card_no=(String) pd.get("id_card_no");//身份证号
		String platform_code=(String) pd.get("platform_code");//平台码
		String sign=(String) pd.get("sign");//签名
		  try{		
				 result.put("response_code","00");
				 result.put("message","请求成功");
			     PageData  platform_message = authService.getplatform_message(platform_code);
			     String platform_key = platform_message.get("platform_key")+"";
			     //组织签名
					StringBuffer sb = new StringBuffer();
					sb.append("bank_crad_no=");
					sb.append(bank_crad_no);
					sb.append("&ch_Time=");
					sb.append(ch_Time);
					sb.append("&platform_code=");
					sb.append(platform_code);
					sb.append("&key=");
					sb.append(platform_key);
					String Splice = sb.toString();
					String MD5 = DigestUtils.md5Hex(Splice).toUpperCase();
			  if (!MD5.equalsIgnoreCase(sign)) {
						result.put("response_code","113");
						result.put("message","验签失败");
						returnMap.put("auth_code", "01");
						returnMap.put("bank_ret_code", "01");
		    			returnMap.put("auth_message", "请求失败");  
						result.put("data",returnMap);
						this.doResponse(response,JsonUtil.toJson(result));
						return;
					}
			        String surplus_card_num =  platform_message.get("surplus_card_num")+"";
			        int num = Integer.parseInt(surplus_card_num);
				    if (num <=10) {
				    	 result.put("response_code","112");
						 result.put("message","余额不足");
						 returnMap.put("auth_code", "01");
						 returnMap.put("bank_ret_code", "01");
			    		 returnMap.put("auth_message", "认证失败");
						 result.put("data",returnMap);
						this.doResponse(response,JsonUtil.toJson(result));
						return;
					}

			  authService.updateplatform_messagecard(platform_code);
			  authService.saveplatform_message(pd);

			    if ("0".equals(chcardtype)) {
			    	//银行卡认证
			    	 Map<String, Object> card_typeresult =banktype(pd);
					 returnMap.put("bank_ret_code", card_typeresult.get("bank_ret_code")+"");
					 returnMap.put("cardtype", card_typeresult.get("cardtype")+"");
			    }
			   
			    result.put("data",returnMap);
			    	
				this.doResponse(response, JsonUtil.toJson(result));
			}catch(NullPointerException ne){
				result.put("response_code","194");
				result.put("message","请检查请求参数");
				returnMap.put("auth_code", "01");
    			returnMap.put("auth_message", "认证失败");
				result.put("data",returnMap);
				this.doResponse(response,JsonUtil.toJson(result));
				return;
			}
		  catch(Exception e){
			    logger.info("4要素认证异常-"+getTrace(e));
				logger.info(username+"请求异常--------"+getTrace(e));
				result.put("response_code","198");
				result.put("message","请求异常");
				returnMap.put("auth_code", "01");
    			returnMap.put("auth_message", "请求失败");
				result.put("data",returnMap);
				this.doResponse(response,JsonUtil.toJson(result));
				return;
			}
	}

	
     public Map<String, Object> banktype(PageData pd) throws Exception{
		

		//String cardNo = "6217856400009005951";
		 Map<String, Object> result = new HashMap<String, Object>();
        //判断银行卡类别
		JSONObject jsonResult = null;
		JSONObject jsondataResult = null;
		DefaultHttpClient client = new DefaultHttpClient();
	    String bank_crad_no=(String) pd.get("bank_crad_no");//银行卡
	    String getCardTypeurl="http://e.apix.cn/apixcredit/bankcardinfo/bankcardinfo?cardno=";
        try {
        	//查询本地库
        	String cardNo = pd.get("bank_crad_no")+"";
        	String cardNo6 = cardNo.substring(0, 6);
        	String cardNo7 = cardNo.substring(0, 7);
        	String cardNo8 = cardNo.substring(0, 8);
        	String cardNo5 = cardNo.substring(0, 5);
        	pd.put("cardprefixnum", cardNo8);
        	pd.put("lenth", "8");
        	//8位为首要
        	PageData checkresult = authService.checkBankCard(pd);
        	if (checkresult==null||checkresult.isEmpty()) {
        		//7位为次要
        		pd.put("cardprefixnum", cardNo7);
        		pd.put("lenth", "7");
        		checkresult = authService.checkBankCard(pd);
        		if (checkresult==null||checkresult.isEmpty()) {
        			//6位为次要
	        		pd.put("cardprefixnum", cardNo6);
	        		pd.put("lenth", "6");
	        		checkresult = authService.checkBankCard(pd);
	        		if (checkresult==null||checkresult.isEmpty()) {
	        			//5位为次要
		        		pd.put("cardprefixnum", cardNo5);
		        		pd.put("lenth", "5");
		        		checkresult = authService.checkBankCard(pd);
		        		
					}
				}
        		
			}
        	if (checkresult!=null&&!"null".equals(checkresult)) {
        		//说明本地有信息
				result.put("bank_ret_code", "00");
				result.put("cardtype", checkresult.get("cardtype")+"");
				return result;
			}else {
        		//说明本地无信息
			      //发送get请求
			      HttpGet send_request = new HttpGet(getCardTypeurl+bank_crad_no);
			      LinkedHashMap<String,String> headers = new LinkedHashMap<String,String>();
			      headers.put("Content-type","application/json");
			      headers.put("Connection", "close");
			      headers.put("apix-key", "f0e116c7f502429a790d348e6f7bac65");
			      headers.put("cardno", bank_crad_no);
			      for (String key : headers.keySet()) {
			      	send_request.setHeader(key, headers.get(key));
			      }   
			      HttpResponse get_response = client.execute(send_request);
			      if (get_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			          String strResult = EntityUtils.toString(get_response.getEntity());
			          jsonResult = JSONObject.fromObject(strResult);
			          String isLuhnString = jsonResult.get("isLuhn")+"";
			          String code = jsonResult.get("code")+"";  
			          if (!"0".equals(code)) {
			        	  result.put("bank_ret_code", "299");
						  result.put("cardtype", "未找到该卡号信息");
						  return result;
					  }else {
						  String data = jsonResult.get("data")+"";
				          jsondataResult = JSONObject.fromObject(data);
				          String cardtype = jsondataResult.get("cardtype")+"";
				          String cardprefixnum = jsondataResult.get("cardprefixnum")+"";
				          String lenth = cardprefixnum.length()+"";
				          String banknum = jsondataResult.get("banknum")+"";
				          String bankname = jsondataResult.get("bankname")+"";
				          String servicephone = jsondataResult.get("servicephone")+"";
				          pd.put("cardtype", cardtype);
				          pd.put("cardprefixnum", cardprefixnum);
				          pd.put("lenth", lenth);
				          pd.put("banknum", banknum);
				          pd.put("bankname", bankname);
				          pd.put("servicephone", servicephone);
						  authService.savebanktype(pd);
						  result.put("bank_ret_code", "00");
						  result.put("cardtype", cardtype);
						  return result;
					}
			          
			      }else {
			    	  	  result.put("bank_ret_code", "299");
			    	  	  result.put("cardtype", "未找到该卡号信息");
				}
			}
            
        }catch (Exception e) {
        	  result.put("bank_ret_code", "299");
			  result.put("cardtype", "未找到该卡号信息");
		}
        return result;
		 
	}

	
	
	
	@RequestMapping(value="/doquery.do", produces = "text/html;charset=utf-8")
	@ResponseBody
	public void doquery(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
	   Map<String, Object> result = new HashMap<String, Object>();
	    Map returnMap=HandResponseBuilder.builderRetCode();
		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String ch_Time = format.format(date);
		pd.put("ch_Time", ch_Time);
		String chk_type = pd.get("chk_type")+"";
		String platform_code=(String) pd.get("platform_code");//平台码
		String sign=(String) pd.get("sign");//签名
		  try{		
				 result.put("response_code","00");
				 result.put("message","请求成功");
			     PageData  platform_message = authService.getplatform_message(platform_code);
			     String platform_key = platform_message.get("platform_key")+"";
			     //组织签名
					StringBuffer sb = new StringBuffer();
					sb.append("ch_Time=");
					sb.append(ch_Time);
					sb.append("&platform_code=");
					sb.append(platform_code);
					sb.append("&key=");
					sb.append(platform_key);
					String Splice = sb.toString();
					String MD5 = DigestUtils.md5Hex(Splice).toUpperCase();
			  if (!MD5.equalsIgnoreCase(sign)) {
						result.put("response_code","113");
						result.put("message","验签失败");
						returnMap.put("ret_code", "01");
						result.put("data",returnMap);
						this.doResponse(response,JsonUtil.toJson(result));
						return;
					}
				String surplus_num =  platform_message.get("surplus_num")+"";
			    String surplus_auth4_num =  platform_message.get("surplus_auth4_num")+"";
				String surplus_card_num =  platform_message.get("surplus_card_num")+"";	
				if ("1134".equals(chk_type)) {
					returnMap.put("surplus_num", surplus_num);
				}else if ("1024".equals(chk_type)) {
					returnMap.put("surplus_num", surplus_card_num);
				}else if ("1084".equals(chk_type)) {
					returnMap.put("surplus_num", surplus_auth4_num);
				}	
			    result.put("data",returnMap);
				this.doResponse(response, JsonUtil.toJson(result));
				return;
		  }catch(Exception e){
				logger.info("查询异常--------"+getTrace(e));
				result.put("response_code","198");
				result.put("message","请求异常");
				returnMap.put("auth_code", "01");
				result.put("data",returnMap);
				this.doResponse(response,JsonUtil.toJson(result));
				return;
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
