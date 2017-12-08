package com.shiyi.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * http工具类
 */

public class HttpUtil {
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	/**
	 * 发送服务器
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public static String sendToServer(String url, Map<String, String> params,String charset)
			throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		// 设置参数
		for (String key : params.keySet()) {
			BasicNameValuePair param = new BasicNameValuePair(key,
					 params.get(key)+"");
			formParams.add(param);
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams,
				"UTF-8");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			InputStream instream = response.getEntity().getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					instream, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} else {
			throw new Exception("地址(" + url + ")不能访问.返回HTTP-" + statusCode
					+ "错误请求");
		}
	}
	public static String sendToServer(String url, Map<String, String> params) 
			throws Exception {
		return sendToServer(url, params,"GBK");
	}

	public static String sendToServerGet(String url, Map<String, Object> params)
			throws Exception {
		HttpClient client = new DefaultHttpClient();
		String requestUrl = url;
		// 设置参数
		for (String key : params.keySet()) {
			if(requestUrl.equals(url)){
				requestUrl =  requestUrl + "?" + key + "=" + (String)params.get(key);
			}else{
				requestUrl = requestUrl + "&" + key + "=" + (String)params.get(key);
			}
		}
		logger.info("requestUrl=" + requestUrl);
		HttpGet get = new HttpGet(requestUrl);
		HttpResponse response = client.execute(get);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			InputStream instream = response.getEntity().getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					instream, "UTF8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} else {
			throw new Exception("地址(" + url + ")不能访问.返回HTTP-" + statusCode
					+ "错误请求");
		}
	}
	/**
	 * 发送服务器  Map<String, Object>
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public static String sendToServer_object(String url, Map<String, Object> params,String charset)
			throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		// 设置参数
		for (String key : params.keySet()) {
			BasicNameValuePair param = new BasicNameValuePair(key,
					 params.get(key)+"");
			formParams.add(param);
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams,
				"UTF-8");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			InputStream instream = response.getEntity().getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					instream, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} else {
			throw new Exception("地址(" + url + ")不能访问.返回HTTP-" + statusCode
					+ "错误请求");
		}
	}



}
