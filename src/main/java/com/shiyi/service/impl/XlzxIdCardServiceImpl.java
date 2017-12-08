package com.shiyi.service.impl;


import com.shiyi.service.XlzxIdCardService;
import com.shiyi.util.Httpclient;
import com.shiyi.util.PageData;
import com.shiyi.util.RSAHelper;
import com.shiyi.util.jiean.VerifyClientUtil;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("xlzxIdCardService")
public class XlzxIdCardServiceImpl implements XlzxIdCardService {
	
	private String insId = "INS161215000002";
	private String operId = "huashi@zhengxin.com";
	private String url = "http://123.58.34.208:8188/xlzxop/certificate/idAuthNoPicture.do";
	
	@Override
	public Map<String, Object> xlzxIdCard(PageData pd) throws Exception {
		String trueName = (String) pd.get("card_user_name");// 用户姓名
		String certNo = (String) pd.get("id_card_no");// 身份证号
        String certType=(String) pd.get("certType");//证件类型必须是身份证“01” 

        String transDate= VerifyClientUtil.getCurrentDate();
        String transTime=VerifyClientUtil.getCurrentTime();
		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String serialNo =sdf.format(date)+new Random().nextInt(1000);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", "1.0");	
		map.put("transDate", transDate);
		map.put("transTime", transTime);
		map.put("trueName", trueName);
		map.put("certType","01");
		map.put("certNo", certNo);
		map.put("serialNo", serialNo);
		map.put("resv", "");
		
		JSONObject temp=JSONObject.fromObject(map);
		String jsonStr =temp.toString();
		
        RSAHelper cipher = new RSAHelper();// 初始化自己的私钥,对方的公钥以及密钥长度.
        cipher.initKey(2048);
        // 签名
        byte[] signBytes = cipher.signRSA(jsonStr.getBytes("UTF-8"), false, "UTF-8");   
        // 对明文加密
        byte[] cryptedBytes = cipher.encryptRSA(jsonStr.getBytes("UTF-8"), false, "UTF-8");
        
        String sign = Base64.encodeBase64String(signBytes);// Base64编码
        String encrypt = Base64.encodeBase64String(cryptedBytes);// Base64编码
		/*byte[] data=map.toString().getBytes();		
		String sign = RSAUtils.sign(data,RSAUtils.getprivatekey());
		String encrypt = RSAUtils.encryptBASE64(RSAUtils.encryptByPublicKey(data, RSAUtils.getpublickey()));*/ /// 生成的密文

		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, String> result = new HashMap<String, String>();
		result.put("insId", insId);
		result.put("operId", operId);
		result.put("sign", sign);
		result.put("encrypt", encrypt);
		String rs = Httpclient.sendRequestMethod(result, url, "post", 3000);
		JSONObject object = JSONObject.fromObject(rs);		
		 // 签名
        signBytes = Base64.decodeBase64(object.get("sign").toString());
        // 密文
        cryptedBytes = Base64.decodeBase64(object.get("encrypt").toString());
        // 对密文解密
        byte[] decryptedBytes = cipher.decryptRSA(cryptedBytes, false, "UTF-8");
        String body = new String(decryptedBytes, "UTF-8");
        boolean isValid = cipher.verifyRSA(decryptedBytes, signBytes, false, "UTF-8");
        // System.out.println("验签结果:" + (isValid ? "成功！" : "失败"));
		//System.out.println("返回的主体数据------------>" + body);
		
		JSONObject obj = JSONObject.fromObject(body);
		String rspCode = (String) obj.get("rspCod");
		String rspMsg = (String) obj.get("rspMsg");
		String execType = (String) obj.get("execType");
		String validateStatus = (String) obj.get("validateStatus");
		results.put("rspCode", rspCode);
		results.put("rspMsg", new String(rspMsg.getBytes("UTF-8"), "UTF-8"));
		results.put("execType", execType);
		results.put("validateStatus", validateStatus);
		return results;
	}

}
