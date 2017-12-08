package com.shiyi.service.impl;


import com.shiyi.controller.bean.XinLianAuthBean;
import com.shiyi.util.DateUtil;
import com.shiyi.util.DownLoadUtil;
import com.shiyi.util.GenerateOidUtil;
import com.shiyi.util.RSAHelper;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/9/6
 */
@Service("xinLianAuthService")
public class XinLianAuthServiceImpl {


    public org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    
    private   String url="http://123.58.34.208:8188/xlzxop/api/";
    private final  String operId="huashi@zhengxin.com";
    private final  String insId="INS161215000002";
    private final  int TIME_OUT=6000;




    public JSONObject  bankCardPicQuery(String picUrl){
        {
            HttpPost httpPost = new HttpPost(url+"idCardQuery.do");
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,TIME_OUT);
            String content="";
            RSAHelper cipher = new RSAHelper();// 初始化自己的私钥,对方的公钥以及密钥长度.
            cipher.initKey(2048);
            try {
                XinLianAuthBean authBean=new XinLianAuthBean();
                authBean.setOperId(operId);
                authBean.setVersion("1.0");
                authBean.setSerialNo(GenerateOidUtil.get());
                authBean.setRequestDate(DateUtil.getDays());
                authBean.setRequestTime(DateUtil.getMin());
                File file= DownLoadUtil.downloadPicture(picUrl);
                FileBody fileBody = new FileBody(file);
                MultipartEntity entity = new MultipartEntity();//文件
                entity.addPart("recognitionPhoto", fileBody);
                JSONObject object=JSONObject.fromObject(authBean);
                String jsonStr=object.toString();
                byte[] data=jsonStr.getBytes();
                Map dataMap=new HashMap();
                dataMap.put("insId",insId);
                dataMap.put("operId",operId);
                byte[] signBytes = cipher.signRSA(jsonStr.getBytes("UTF-8"), false, "UTF-8");
                String sign = Base64.encodeBase64String(signBytes);//Base64编码
                dataMap.put("sign",sign);
                byte[] cryptedBytes = cipher.encryptRSA(jsonStr.getBytes("UTF-8"),
                        false, "UTF-8");
                String cryptedEncryptString = Base64.encodeBase64String(cryptedBytes);// Base64编码
                System.out.println("no="+authBean.getSerialNo());
                entity.addPart("sign", new StringBody(new String(sign),Charset.forName("UTF-8")));
                entity.addPart("encrypt", new StringBody(new String(cryptedEncryptString),Charset.forName("UTF-8")));
                entity.addPart("insId", new StringBody(new String(insId),Charset.forName("UTF-8")));
                entity.addPart("operId", new StringBody(operId, Charset.forName("UTF-8")));
                httpPost.setEntity(entity);
                HttpResponse httpResponse = null;
                httpResponse = httpClient.execute(httpPost);
                HttpEntity resEntity = httpResponse.getEntity();
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    logger.info("正常响应");
                    if (resEntity != null) {
                        content = EntityUtils.toString(resEntity, "UTF-8");
                        logger.info("正常响应"+content);
                    }
                    EntityUtils.consume(resEntity);
                }
                JSONObject objectReturn=JSONObject.fromObject(content);
                 //{"result":{"bank_name":"中国建设银行","bank_identification_number":"1050000","card_type":"借记卡","card_number":"6227002470170278192",
                // "card_name":"龙卡储蓄卡(银联卡)"},"transDate":"20170912","queryStatus":"01","rspMsg":"交易成功",
                // "paySerialNo":"61709121625677387","transTime":"103730","reqSerialNo":"201709121036530","rspCod":"000000","version":"1.0"}
                cryptedBytes=Base64.decodeBase64(objectReturn.get("encrypt").toString());
                // 对密文解密
                byte[] decryptedBytes = cipher.decryptRSA(cryptedBytes, false, "UTF-8");
                return JSONObject.fromObject(new String(decryptedBytes,"UTF-8"));
            }catch (Exception e){
                logger.error("接口异常"+e.getMessage());
                JSONObject object= new JSONObject();
                object .put("queryStatus","02");
                object .put("queryMes","请求接口异常");
                return object;
            } finally {
                try {
                    httpClient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }



    public JSONObject  idCardCheckByPic(String picUrl){
        {
            HttpPost httpPost = new HttpPost(url+"bankCardQuery.do");
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,TIME_OUT);
            String content="";
            RSAHelper cipher = new RSAHelper();// 初始化自己的私钥,对方的公钥以及密钥长度.
            cipher.initKey(2048);
            try {
                XinLianAuthBean authBean=new XinLianAuthBean();
                authBean.setOperId(operId);
                authBean.setVersion("1.0");
                authBean.setSerialNo(GenerateOidUtil.get());
                authBean.setRequestDate(DateUtil.getDays());
                authBean.setRequestTime(DateUtil.getMin());
                File file=DownLoadUtil.downloadPicture(picUrl);
                FileBody fileBody = new FileBody(file);
                MultipartEntity entity = new MultipartEntity();//文件
                entity.addPart("recognitionPhoto", fileBody);
                JSONObject object=JSONObject.fromObject(authBean);
                String jsonStr=object.toString();
                byte[] data=jsonStr.getBytes();
                Map dataMap=new HashMap();
                dataMap.put("insId",insId);
                dataMap.put("operId",operId);
                byte[] signBytes = cipher.signRSA(jsonStr.getBytes("UTF-8"), false, "UTF-8");
                String sign = Base64.encodeBase64String(signBytes);//Base64编码
                dataMap.put("sign",sign);
                byte[] cryptedBytes = cipher.encryptRSA(jsonStr.getBytes("UTF-8"),
                        false, "UTF-8");
                String cryptedEncryptString = Base64.encodeBase64String(cryptedBytes);// Base64编码
                System.out.println("no="+authBean.getSerialNo());
                entity.addPart("sign", new StringBody(new String(sign),Charset.forName("UTF-8")));
                entity.addPart("encrypt", new StringBody(new String(cryptedEncryptString),Charset.forName("UTF-8")));
                entity.addPart("insId", new StringBody(new String(insId),Charset.forName("UTF-8")));
                entity.addPart("operId", new StringBody(operId, Charset.forName("UTF-8")));
                httpPost.setEntity(entity);
                HttpResponse httpResponse = null;
                httpResponse = httpClient.execute(httpPost);
                HttpEntity resEntity = httpResponse.getEntity();
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    logger.info("正常响应");
                    if (resEntity != null) {
                        content = EntityUtils.toString(resEntity, "UTF-8");
                        logger.info("正常响应"+content);
                    }
                    EntityUtils.consume(resEntity);
                }
                logger.info("content"+content);
                JSONObject objectReturn=JSONObject.fromObject(content);
                //返回报文:{"transDate":"20170911","queryStatus":"01","side":"front","validity":{"birthday":"true","sex":"true",
                // "address":"true","name":"true","cidno":"true"},"rspMsg":"交易成功","paySerialNo":"61709113310319760","transTime":"171317",
                // "reqSerialNo":"201709111712410","rspCod":"000000","info":{"sex":"男","address":"长春市绿园区支农大街54-57栋1门1楼102号","name":"刘军",
                // "cidno":"220104197503248213","month":"3","year":"1975","day":"24","nation":"汉"},"version":"1.0"}
                //签名
                // signBytes=	Base64.decodeBase64(objectReturn.get("sign").toString());
                //密文
                cryptedBytes=Base64.decodeBase64(objectReturn.get("encrypt").toString());
                // 对密文解密
                byte[] decryptedBytes = cipher.decryptRSA(cryptedBytes, false, "UTF-8");
                return JSONObject.fromObject(new String(decryptedBytes,"UTF-8"));
                //boolean isValid = cipher.verifyRSA(decryptedBytes,
                //        signBytes, false, "UTF-8");
                //System.out.println("验签结果:"+(isValid?"成功！":"失败"));
           }catch (Exception e){
                e.printStackTrace();
                logger.info("接口异常"+e.getMessage());
                JSONObject object= new JSONObject();
                object .put("queryStatus","02");
                object .put("queryMes","请求接口异常");
                return object;
            } finally {
                try {
                    httpClient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }


}
