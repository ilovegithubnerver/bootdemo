package com.shiyi.service.impl;


import com.shiyi.service.XlzxAuthService;
import com.shiyi.util.Httpclient;
import com.shiyi.util.PageData;
import com.shiyi.util.RSAHelper;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("xlzxAuthService")
public class XlzxAuthServiceImpl implements XlzxAuthService {
	private String insId = "INS161215000002";
	private String operId = "huashi@zhengxin.com";
	private String url = "http://123.58.34.208:8188/xlzxop/certificate/auth/relCard.do";

	@Override
	public Map<String, Object> xlzxAuth(PageData pd) throws Exception {
		String mobile = pd.get("tel")==null?"":pd.get("tel")+"";// 手机号
		String cardNo = pd.get("bank_crad_no")==null?"":pd.get("bank_crad_no")+"";// 银行卡
		String name = pd.get("card_user_name")==null?"":pd.get("card_user_name")+"";// 银行卡用户姓名
		String cidNo = pd.get("id_card_no")==null?"":pd.get("id_card_no")+"";// 身份证号

		Calendar myDate = Calendar.getInstance();
		Date date = myDate.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String cooperSerialNo =sdf.format(date)+ new Random().nextInt(1000);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", "1.0");
		map.put("name", name);
		map.put("cooperSerialNo", cooperSerialNo);
		map.put("mobile", mobile);
		map.put("cidNo", cidNo);
		map.put("cardNo", cardNo);
	
		JSONObject temp=JSONObject.fromObject(map);
	    String jsonStr = map.toString();
        RSAHelper cipher = new RSAHelper();// 初始化自己的私钥,对方的公钥以及密钥长度.
        cipher.initKey(2048);      
        // 签名
        byte[] signBytes = cipher.signRSA(jsonStr.getBytes("UTF-8"), false, "UTF-8");
        // 对明文加密
        byte[] cryptedBytes = cipher.encryptRSA(jsonStr.getBytes("UTF-8"), false, "UTF-8");
        
        String sign = Base64.encodeBase64String(signBytes);// Base64编码
        String encrypt = Base64.encodeBase64String(cryptedBytes);// Base64编码
		
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

        System.out.println("返回报文:" + body);
        boolean isValid = cipher.verifyRSA(decryptedBytes, signBytes, false, "UTF-8");
        System.out.println("验签结果:" + (isValid ? "成功！" : "失败"));
        
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

	/*public static void main(String[] args) {
		XlzxAuthServiceImpl xl=new XlzxAuthServiceImpl();
		Map<String, Object> map;
		try {
			map = xl.xlzxAuth("18107153192","6228482409577867675", "柳嘉宾", "421023199204126698");
			System.out.println(map.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	}*/
	
}
