package com.shiyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.shiyi.exception.IDCardAndNameServiceException;
import com.shiyi.exception.ShouChiPicCheckServiceException;
import com.shiyi.service.IDCardAndNameService;
import com.shiyi.service.ShouChiPicCheckService;
import net.sf.json.util.JSONTokener;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 天津人行指定机构的服务接口
 *
 * @author heqingqi
 * @date 2017/9/13
 */
@Service("tianJinRenHangService")
@PropertySource("classpath:auth.properties")
public class TianJinRenHangServiceImpl implements IDCardAndNameService, ShouChiPicCheckService, InitializingBean {
	
	public static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TianJinRenHangServiceImpl.class);

	private static Map<String,String> head = new HashMap<String, String>();
	
	private volatile int score = 70;
	
	@Value("${tjrh_auth_host}")
	private String auth_host;
	
	@Value("${tjrh_photo_host}")
	private String photo_host;

	@Value("${tjrh_auth}")
	private String auth;
	
	@Value("${tjrh_key}")
	private String key;
	
	/**
	 *  通过身份证号码和姓名，验证身份证号是否与姓名匹配； 即：两要素验证
	 *  
	 *  @param idCardNo 身份证号码， 必传
	 *  @param name 姓名
	 *  
	 *  @return 符合 true，不符合 false
	 **/
	@Override
	public boolean isCardMatchName(String idCardNo, String name) throws IDCardAndNameServiceException {
		logger.info("调用天津人行指定机构的身份证号与姓名匹配接口");
		logger.info("身份证:"+idCardNo);
		logger.info("姓名:"+name);
		
		if(StringUtils.isBlank(idCardNo) ||
				StringUtils.isBlank(name))
			throw new IDCardAndNameServiceException("身份证号 or 姓名不能为空");

		Map<String,String> body = new HashMap<String, String>();
		body.put("id_card", idCardNo);
		body.put("uname", name);
		
		try{
			HttpResponse httpResponse = com.shiyi.util.HttpUtils.doPost(auth_host, "", "POST", head, null, body);
			if(httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200){
				String tmp = new JSONTokener(EntityUtils.toString(httpResponse.getEntity())).nextValue().toString();
				JSONObject jSONObject = JSON.parseObject(tmp);
				int status_code = jSONObject.getIntValue("status_code");
				switch (status_code) {
					case 100:
						return true;
					case 101:
						return false;
					case 102:
						throw new IDCardAndNameServiceException("查询身份证号码为无效身份证号码");
					case 400:
						logger.error(jSONObject.getString("msg"));
						if(StringUtils.contains(jSONObject.getString("msg"), "余额不足"))
							throw new IDCardAndNameServiceException("查询失败，请联系商务");
						else
							throw new IDCardAndNameServiceException("服务通道异常");
					default:
						throw new IDCardAndNameServiceException("查询失败，请重试");
				}
			}else{
				logger.error(httpResponse);
				logger.error(httpResponse.getStatusLine().getStatusCode());
				throw new IDCardAndNameServiceException("服务通道异常");
			}
		}catch(Exception e){
			logger.error(e);
			throw new IDCardAndNameServiceException(e.getMessage());
		}
	}

	/**
	 *  通过身份证号码和姓名，验证身份证号是否与生活照匹配；
	 *  
	 *  @param idCardNo 身份证号码， 必传
	 *  @param picBase64 照片 base64编码的字符串
	 *  
	 *  @return 符合 true，不符合 false
	 **/
	@Override
	public boolean checkPicWithCardIdAndName(String idCardNo, String picBase64) throws ShouChiPicCheckServiceException {
		logger.info("调用天津人行指定机构的照片验证接口");
		logger.info("身份证:"+idCardNo);
		//logger.info("照片:"+picBase64);
		
		if(StringUtils.isBlank(idCardNo) ||
				StringUtils.isBlank(picBase64))
			throw new IDCardAndNameServiceException("身份证号 or 照片不能为空");

		Map<String,String> body = new HashMap<String, String>();
		body.put("id_card", idCardNo);
		body.put("img", picBase64);
		
		try{
			HttpResponse httpResponse = com.shiyi.util.HttpUtils.doPost(photo_host, "", "POST", head, null, body);
			if(httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200){
				String tmp = new JSONTokener(EntityUtils.toString(httpResponse.getEntity())).nextValue().toString();
				JSONObject jSONObject = JSON.parseObject(tmp);
				int status_code = jSONObject.getIntValue("status_code");
				switch (status_code) {
					case 100:
						float _score = jSONObject.getFloatValue("score")*100;
						int s = new Float(_score).intValue();
						if(s >= score)
							return true;
						else
							return false;
					case 101:
						throw new ShouChiPicCheckServiceException("查询身份证号码为无效身份证号码");
					case 102:
						throw new ShouChiPicCheckServiceException("请求超时，请重试");
					case 400:
						logger.error(jSONObject.getString("msg"));
						if(StringUtils.contains(jSONObject.getString("msg"), "余额不足"))
							throw new ShouChiPicCheckServiceException("查询失败，请联系商务");
						else
							throw new ShouChiPicCheckServiceException("服务通道异常");
					default:
						throw new ShouChiPicCheckServiceException("查询失败，请重试");
				}
			}else{
				logger.error(httpResponse);
				logger.error(httpResponse.getStatusLine().getStatusCode());
				throw new ShouChiPicCheckServiceException("服务通道异常");
			}
		}catch(Exception e){
			logger.error(e);
			throw new ShouChiPicCheckServiceException(e.getMessage());
		}
	}

	/**
	 *  设定相似度合格分数，默认为70分
	 **/
	@Override
	public void setScore(int score) throws ShouChiPicCheckServiceException {
		this.score = score;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		head.put("Authorization", "Basic "+com.shiyi.util.ApacheCodecBase64Util.encodeStrintToString(auth));
		head.put("X-API-KEY", key);
	}

}
