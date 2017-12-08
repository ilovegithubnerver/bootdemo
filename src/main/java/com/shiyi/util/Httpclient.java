package com.shiyi.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Httpclient {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Httpclient.class);
	
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String CHARACTER_ENCODING = "UTF-8";
	

	public static String sendGetRequestMethod(String body, String url,int timeout) throws Exception{
		
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault(); 
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout*1000)
                .setConnectTimeout(timeout*1000)
                .setConnectionRequestTimeout(timeout*1000)
                .build();
		HttpGet httpGet = new HttpGet(url+"?"+body); // 一定要这样才能发送出去短信
        try {
        	httpGet.setConfig(requestConfig);
        	httpGet.setHeader("Content-Type", "text/html");   
            
            CloseableHttpResponse response = null;
            if(httpclient != null)
            	response = httpclient.execute(httpGet);
            else
            	throw new Exception("create CloseableHttpClient failed.");
            
           if(response != null && response.getStatusLine().getStatusCode() == 200){
        	   String rtn = EntityUtils.toString(response.getEntity());
        	   if(logger.isDebugEnabled())
       			logger.debug("sendRequestMethodPost return :"+rtn);
        	   return rtn;
	        }
	        else{
	        	if(response != null)
	        		logger.warn(" status code {} "+response.getStatusLine().getStatusCode());
	        	logger.warn(" server error, return null");
	        	return null;
	        }
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally{
			if(httpclient != null)
				httpclient.close();
		}
	}
	
	public static String sendRequestMethod(Map<String, String> map, String url, String method, int timeout) throws Exception{
		
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault(); 
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout*1000)
                .setConnectTimeout(timeout*1000)
                .setConnectionRequestTimeout(timeout*1000)
                .build();
        
        try {
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
	        Set<Map.Entry<String, String>> entrySet = map.entrySet();
	        for (Map.Entry<String, String> e : entrySet) {
	            String name = e.getKey();
	            String value = e.getValue();
	            NameValuePair pair = new BasicNameValuePair(name, value);
	            params.add(pair);
	        }
	        
	        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
	        
	        if(logger.isDebugEnabled()){
    			logger.debug("http client url:"+url);
    			//logger.debug("http client params:"+params.toString());
	        }
	        HttpUriRequest reqMethod = null;
	        if (METHOD_POST.equalsIgnoreCase(method)) {
	        	reqMethod = RequestBuilder.post().setUri(url).setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
	        			.setEntity(urlEncodedFormEntity)
	                    .setConfig(requestConfig).build();
	        }else if(METHOD_GET.equalsIgnoreCase(method)) {
	        	reqMethod = RequestBuilder.get().setUri(url).setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
	        			.setEntity(urlEncodedFormEntity)
	                    .setConfig(requestConfig).build();
			}else{
				logger.warn("method unknow, return null.");
	        	return null;
			}
	        CloseableHttpResponse response = null;
	        if(httpclient != null)
	        	response = httpclient.execute(reqMethod);
	        
	        if(response != null && response.getStatusLine().getStatusCode() == 200)
	        	return EntityUtils.toString(response.getEntity(), "UTF-8");
	        else{
	        	if(response != null)
	        		logger.warn("http response status error, status{}, return null"+response.getStatusLine().getStatusCode());
	        	return null;
	        }
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally{
			if(httpclient != null)
				httpclient.close();
		}
	}
	
	
	public static String sendJsonRequestMethod(String json, String url, String method,int timeout) throws Exception{
		
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault(); 
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout*1000)
                .setConnectTimeout(timeout*1000)
                .setConnectionRequestTimeout(timeout*1000)
                .build();
		HttpPost httpPost = new HttpPost(url);
        try {
        	StringEntity entity = new StringEntity(json.toString(),"utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");    
            entity.setContentType("application/json");   
            httpPost.setEntity(entity);
	        
            if(logger.isDebugEnabled())
    			logger.debug("executing request :{}"+httpPost.getRequestLine());
            
            HttpUriRequest reqMethod = RequestBuilder.post().setUri(url).setEntity(entity).setConfig(requestConfig).build();
            CloseableHttpResponse response = null;
            if(httpclient != null)
            	response = httpclient.execute(reqMethod);
            
           if(response != null && response.getStatusLine().getStatusCode() == 200)
	        	return EntityUtils.toString(response.getEntity(), "UTF-8");
	        else{
	        	if(response != null)
	        		logger.warn(" status code {} "+response.getStatusLine().getStatusCode());
	        	logger.warn(" server error, return null");
	        	return null;
	        }
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally{
			if(httpclient != null)
				httpclient.close();
		}
	}
	
//	public static void main(String[] args) {
////		String url = "http://139.129.194.107:10248";
////		Map<String, String> map = new HashMap<String, String>();
////		map.put("trancde", "107");
////		map.put("pay_channel_id", "1");
////		try {
////			String respJson = sendRequestMethod(map, url, METHOD_POST, 20);
////			System.out.println(new String(respJson.getBytes("utf-8"),"UTF-8"));
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//		
//		try {
//			Httpclient.sendRequestMethodPost("","http://219.239.91.114:8080/sdkproxy/sendsms.action",60);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
